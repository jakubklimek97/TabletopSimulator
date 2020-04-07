package pl.polsl.gk.tabletopSimulator.EngineManagers;

import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Math.Matrix.Matrix4f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;

public class TransformManager {

    private final  Matrix4f worldMatrix;
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;

    public TransformManager(){
        projectionMatrix = new Matrix4f();
        worldMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }


    public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale){
        worldMatrix.setIdentity();
        Matrix4f.translate(offset.x,offset.y,offset.z);
        worldMatrix.rotateX((float)Math.toRadians(rotation.x));
        worldMatrix.rotateY((float)Math.toRadians(rotation.y));
        worldMatrix.rotateZ((float)Math.toRadians(rotation.z));
        Matrix4f.scale(scale,scale,scale);

        return worldMatrix;
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar){

        float aspectRatio = width / height;
        projectionMatrix.setIdentity();
        Matrix4f.perspective(fov,aspectRatio,zNear,zFar);
        return projectionMatrix;

    }

    public  Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(rotation.x),1,0,0);
        Matrix4f.rotate((float)Math.toRadians(rotation.y),0,1,0);

        Matrix4f.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);

        return  viewMatrix;
    }


}
