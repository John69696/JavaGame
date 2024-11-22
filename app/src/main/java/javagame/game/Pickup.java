package javagame.game;

import java.awt.*;

public class Pickup {
    public enum Type { COIN, HEALTH_PACK }

    private int x, y, size = 10;
    private Type type;

    public Pickup(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public Type getType() {
        return type;
    }

    public void draw(Graphics g) {
        if (type == Type.COIN) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, size, size);
        } else if (type == Type.HEALTH_PACK) {
            g.setColor(Color.PINK);
            g.fillRect(x, y, size, size);
        }
    }
}
