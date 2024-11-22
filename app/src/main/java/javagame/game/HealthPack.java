package javagame.game;

import java.awt.*;

public class HealthPack {
    private int x, y;
    private int size;

    public HealthPack(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 20;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}
