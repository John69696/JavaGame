package javagame.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleManager {
    private List<Particle> particles;

    public ParticleManager() {
        this.particles = new ArrayList<>();
    }

    // Add a new particle
    public void addParticle(int x, int y, int velocityX, int velocityY, Color color) {
        particles.add(new Particle(x, y, velocityX, velocityY, color));
    }

    // Update and clean up particles
    public void updateParticles() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update();
            if (!particle.isAlive()) {
                iterator.remove();  // Remove dead particles
            }
        }
    }

    // Draw all the particles
    public void drawParticles(Graphics g) {
        for (Particle particle : particles) {
            particle.draw(g);
        }
    }
}
