package pl.polsl.gk.tabletopSimulator.EngineManagers;

import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Math.Matrix.Matrix4f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;

public class TransformManager {

    private   Matrix4f modelViewMatrix;
    private  Matrix4f projectionMatrix;
    private  Matrix4f viewMatrix;

    public TransformManager(){
        projectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }


    public Matrix4f getModelViewMatrix(Items item, Matrix4f viewMatrix){
       Vector3f rotation =  item.getRotation();
       modelViewMatrix.setIdentity();
       modelViewMatrix = Matrix4f.translate(item.getPosition().x,item.getPosition().y,item.getPosition().z);
       modelViewMatrix = modelViewMatrix.rotateX((float)Math.toRadians(rotation.x));
       modelViewMatrix = modelViewMatrix.rotateY((float)Math.toRadians(rotation.y));
       modelViewMatrix = modelViewMatrix.rotateZ((float)Math.toRadians(rotation.z));
       modelViewMatrix = Matrix4f.scale(item.getScale(),item.getScale(),item.getScale());
        Matrix4f viewCurrentMatrix = new Matrix4f(viewMatrix);
       viewCurrentMatrix = viewCurrentMatrix.multiply(modelViewMatrix);
        return viewCurrentMatrix;
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar){

        float aspectRatio = width / height;
        projectionMatrix.setIdentity();
        projectionMatrix = Matrix4f.perspective(fov,aspectRatio,zNear,zFar);
        return projectionMatrix;

    }

    public  Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.setIdentity();

        viewMatrix = Matrix4f.rotate((float)Math.toRadians(rotation.x),1,0,0);
        viewMatrix = Matrix4f.rotate((float)Math.toRadians(rotation.y),0,1,0);

       viewMatrix = Matrix4f.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);

        return  viewMatrix;
    }


}
