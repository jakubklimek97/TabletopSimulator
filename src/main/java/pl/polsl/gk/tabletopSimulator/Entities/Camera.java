package pl.polsl.gk.tabletopSimulator.Entities;
import org.lwjgl.glfw.GLFW;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector2f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private final Vector3f position;
    private final Vector3f rotation;
    public static float CAMERA_POS_STEP = 0.2f;
    private final Vector3f cameraInc;
    public Camera() {
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        cameraInc = new Vector3f(0,0,0);
    }

    public Camera(Vector3f position, Vector3f rotation){
        this.position = position;
        this.rotation = rotation;
        cameraInc = new Vector3f(0,0,0);
    }

    public Vector3f getPosition(){
        return  position;
    }

    public Vector3f getRotation(){
        return rotation;
    }

    public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }
    public void movePosition(float offsetX, float offsetY, float offsetZ){
        if(offsetZ != 0){
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if(offsetX != 0){
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public void setRotation(float x, float y, float z){
     rotation.x = x;
     rotation.y = y;
     rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ){
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;

    }

    public void input(Window window){

        cameraInc.set(0,0,0);
        if(InputHandler.isKeyPressed(GLFW_KEY_W)){
            cameraInc.z = -1;
        }
        else if (InputHandler.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z = 1;
        }
        if (InputHandler.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x = -1;
        } else if (InputHandler.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x = 1;
        }
        if (InputHandler.isKeyPressed(GLFW_KEY_Z)){
            cameraInc.y = 1;
        } else if (InputHandler.isKeyPressed(GLFW_KEY_X)){
            cameraInc.y = 1;
        }

    }

    public void update(){
        // Update camera position
        movePosition(cameraInc.x * CAMERA_POS_STEP,
                cameraInc.y * CAMERA_POS_STEP,
                cameraInc.z * CAMERA_POS_STEP);
    }
}
