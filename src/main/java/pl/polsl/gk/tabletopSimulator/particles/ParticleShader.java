package pl.polsl.gk.tabletopSimulator.particles;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ParticleShader extends Shader {
    private static final String FILE = "particleShader";
    private int modelViewMatrix;
    private int projectionMatrix;
    private int textureXOffset;
    private int textureYOffset;
    private int numCols;
    private int numRows;
    private int textureSampler;
    public ParticleShader() {
        super(FILE);
    }

    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        modelViewMatrix = super.getUniformLocation("modelViewMatrix");
        textureXOffset = super.getUniformLocation("textureXOffset");
        textureYOffset = super.getUniformLocation("textureYOffset");
        numCols = super.getUniformLocation("numCols");
        numRows = super.getUniformLocation("numRows");
        textureSampler = super.getUniformLocation("textureSampler");
    }

    @Override
    protected void bindAllUniforms() {
        super.createUniform("projectionMatrix", projectionMatrix);
        super.createUniform("modelViewMatrix", modelViewMatrix);
        super.createUniform("projectionMatrix", projectionMatrix);
        super.createUniform("modelViewMatrix", modelViewMatrix);
        super.createUniform("textureXOffset", textureXOffset);
        super.createUniform("textureYOffset", textureYOffset);
        super.createUniform("numCols", numCols);
        super.createUniform("numRows", numRows);
        super.createUniform("textureSampler", textureSampler);


    }
    public void loadTextureSampler(int textureSampler) {
        super.loadInt("textureSampler", textureSampler);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("modelViewMatrix", modelViewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix("projectionMatrix", projectionMatrix);
    }

    public void loadTextureXOffset(float textureXOffset) {
        super.loadFloat("textureXOffset", textureXOffset);
    }

    public void loadTextureYOffset(float textureYOffset) {
        super.loadFloat("textureYOffset", textureYOffset);
    }

    public void loadNumCols(int numCols) {
        super.loadInt("numCols", numCols);
    }

    public void loadNumRows(int numRows) {
        super.loadInt("numRows", numRows);
    }

}
