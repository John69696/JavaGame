package javagame.game;

import java.awt.*;
//import java.util.Random;

public class Particle {
    private int x, y;
    private int velocityX, velocityY;
    private Color color;
    private int lifeSpan;
    private static final int MAX_LIFE = 50;  // Life in frames

    public Particle(int x, int y, int velocityX, int velocityY, Color color) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.lifeSpan = MAX_LIFE;
    }

    // Update the particle position and decrease its lifespan
    public void update() {
        x += velocityX;
        y += velocityY;
        lifeSpan--;
    }

    // Check if the particle is alive (should be removed when life is 0)
    public boolean isAlive() {
        return lifeSpan > 0;
    }

    // Draw the particle
    public void draw(Graphics g) {
        if (isAlive()) {
            g.setColor(color);
            g.fillOval(x, y, 5, 5);  // Particle size of 5px
        }
    }

    // Getter for particle bounds (used for collision detection)
    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 5);  // Particle size
    }
}
