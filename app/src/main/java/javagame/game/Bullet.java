package javagame.game;

import java.awt.*;

public class Bullet {
    private int x, y;
    private double dx, dy;
    private int speed;
    private int size;

    public Bullet(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.size = 5;
        this.dx = Math.cos(angle) * speed;
        this.dy = Math.sin(angle) * speed;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, size, size);
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || y < 0 || x > width || y > height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
