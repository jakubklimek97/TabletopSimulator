package pl.polsl.gk.tabletopSimulator.engine.managers;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Entity;

public class TransformManager {

    private final Matrix4f projectionMatrix;

    private final Matrix4f modelViewMatrix;

    private final Matrix4f viewMatrix;

    private final Matrix4f orthoProjectionMatrix;

    private final Matrix4f lightViewMatrix;

    private final Matrix4f modelMatrix;

    private final Matrix4f modelLightViewMatrix;

    private final Matrix4f modelLightMatrix;

    public TransformManager() {
        projectionMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        modelLightMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        orthoProjectionMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public void setupLightViewMatrix(Matrix4f lightViewMatrix) {
        this.lightViewMatrix.set(lightViewMatrix);
    }

    public final Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    public Matrix4f setupViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public Matrix4f setupModelViewMatrix(Entity item, Matrix4f matrix) {
        Quaternionf rotation = item.getRotation();
        modelMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        modelViewMatrix.set(matrix);
        return modelViewMatrix.mul(modelMatrix);
    }

    public Matrix4f setupModelMatrix(Entity item){
        Quaternionf rotation = item.getRotation();
        modelMatrix.identity().translate(item.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(item.getScale());
        return modelMatrix;
    }

    public Matrix4f setupModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        modelViewMatrix.set(viewMatrix);
        return modelViewMatrix.mul(modelMatrix);
    }

    public Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        orthoProjectionMatrix.identity();
        orthoProjectionMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
        return orthoProjectionMatrix;
    }

    public Matrix4f setupModelLightViewMatrix(Entity item, Matrix4f matrix) {
        Quaternionf rotation = item.getRotation();
        modelLightMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        modelLightViewMatrix.set(matrix);
        return modelLightViewMatrix.mul(modelLightMatrix);
    }

    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    public Matrix4f updateViewMatrix(Camera camera) {
        return updateGenericViewMatrix(camera.getPosition(), camera.getRotation(), viewMatrix);
    }

    private Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    public Matrix4f getOrthoProjectionMatrix() {
        return orthoProjectionMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getModelViewMatrix() {
        return modelViewMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getLightViewMatrix() {
        return lightViewMatrix;

    }

    public Matrix4f setViewSpriteMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        modelMatrix.m00(viewMatrix.m00());
        modelMatrix.m01(viewMatrix.m10());
        modelMatrix.m02(viewMatrix.m20());
        modelMatrix.m10(viewMatrix.m01());
        modelMatrix.m11(viewMatrix.m11());
        modelMatrix.m12(viewMatrix.m21());
        modelMatrix.m20(viewMatrix.m02());
        modelMatrix.m21(viewMatrix.m12());
        modelMatrix.m22(viewMatrix.m22());
        Matrix4f viewMulModelMatrix = new Matrix4f();
        viewMatrix.mul(modelMatrix, viewMulModelMatrix);
        return viewMulModelMatrix;

    }



    public static  Matrix4f updateGenericViewMatrixVersion2(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }



    public Matrix4f buildModelMatrix(Entity entity) {
        Quaternionf rotation = entity.getRotation();
        return modelMatrix.translationRotateScale(
                entity.getPosition().x, entity.getPosition().y, entity.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                entity.getScale(), entity.getScale(), entity.getScale());
    }

    public Matrix4f buildModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(entity), viewMatrix);
    }

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

}
