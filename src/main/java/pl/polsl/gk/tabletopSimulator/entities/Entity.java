package pl.polsl.gk.tabletopSimulator.entities;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {


    private boolean selected;

    private  Mesh[] meshes;

    private final Vector3f position;

    private float scale;

    private final Quaternionf rotation;

    private Vector3f pickColor;

    private String name;

    private int texturePos;
    private boolean insideFrustum;


    public Entity(){
        position = new Vector3f();
        scale = 1;
        rotation = new Quaternionf();
        insideFrustum = true;
        selected = false;
        name = "Not set";
        texturePos = 0;
    }

    public Entity(Mesh mesh) {
       this();
       this.meshes = new Mesh[]{mesh};
    }

    public Entity(Mesh[] meshes){
        this();
        this.meshes = meshes;
    }

    public Mesh getMesh() {
        return meshes[0];
    }

    public  Mesh[] getMeshes(){
        return meshes;
    }

    public void setMeshes(Mesh[] meshes){
        this.meshes = meshes;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    public final void setRotation(Quaternionf q) {
        this.rotation.set(q);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getPickColor() {
        return pickColor;
    }

    public void setPickColor(Vector3f pickColor) {
        this.pickColor = pickColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void cleanup(){
        int numberMeshes = this.meshes != null ? this.meshes.length : 0;
        for(int i = 0; i<numberMeshes; i++){
            this.meshes[i].clean();
        }
    }

    public void setTexturePos(int texturePos){
        this.texturePos = texturePos;
    }

    public int getTexturePos() {
        return texturePos;
    }


}
