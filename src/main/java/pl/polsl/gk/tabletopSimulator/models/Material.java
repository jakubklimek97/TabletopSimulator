package pl.polsl.gk.tabletopSimulator.models;

import org.joml.Vector4f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;

public class Material {

    private static final Vector4f COLOR = new Vector4f(1.0f,1.0f,1.0f,1.0f);

    private Vector4f ambientLight;

    private Vector4f diffuseLight;

    private  Vector4f specularLight;

    private float reflectFactor;

    private TextureManager texture;

    public Material(){
        this.texture = null;
        this.reflectFactor = 0;
        this.ambientLight = COLOR;
        this.diffuseLight = COLOR;
        this.specularLight = COLOR;
    }

    public Material(TextureManager texture){
        this(COLOR,COLOR,COLOR,texture,0);
    }

    public Material(TextureManager texture, float reflectFactor){
        this(COLOR,COLOR,COLOR,texture,reflectFactor);
    }

    public Material(Vector4f ambientLight, Vector4f diffuseLight,  Vector4f specularLight, TextureManager texture, float reflectFactor){
        this.texture = texture;
        this.reflectFactor = reflectFactor;
        this.ambientLight = COLOR;
        this.diffuseLight = COLOR;
        this.specularLight = COLOR;
    }

    public Vector4f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector4f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public Vector4f getDiffuseLight() {
        return diffuseLight;
    }

    public void setDiffuseLight(Vector4f diffuseLight) {
        this.diffuseLight = diffuseLight;
    }

    public Vector4f getSpecularLight() {
        return specularLight;
    }

    public void setSpecularLight(Vector4f specularLight) {
        this.specularLight = specularLight;
    }

    public float getReflectFactor() {
        return reflectFactor;
    }

    public void setReflectFactor(float reflectFactor) {
        this.reflectFactor = reflectFactor;
    }

    public TextureManager getTexture() {
        return texture;
    }

    public void setTexture(TextureManager texture) {
        this.texture = texture;
    }

    public boolean CheckTexture(){
        return texture != null;
    }

}
