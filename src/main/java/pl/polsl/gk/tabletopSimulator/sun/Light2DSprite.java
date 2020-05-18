package pl.polsl.gk.tabletopSimulator.sun;

import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;

public class Light2DSprite {
    private static final float DISTANCE = 70;


    private final TextureManager texture;
    private Vector3f lightDir = new Vector3f(0,-1,0);
    private float scale;

    public Light2DSprite(TextureManager texture, float scale){
        this.scale = scale;
        this.texture = texture;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public TextureManager getTexture() {
        return texture;
    }

    public void setLightDir(Vector3f lightDir) {
        this.lightDir = lightDir;
        lightDir.normalize();
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getWorldGamePosition(Vector3f cameraPos){
        Vector3f sunPosition = new Vector3f(lightDir);
        sunPosition.normalize(DISTANCE);
        Vector3f cameraAndSunPosition = new Vector3f();
        sunPosition.add(cameraPos,cameraAndSunPosition);
        return cameraAndSunPosition;
    }


}
