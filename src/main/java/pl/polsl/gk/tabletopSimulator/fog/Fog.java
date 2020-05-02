package pl.polsl.gk.tabletopSimulator.fog;

import org.joml.Vector3f;

public class Fog {

    private Vector3f colour;

    private float densityFactor;

    private boolean active;
    // 1 - linear, 2 - exp, 3 - exp2, any other - 3
    private  int equationType;

    private  float fogStart;

    private float fogEnd ;

    public Fog(){
        active = false;
        this.densityFactor = 0f;
        this.colour = new Vector3f(0,0,0);
        this.fogStart = 0.0f;
        this.fogEnd = 80.0f;
        this.equationType = 2;
    }

    public Fog(boolean active, Vector3f colour, float densityFactor, float fogStart, float fogEnd, int equationType){
        this.colour = colour;
        this.densityFactor = densityFactor;
        this.active = active;
        this.fogStart = fogStart;
        this.fogEnd = fogEnd;
        this.equationType = equationType;
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

    public int getEquationType() {
        return equationType;
    }

    public void setEquationType(int equationType) {
        this.equationType = equationType;
    }

    public float getFogStart() {
        return fogStart;
    }

    public void setFogStart(float fogStart) {
        this.fogStart = fogStart;
    }

    public float getFogEnd() {
        return fogEnd;
    }

    public void setFogEnd(float fogEnd) {
        this.fogEnd = fogEnd;
    }


}
