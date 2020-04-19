package pl.polsl.gk.tabletopSimulator.skybox;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class SkyboxShader extends Shader {

    private static final String FILE = "skyboxShader";


    private final TransformManager transformation;

    private int location_projectionMatrix;
    private int location_viewMatrix;


    public SkyboxShader() {
        super(FILE);
        transformation = new TransformManager();
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix("location_projectionMatrix", matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = transformation.getViewMatrix(camera);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        super.loadMatrix("location_viewMatrix", matrix);
    }

    @Override
    public void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAllUniforms() {

        super.createUniform("location_projectionMatrix", location_projectionMatrix);
        super.createUniform("location_viewMatrix", location_viewMatrix);

    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
