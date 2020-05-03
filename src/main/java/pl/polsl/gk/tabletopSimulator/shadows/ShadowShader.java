package pl.polsl.gk.tabletopSimulator.shadows;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ShadowShader extends Shader {

    private static final String FILE = "shadowShader";
    private int orthoProjectionMatrix;
    private int lightViewMatrix;

    public ShadowShader(){
        super(FILE);
    }
    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
        orthoProjectionMatrix = super.getUniformLocation("orthoProjectionMatrix");
        lightViewMatrix = super.getUniformLocation("lightViewMatrix");

    }

    @Override
    protected void bindAllUniforms() {
    super.createUniform("orthoProjectionMatrix", orthoProjectionMatrix);
    super.createUniform("lightViewMatrix", lightViewMatrix);
    }

    public void loadOrthoProjectionMatrix(Matrix4f orthoProjectionMatrix) {
        super.loadMatrix("orthoProjectionMatrix", orthoProjectionMatrix);
    }

    public void loadLightViewMatrix(Matrix4f lightViewMatrix) {
        super.loadMatrix("lightViewMatrix", lightViewMatrix);
    }

}
