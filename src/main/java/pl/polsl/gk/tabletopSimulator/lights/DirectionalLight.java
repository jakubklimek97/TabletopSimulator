package pl.polsl.gk.tabletopSimulator.lights;

import org.joml.Vector3f;

public class DirectionalLight {

    private Vector3f colour;
    private Vector3f direction;
    private float intensityFactor;

    public DirectionalLight(Vector3f colour, Vector3f direction, float intensityFactor){
        this.direction = direction;
        this.colour = colour;
        this.intensityFactor = intensityFactor;
    }

    public DirectionalLight(DirectionalLight dirLight){
        this(new Vector3f(dirLight.getDirection()),new Vector3f(dirLight.getColour()), dirLight.getIntensityFactor());
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

    public float getIntensityFactor() {
        return intensityFactor;
    }

    public void setIntensityFactor(float intensityFactor) {
        this.intensityFactor = intensityFactor;
    }



}


