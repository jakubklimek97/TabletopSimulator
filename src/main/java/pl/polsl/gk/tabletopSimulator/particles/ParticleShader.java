package pl.polsl.gk.tabletopSimulator.particles;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ParticleShader extends Shader {
    private static final String FILE = "particleShader";
    private int modelViewMatrix;
    private int projectionMatrix;
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
        modelViewMatrix = super.getUniformLocation("viewMatrix");
        numCols = super.getUniformLocation("numCols");
        numRows = super.getUniformLocation("numRows");
        textureSampler = super.getUniformLocation("texture_sampler");
    }

    @Override
    protected void bindAllUniforms() {
        super.createUniform("projectionMatrix", projectionMatrix);
        super.createUniform("viewMatrix", modelViewMatrix);
        super.createUniform("numCols", numCols);
        super.createUniform("numRows", numRows);
        super.createUniform("texture_sampler", textureSampler);


    }
    public void loadTextureSampler(int textureSampler) {
        super.loadInt("texture_sampler", textureSampler);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("viewMatrix", modelViewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix("projectionMatrix", projectionMatrix);
    }

    public void loadNumCols(int numCols) {
        super.loadInt("numCols", numCols);
    }

    public void loadNumRows(int numRows) {
        super.loadInt("numRows", numRows);
    }

}
