package pl.polsl.gk.tabletopSimulator.particles;

import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;
import pl.polsl.gk.tabletopSimulator.entities.Entity;
import pl.polsl.gk.tabletopSimulator.entities.Mesh;

public class Particle extends Entity {

    private long updateTextureMillis;

    private long currentAnimationTimeMillis;

    private Vector3f speed;

    private long lifeTimeMillis;

    private int animationFrames;

    public Particle(Mesh mesh, Vector3f speed, long lifeTimeMillis, long updateTextureMillis){
         super(mesh);
        this.speed = new Vector3f(speed);
        this.lifeTimeMillis = lifeTimeMillis;
        this.updateTextureMillis = updateTextureMillis;
        this.currentAnimationTimeMillis = 0;
        TextureManager texture = this.getMesh().getMaterial().getTexture();
        this.animationFrames = texture.getNumCols() * texture.getNumRows();
    }

    public Particle(Particle baseParticle) {
        super(baseParticle.getMesh());
        Vector3f aux = baseParticle.getPosition();
        setPosition(aux.x, aux.y, aux.z);
        aux = baseParticle.getRotation();
        setRotation(aux.x,aux.y,aux.z);
        setScale(baseParticle.getScale());
        this.speed = new Vector3f(baseParticle.speed);
        this.lifeTimeMillis = baseParticle.getLifeTimeMillis();
        this.updateTextureMillis = baseParticle.getUpdateTextureMillis();
        this.currentAnimationTimeMillis = 0;
        this.animationFrames = baseParticle.getAnimationFrames();
    }


    public long getUpdateTextureMillis() {
        return updateTextureMillis;
    }

    public void setUpdateTextureMillis(long updateTextureMillis) {
        this.updateTextureMillis = updateTextureMillis;
    }

    public long getCurrentAnimationTimeMillis() {
        return currentAnimationTimeMillis;
    }

    public void setCurrentAnimationTimeMillis(long currentAnimationTimeMillis) {
        this.currentAnimationTimeMillis = currentAnimationTimeMillis;
    }

    public Vector3f getSpeed() {
        return speed;
    }

    public void setSpeed(Vector3f speed) {
        this.speed = speed;
    }

    public long getLifeTimeMillis() {
        return lifeTimeMillis;
    }

    public void setLifeTimeMillis(long lifeTimeMillis) {
        this.lifeTimeMillis = lifeTimeMillis;
    }

    public int getAnimationFrames() {
        return animationFrames;
    }

    public void setAnimationFrames(int animationFrames) {
        this.animationFrames = animationFrames;
    }

    public long updateLifteTimeMill(long elapsedTime) {
        this.lifeTimeMillis -= elapsedTime;
        this.currentAnimationTimeMillis += elapsedTime;
        if ( this.currentAnimationTimeMillis >= this.getUpdateTextureMillis() && this.animationFrames > 0 ) {
            this.currentAnimationTimeMillis = 0;
            int pos = this.getTexturePos();
            pos++;
            if ( pos < this.animationFrames ) {
                this.setTexturePos(pos);
            } else {
                this.setTexturePos(0);
            }
        }
        return this.lifeTimeMillis;
    }
}
