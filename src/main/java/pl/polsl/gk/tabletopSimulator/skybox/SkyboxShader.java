package pl.polsl.gk.tabletopSimulator.skybox;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class SkyboxShader extends Shader {

    private static final String FILE = "skyboxShader";
    private static final float ROTATE = 0.05f;

    private final TransformManager transformation;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
    private int location_cubeMapDay;
    private int location_cubeMapNight;
    private int location_blendFactor;
    private float rotation;

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
        rotation += ROTATE;
        matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0));
        super.loadMatrix("location_viewMatrix", matrix);
    }

    public void loadFogColor(float R, float G, float B){
        super.loadVector("location_fogColor", new Vector3f(R,G,B));
    }

    public void connectTextureUnits(){
        super.loadInt("location_cubeMapDay", 0);
        super.loadInt("location_cubeMapNight", 1);
    }


    public void loadBlendFactor(float blend){
        super.loadFloat("location_blendFactor", blend);
    }

    @Override
    public void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor = super.getUniformLocation("fogColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMapDay = super.getUniformLocation("cubeMapDay");
        location_cubeMapNight = super.getUniformLocation("cubeMapNight");
    }

    @Override
    protected void bindAllUniforms() {

        super.createUniform("location_projectionMatrix", location_projectionMatrix);
        super.createUniform("location_viewMatrix", location_viewMatrix);
        super.createUniform("location_fogColor", location_fogColor);
        super.createUniform("location_cubeMapDay", location_cubeMapDay);
        super.createUniform("location_cubeMapNight", location_cubeMapNight);
        super.createUniform("location_blendFactor", location_blendFactor);

    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
