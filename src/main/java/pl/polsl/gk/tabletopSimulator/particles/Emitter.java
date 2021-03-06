package pl.polsl.gk.tabletopSimulator.particles;

import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.Game;
import pl.polsl.gk.tabletopSimulator.entities.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Emitter implements IEmitter {

    private int maxParticles;

    private boolean active;

    private final List<Entity> particles;

    private final Particle baseParticle;

    private long creationPeriodMillis;

    private long lastCreationTime;

    private float speedRndRange;

    private float positionRndRange;

    private float scaleRndRange;

    private long animRange;

    public Emitter(Particle baseParticle, int maxParticles, long creationPeriodMillis) {
        particles = new ArrayList<>();
        this.baseParticle = baseParticle;
        this.maxParticles = maxParticles;
        this.active = false;
        this.lastCreationTime = 0;
        this.creationPeriodMillis = creationPeriodMillis;
    }

    public void update(long elapsedTime) {
        long now = System.currentTimeMillis();
        if (lastCreationTime == 0) {
            lastCreationTime = now;
        }
        Iterator<? extends Entity> it = particles.iterator();
        while (it.hasNext()) {
            Particle particle = (Particle) it.next();
            if (particle.updateLifteTimeMill(elapsedTime) < 0) {
                it.remove();
            } else {
                updatePosition(particle, elapsedTime);
            }
        }

        int length = this.getParticles().size();
        if (now - lastCreationTime >= this.creationPeriodMillis && length < maxParticles) {
            createParticle();
            this.lastCreationTime = now;
        }
    }


    private void createParticle() {
        Particle particle = new Particle(this.getBaseParticle());
        // Add a little bit of randomness of the particle
        float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
        float speedInc = sign * (float)Math.random() * this.speedRndRange;
        float posInc = sign * (float)Math.random() * this.positionRndRange;
        float scaleInc = sign * (float)Math.random() * this.scaleRndRange;
        long updateAnimInc = (long)sign *(long)(Math.random() * (float)this.animRange);
        particle.getPosition().add(posInc, posInc, posInc);
        particle.getSpeed().add(speedInc, speedInc, speedInc);
        particle.setScale(particle.getScale() + scaleInc);
        particle.setUpdateTextureMillis(particle.getUpdateTextureMillis() + updateAnimInc);
        particles.add(particle);
    }

    /**
     * Updates a particle position
     * @param particle The particle to update
     * @param elapsedTime Elapsed time in milliseconds
     */
    public void updatePosition(Particle particle, long elapsedTime) {
        Vector3f speed = particle.getSpeed();
        float delta = elapsedTime / 1000.0f;
        float dx = speed.x * delta;
        float dy = speed.y * delta;
        float dz = speed.z * delta;
        Vector3f pos = particle.getPosition();
        particle.setPosition(pos.x + dx, pos.y + dy, pos.z + dz);
    }

    public long getCreationPeriodMillis() {
        return creationPeriodMillis;
    }

    public float getPositionRndRange() {
        return positionRndRange;
    }

    public float getScaleRndRange() {
        return scaleRndRange;
    }

    public float getSpeedRndRange() {
        return speedRndRange;
    }

    public void setAnimRange(long animRange) {
        this.animRange = animRange;
    }

    public void setCreationPeriodMillis(long creationPeriodMillis) {
        this.creationPeriodMillis = creationPeriodMillis;
    }

    public void setMaxParticles(int maxParticles) {
        this.maxParticles = maxParticles;
    }

    public void setPositionRndRange(float positionRndRange) {
        this.positionRndRange = positionRndRange;
    }

    public void setScaleRndRange(float scaleRndRange) {
        this.scaleRndRange = scaleRndRange;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setSpeedRndRange(float speedRndRange) {
        this.speedRndRange = speedRndRange;
    }




    @Override
    public void cleanup() {
        for (Entity particle : getParticles()) {
            particle.cleanup();
        }
    }

    @Override
    public Particle getBaseParticle() {
        return baseParticle;
    }

    @Override
    public List<Entity> getParticles() {
        return particles;
    }
}
