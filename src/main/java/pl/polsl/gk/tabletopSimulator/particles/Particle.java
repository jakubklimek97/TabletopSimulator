package pl.polsl.gk.tabletopSimulator.particles;

import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;
import pl.polsl.gk.tabletopSimulator.entities.Mesh;

public class Particle {

    private long updateTextureMillis;

    private long currentAnimationTimeMillis;

    private Vector3f speed;

    private long lifeTimeMillis;

    private int animationFrames;

    public Particle(Mesh mesh, Vector3f speed, long lifeTimeMillis, long updateTextureMillis){
        // super(Mesh);
        this.speed = new Vector3f(speed);
        this.lifeTimeMillis = lifeTimeMillis;
        this.updateTextureMillis = updateTextureMillis;
        this.currentAnimationTimeMillis = 0;
        TextureManager texture = this.getMesh().getMaterial().getTexture();
        this.animationFrames = texture.getNumCols() * texture.getNumRows();
    }

    public Particle(Particle baseParticle) {
        //super(baseParticle.getMesh());
        Vector3f aux = baseParticle.getPosition();
        setPosition(aux.x, aux.y, aux.z);
        setRotation(baseParticle.getRotation());
        setScale(baseParticle.getScale());
        this.speed = new Vector3f(baseParticle.speed);
        this.ttl = baseParticle.geTtl();
        this.updateTextureMillis = baseParticle.getUpdateTextureMillis();
        this.currentAnimTimeMillis = 0;
        this.animFrames = baseParticle.getAnimFrames();
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

    public long updateTtl(long elapsedTime) {
        this.lifeTimeMillis -= elapsedTime;
        this.currentAnimationTimeMillis += elapsedTime;
        if ( this.currentAnimationTimeMillis >= this.getUpdateTextureMillis() && this.animationFrames > 0 ) {
            this.currentAnimationTimeMillis = 0;
            int pos = this.getTextPos();
            pos++;
            if ( pos < this.animationFrames ) {
                this.setTextPos(pos);
            } else {
                this.setTextPos(0);
            }
        }
        return this.lifeTimeMillis;
    }
}
