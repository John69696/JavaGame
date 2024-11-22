package javagame.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Wave {
    private ArrayList<Enemy> enemies;
    private ArrayList<HealthPack> healthPacks;
    private ArrayList<Particle> particles;
    private ArrayList<Coin> coins;
    private int waveNumber;
    private int spawnTimer;

    public Wave() {
        enemies = new ArrayList<>();
        healthPacks = new ArrayList<>();
        particles = new ArrayList<>();
        coins = new ArrayList<>();
        waveNumber = 1;
        spawnTimer = 0;
        spawnWave();
    }

    public void update(Player player, ArrayList<Bullet> bullets) {
        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.update(player);
        }

        // Update coins
        Iterator<Coin> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            if (coin.collidesWith(player)) {
                player.addCoins(coin.getValue());
                coinIterator.remove();  // Safely remove coin after collection
            }
        }

        // Update particles
        Iterator<Particle> particleIterator = particles.iterator();
        while (particleIterator.hasNext()) {
            Particle particle = particleIterator.next();
            particle.update();
            if (!particle.isAlive()) {
                particleIterator.remove();  // Remove expired particles
            }
        }

        // Handle bullet collisions with enemies
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        ArrayList<Coin> newCoins = new ArrayList<>();
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        
        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (enemy.getBounds().intersects(bullet.getBounds())) {
                    enemy.takeDamage(1);
                    bulletsToRemove.add(bullet); // Mark bullet for removal
        
                    if (enemy.getHealth() <= 0) {
                        enemiesToRemove.add(enemy); // Mark enemy for removal
                        spawnDeathParticles(enemy);
                        dropCoins(enemy, newCoins); // Generate coins
                        if (Math.random() < 0.15) {
                            spawnHealthPack(enemy.getX(), enemy.getY());
                        }
                    }
                    break; // Stop checking this bullet against other enemies
                }
            }
        }
        
        // Safely remove bullets and enemies outside the loop
        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
        
        // Add new coins to the game
        coins.addAll(newCoins);
        

        // Remove dead enemies and add new coins
        enemies.removeAll(enemiesToRemove);
        coins.addAll(newCoins);

        // Spawn wave after timer expires
        spawnTimer++;
        if (spawnTimer > 200) {
            spawnWave();
            spawnTimer = 0;
        }
    }

    public void draw(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        for (HealthPack healthPack : healthPacks) {
            healthPack.draw(g);
        }

        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.draw(g);
            }
        }

        for (Coin coin : coins) {
            coin.draw(g);
        }

        g.setColor(Color.WHITE);
        g.drawString("Wave: " + waveNumber, 350, 20);
        g.drawString("Enemies Left: " + enemies.size(), 350, 40);
    }

    public void spawnWave() {
        waveNumber++;
        int enemyCount = waveNumber + (int) (Math.random() * 2);
        for (int i = 0; i < enemyCount; i++) {
            enemies.add(createRandomEnemy());
        }
    }

    private Enemy createRandomEnemy() {
        double random = Math.random();
        if (random < 0.4) {
            // Fast enemy
            return new Enemy((int) (Math.random() * 800), (int) (Math.random() * 600), 20, 20, 1, 4, Color.RED);
        } else if (random < 0.8) {
            // Normal enemy
            return new Enemy((int) (Math.random() * 800), (int) (Math.random() * 600), 30, 30, 2, 2, Color.PINK);
        } else {
            // Large, slow enemy
            return new Enemy((int) (Math.random() * 800), (int) (Math.random() * 600), 40, 40, 4, 1, Color.ORANGE);
        }
    }

    public void spawnHealthPack(int x, int y) {
        healthPacks.add(new HealthPack(x, y));
    }

    private void spawnDeathParticles(Enemy enemy) {
        Random rand = new Random();
        int numParticles = 20;
        for (int i = 0; i < numParticles; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double speed = rand.nextDouble() * 4 + 2;
            int offsetX = (int) (Math.cos(angle) * speed);
            int offsetY = (int) (Math.sin(angle) * speed);

            Color particleColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            addParticle(enemy.getX(), enemy.getY(), offsetX, offsetY, particleColor);
        }
    }

    private void dropCoins(Enemy enemy, ArrayList<Coin> newCoins) {
        int coinCount = (int) (Math.random() * 3) + 1;
        for (int i = 0; i < coinCount; i++) {
            newCoins.add(new Coin(enemy.getX(), enemy.getY()));
        }
    }

    public void addParticle(int x, int y, int velocityX, int velocityY, Color color) {
        particles.add(new Particle(x, y, velocityX, velocityY, color));
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<HealthPack> getHealthPacks() {
        return healthPacks;
    }
}
