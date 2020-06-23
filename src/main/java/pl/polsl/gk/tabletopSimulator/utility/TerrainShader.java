package pl.polsl.gk.tabletopSimulator.utility;

import org.joml.Matrix4f;

public class TerrainShader extends GeometryShader {
    public TerrainShader(){
        super("terrainShader");
        this.getAllUniformLocations();
    }
    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
        super.createUniform("modelViewMatrix");
        super.createUniform("projectionMatrix");
    }

    @Override
    protected void bindAllUniforms() {

    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix("projectionMatrix", projectionMatrix);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("modelViewMatrix", modelViewMatrix);
    }
}
