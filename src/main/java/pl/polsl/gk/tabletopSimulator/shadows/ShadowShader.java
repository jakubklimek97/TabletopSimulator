package pl.polsl.gk.tabletopSimulator.shadows;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ShadowShader extends Shader {

    private static final String FILE = "shadowsShader";
    private int orthogonalProjectionMatrix;
    private int modelLightViewMatrix;

    public ShadowShader(){
        super(FILE);
    }
    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
        orthogonalProjectionMatrix = super.getUniformLocation("orthogonalProjectionMatrix");
        modelLightViewMatrix = super.getUniformLocation("modelLightViewMatrix");

    }

    @Override
    public void bindAllUniforms() {
    super.createUniform("orthogonalProjectionMatrix", orthogonalProjectionMatrix);
    super.createUniform("modelLightViewMatrix", modelLightViewMatrix);
    }

    public void loadOrthoProjectionMatrix(Matrix4f orthoProjectionMatrix) {
        super.loadMatrix("orthogonalProjectionMatrix", orthoProjectionMatrix);
    }

    public void loadModelLightViewMatrix(Matrix4f modelLightViewMatrix) {
        super.loadMatrix("modelLightViewMatrix", modelLightViewMatrix);
    }

}
