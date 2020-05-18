package pl.polsl.gk.tabletopSimulator.sun;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class Light2DShader extends Shader {
    private static final String FILE = "2DSpriteShader";
    private int sunTexture;
    private int modelViewMatrix;
    public Light2DShader() {
        super(FILE);
    }

    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
        sunTexture = super.getUniformLocation("sunTexture");
        modelViewMatrix = super.getUniformLocation("modelViewMatrix");

    }

    @Override
    protected void bindAllUniforms() {
        super.createUniform("sunTexture", sunTexture);
        super.createUniform("modelViewMatrix", modelViewMatrix);

    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("modelViewMatrix", modelViewMatrix);
    }

    public void loadSunTexture(int sunTexture) {
        super.loadInt("sunTexture", sunTexture);
    }

}
