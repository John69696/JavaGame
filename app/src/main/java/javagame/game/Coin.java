package javagame.game;

import java.awt.*;
import java.util.Random;

public class Coin {
    private int x, y;
    private int size;
    private int value;

    public Coin(int x, int y) {
        this.x = x + new Random().nextInt(10) - 5; // Slight random offset
        this.y = y + new Random().nextInt(10) - 5;
        this.size = 10;
        this.value = 1; // Default coin value is 1
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, size, size);
    }

    public boolean collidesWith(Player player) {
        return new Rectangle(x, y, size, size).intersects(player.getBounds());
    }

    public int getValue() {
        return value;
    }
}
