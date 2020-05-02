package pl.polsl.gk.tabletopSimulator.lights;

import org.joml.Vector3f;

public class DirectionalLight {

    private Vector3f colour;
    private Vector3f direction;
    private float intensity;

    public DirectionalLight(Vector3f colour, Vector3f direction, float intensity){
        this.direction = direction;
        this.colour = colour;
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight dirLight){
        this(new Vector3f(dirLight.getColour()), new Vector3f(dirLight.getDirection()), dirLight.getIntensity());
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }



}


