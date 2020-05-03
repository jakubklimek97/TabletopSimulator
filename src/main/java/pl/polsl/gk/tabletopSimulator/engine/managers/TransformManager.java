package pl.polsl.gk.tabletopSimulator.engine.managers;

import org.joml.Matrix4f;
import org.joml.Vector3f;


import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Entity;

public class TransformManager {

    private final Matrix4f projectionMatrix;

    private final Matrix4f modelViewMatrix;

    private final Matrix4f viewMatrix;

    private final  Matrix4f orthoProjectionMatrix;

    private final Matrix4f lightViewMatrix;

    public TransformManager() {
        projectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        orthoProjectionMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }

    public final Matrix4f setupProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
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

    public Matrix4f setupModelViewMatrix(Entity item, Matrix4f viewMatrix) {
        Vector3f rotation = item.getRotation();
        modelViewMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f setupOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar){
        orthoProjectionMatrix.identity();
        orthoProjectionMatrix.setOrtho(left,right,bottom,top,zNear,zFar);
        return  orthoProjectionMatrix;
    }

    public Matrix4f setupLightViewMatrix(Entity item, Matrix4f matrix){
        Vector3f rotation = item.getRotation();
        lightViewMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        Matrix4f viewCurr = new Matrix4f(lightViewMatrix);
        return viewCurr.mul(lightViewMatrix);
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

}
