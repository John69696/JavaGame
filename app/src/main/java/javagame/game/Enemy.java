package javagame.game;

import java.awt.*;

public class Enemy {
    private int x, y;
    private int width, height;
    private int health;
    private int speed;

    public Enemy(int x, int y, int width, int height, int health, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.speed = speed;
    }

    public void update(Player player) {
        int dx = player.getX() - x;
        int dy = player.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance != 0) {
            x += (int) (dx / distance * speed);
            y += (int) (dy / distance * speed);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getHealth() { return health; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
