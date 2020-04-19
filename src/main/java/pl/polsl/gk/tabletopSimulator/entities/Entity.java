package pl.polsl.gk.tabletopSimulator.entities;


import org.joml.Vector3f;

public class Entity {


    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;


    public Entity(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    public Mesh getMesh() {
        return mesh;
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

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
