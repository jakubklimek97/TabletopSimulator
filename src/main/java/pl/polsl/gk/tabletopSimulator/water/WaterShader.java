package pl.polsl.gk.tabletopSimulator.water;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class WaterShader extends Shader {

    private final static String WATER_FILE ="waterShader";

    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;

    public WaterShader() {
        super(WATER_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {

        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_reflectionTexture = super.getUniformLocation("reflectionTexture");
        location_refractionTexture = super.getUniformLocation("refractionTexture");

    }
    @Override
    protected void bindAllUniforms() {

        super.createUniform("projectionMatrix", location_projectionMatrix);
        super.createUniform("viewMatrix", location_viewMatrix);
        super.createUniform("modelMatrix", location_modelMatrix);
        super.createUniform("reflectionTexture", location_reflectionTexture);
        super.createUniform("refractionTexture", location_refractionTexture);

    }

    public void connectTextureUnits() {
        super.loadInt("reflectionTexture", 0);
        super.loadInt("refractionTexture", 1);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix("projectionMatrix", projection);
    }

    public void loadViewMatrix(Matrix4f viewMatrix){
        super.loadMatrix("viewMatrix", viewMatrix);
    }

    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix("modelMatrix", modelMatrix);
    }
}
