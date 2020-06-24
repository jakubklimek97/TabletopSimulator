package pl.polsl.gk.tabletopSimulator.utility;

import org.joml.Matrix4f;

public class AnimatedEntityShader extends Shader {
    public AnimatedEntityShader(){
        super("animatedMesh");

    }
    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
        super.createUniform("modelView");
        super.createUniform("projection");
        for(int i = 0; i < 150; ++i){
            super.createUniform("bones["+i+"]");
        }
    }

    @Override
    protected void bindAllUniforms() {

    }
    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix("projection", projectionMatrix);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("modelView", modelViewMatrix);
    }
    public void loadBoneTransformMatrix(Object[] transformMatrix, int matrixSize){
        for(int i = 0; i < matrixSize; ++i){
            super.loadMatrix("bones["+i+"]",(Matrix4f)(transformMatrix[i]));
        }
    }
    public void loadBoneTransformMatrix(Matrix4f[] transformMatrix, int matrixSize){
        for(int i = 0; i < matrixSize; ++i){
            super.loadMatrix("bones["+i+"]",(transformMatrix[i]));
        }
    }
}
