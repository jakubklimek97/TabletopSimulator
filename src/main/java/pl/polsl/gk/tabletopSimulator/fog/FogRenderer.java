package pl.polsl.gk.tabletopSimulator.fog;

import org.joml.Vector3f;

public class FogRenderer {

    private Vector3f colour;

    private float densityFactor;

    private boolean active;

    public FogRenderer(){
        active = false;
        this.densityFactor = 0f;
        this.colour = new Vector3f(0,0,0);
    }

    public FogRenderer(boolean active, Vector3f colour, float densityFactor){
        this.colour = colour;
        this.densityFactor = densityFactor;
        this.active = active;
    }


    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public float getDensityFactor() {
        return densityFactor;
    }

    public void setDensityFactor(float densityFactor) {
        this.densityFactor = densityFactor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



}
