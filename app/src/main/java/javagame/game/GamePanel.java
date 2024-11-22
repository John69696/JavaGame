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
    private boolean gameOver = false;  // Track if the game is over
    private Store store;

    public GamePanel() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        player = new Player(400, 300, this);
        bullets = new ArrayList<>();
        wave = new Wave();
        particleManager = new ParticleManager();
        store = new Store();

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    
    public Store getStore(){
        return store;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver || store.isOpen()) {
            repaint(); // Redraw the store and UI
            return; // Stop updates when the store is open
        }
    
        player.update(up, down, left, right);
        wave.update(player, bullets);
        particleManager.updateParticles();
    
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.isOutOfBounds(getWidth(), getHeight())) {
                bullets.remove(i);
            }
        }
    
        checkCollisions();
        if (player.getHealth() <= 0) {
            triggerGameOver();
        }
        repaint();
    }

    private void triggerGameOver() {
        gameOver = true;
        timer.stop(); // Stop the game loop timer
        repaint(); // Trigger a repaint to display the Game Over screen
    }

    private void checkCollisions() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            for (int j = wave.getEnemies().size() - 1; j >= 0; j--) {
                Enemy enemy = wave.getEnemies().get(j);
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.takeDamage(1);
                    bullets.remove(i);
                    spawnBulletImpactParticles(bullet.getX(), bullet.getY());

                    if (enemy.getHealth() <= 0) {
                        wave.getEnemies().remove(j);
                        spawnEnemyDeathParticles(enemy.getX(), enemy.getY());
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
                    spawnCollisionParticles(player.getX(), player.getY());
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
        for (int i = 0; i < 10; i++) {
            int velocityX = (int) (Math.random() * 10 - 5);
            int velocityY = (int) (Math.random() * 10 - 5);
            Color color = Color.ORANGE;
            particleManager.addParticle(x, y, velocityX, velocityY, color);
        }
    }

    private void spawnEnemyDeathParticles(int x, int y) {
        for (int i = 0; i < 10; i++) {
            int velocityX = (int) (Math.random() * 10 - 5);
            int velocityY = (int) (Math.random() * 10 - 5);
            Color color = Color.RED;
            particleManager.addParticle(x, y, velocityX, velocityY, color);
        }
    }

    private void spawnCollisionParticles(int x, int y) {
        for (int i = 0; i < 10; i++) {
            int velocityX = (int) (Math.random() * 3 - 1);
            int velocityY = (int) (Math.random() * 3 - 1);
            Color color = Color.YELLOW;
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

        if (gameOver) {
            drawGameOverScreen(g);
            return;
        }

        player.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        wave.draw(g);
        particleManager.drawParticles(g);
        if (store.isOpen()) store.draw(g); // Draw store if open
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("GAME OVER", getWidth() / 2 - 150, getHeight() / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press R to Restart", getWidth() / 2 - 100, getHeight() / 2 + 50);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
            return;
        }

   
        if (e.getKeyCode() == KeyEvent.VK_E) {
            store.toggle(); // Open/close the store
            return;
        }
    
        if (store.isOpen()) {
            return; // Ignore other key inputs when the store is open
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_R -> player.reload();
        }
    }

    private void restartGame() {
        player = new Player(400, 300, this);
        bullets.clear();
        wave = new Wave();
        particleManager = new ParticleManager();
        gameOver = false;
        timer.start();
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
    if (store.isOpen()) {
        store.handleMouseClick(e.getX(), e.getY(), player); // Handle store button clicks
    } else {
        double angle = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
        if (player.shoot()) {
            bullets.add(new Bullet(player.getX() + 15, player.getY() + 15, angle));
        }
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
