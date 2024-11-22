package javagame.game;

import java.awt.*;

public class Player {
    private int x, y;
    private int width, height;
    private int health;
    private int maxHealth;
    private int ammo;
    private int maxAmmo;
    private int coins;
    private boolean reloading;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.maxAmmo = 10;
        this.ammo = maxAmmo;
        this.coins = 0;
        this.reloading = false;
    }

    public void update(boolean up, boolean down, boolean left, boolean right) {
        int speed = 5;
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;
    }

    public boolean shoot() {
        if (ammo > 0 && !reloading) {
            ammo--;
            return true;
        }
        return false;
    }

    public void reload() {
        if (ammo < maxAmmo) {
            reloading = true;
            ammo = maxAmmo; // Instant reload for simplicity
            reloading = false;
        }
    }
    public void collectCoin(Coin coin) {
        coins += coin.getValue();
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);

        // Draw health bar
        g.setColor(Color.GREEN);
        g.fillRect(10, 10, (int) (200 * (health / (double) maxHealth)), 20);
        g.setColor(Color.WHITE);
        g.drawRect(10, 10, 200, 20);
        g.drawString("Health: " + health, 220, 25);

        // Draw ammo
        g.setColor(Color.YELLOW);
        for (int i = 0; i < ammo; i++) {
            g.fillOval(10 + i * 15, 40, 10, 10);
        }
        g.setColor(Color.WHITE);
        g.drawString("Ammo: " + ammo, 220, 50);

        // Draw coins
        g.setColor(Color.YELLOW);
        g.drawString("Coins: " + coins, 10, 70);
    }

    public boolean takeDamage(int damage) {
        health -= damage;
        return true;
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getHealth(){
        return health;
    }
}
