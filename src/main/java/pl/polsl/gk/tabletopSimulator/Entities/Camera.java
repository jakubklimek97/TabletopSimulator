package pl.polsl.gk.tabletopSimulator.Entities;
import org.lwjgl.glfw.GLFW;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;
public class Camera {

    private Vector3f position = new Vector3f(0,5,0);
    private float roll;
    private float yaw;
    private float pitch = 10;
    private InputHandler input = new InputHandler();

    public void move(){
        if(InputHandler.isKeyDown(GLFW.GLFW_KEY_W)){
            position.z -= 0.2f;
        }
        if(InputHandler.isKeyDown(GLFW.GLFW_KEY_D)){
            position.x += 0.2f;
        }
        if(InputHandler.isKeyDown(GLFW.GLFW_KEY_A)){
            position.x -= 0.2f;
        }
        if(InputHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)){
            position.y +=0.2f;
        }
        if(InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)){
            position.y -=0.2f;
        }
    }

    public Vector3f getPosition(){
        return position;
    }
    public float getPitch(){
        return pitch;
    }
    public float getRoll(){
        return roll;
    }
    public float getYaw(){
        return yaw;
    }

}
