package javagame.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    private Timer timer;
    private Player player;
    private ArrayList<Bullet> bullets;
    private Wave wave;
    private ParticleManager particleManager;  // Particle Manager

    private boolean up, down, left, right;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK); // Set the background to black
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        player = new Player(400, 300);
        bullets = new ArrayList<>();
        wave = new Wave();
        particleManager = new ParticleManager();  // Initialize the particle manager

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.update(up, down, left, right);
        wave.update(player, bullets);

        // Update particles
        particleManager.updateParticles();

        // Update bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.isOutOfBounds(getWidth(), getHeight())) {
                bullets.remove(i);
            }
        }

        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            for (int j = wave.getEnemies().size() - 1; j >= 0; j--) {
                Enemy enemy = wave.getEnemies().get(j);
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.takeDamage(1);
                    bullets.remove(i);
                    spawnBulletImpactParticles(bullet.getX(), bullet.getY());  // Particle effect on bullet hit

                    if (enemy.getHealth() <= 0) {
                        wave.getEnemies().remove(j);
                        spawnEnemyDeathParticles(enemy.getX(), enemy.getY());  // Particle effect on enemy death
                        player.addCoins((int) (Math.random() * 3) + 1);
                        if (Math.random() < 0.15) {
                            wave.spawnHealthPack(enemy.getX(), enemy.getY());
                        }
                    }
                    break;
                }
            }
        }

        for (Enemy enemy : wave.getEnemies()) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                if (player.takeDamage(10)) {
                    pushBackEnemy(enemy);
                    spawnCollisionParticles(player.getX(), player.getY());  // Particle effect on player-enemy collision
                }
            }
        }

        for (int i = wave.getHealthPacks().size() - 1; i >= 0; i--) {
            HealthPack healthPack = wave.getHealthPacks().get(i);
            if (player.getBounds().intersects(healthPack.getBounds())) {
                player.heal(20);
                wave.getHealthPacks().remove(i);
            }
        }
    }

    private void spawnBulletImpactParticles(int x, int y) {
        // Spawning particles at the location where the bullet hits
        for (int i = 0; i < 15; i++) {  // Reduced from 50 to 15
            int velocityX = (int) (Math.random() * 10 - 5);  // Random velocity
            int velocityY = (int) (Math.random() * 10 - 5);  // Random velocity
            Color color = Color.ORANGE;  // Color of the particles
            particleManager.addParticle(x, y, velocityX, velocityY, color);
        }
    }
    
    private void spawnEnemyDeathParticles(int x, int y) {
        // Spawning explosion-like particles on enemy death
        for (int i = 0; i < 15; i++) {  // Reduced from 50 to 15
            int velocityX = (int) (Math.random() * 10 - 5);  // Random velocity
            int velocityY = (int) (Math.random() * 10 - 5);  // Random velocity
            Color color = Color.RED;  // Explosion color
            particleManager.addParticle(x, y, velocityX, velocityY, color);
        }
    }
    
    private void spawnCollisionParticles(int x, int y) {
        // Spawning small particles when player collides with an enemy
        for (int i = 0; i < 15; i++) {  // Reduced from 50 to 15
            int velocityX = (int) (Math.random() * 3 - 1);  // Small random velocity
            int velocityY = (int) (Math.random() * 3 - 1);  // Small random velocity
            Color color = Color.YELLOW;  // Color of the particles
            particleManager.addParticle(x, y, velocityX, velocityY, color);
        }
    }
    

    private void pushBackEnemy(Enemy enemy) {
        int dx = enemy.getX() - player.getX();
        int dy = enemy.getY() - player.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance != 0) {
            enemy.setPosition(enemy.getX() + (int) (dx / distance * 50), enemy.getY() + (int) (dy / distance * 50));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        wave.draw(g);

        // Draw particles
        particleManager.drawParticles(g);  // Draw all particles
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_R -> player.reload();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        double angle = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
        if (player.shoot()) {
            bullets.add(new Bullet(player.getX() + 15, player.getY() + 15, angle));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
