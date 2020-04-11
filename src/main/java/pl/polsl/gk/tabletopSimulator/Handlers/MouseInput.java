package pl.polsl.gk.tabletopSimulator.Handlers;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;



import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
    private GLFWCursorPosCallback posCallback;

    private GLFWMouseButtonCallback mouseCallback;

    private GLFWCursorEnterCallback enterCallback;

    private final Vector2f previousPos;

    private final Vector2f currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;

    private boolean rightButtonPressed = false;

    public MouseInput() {
        previousPos = new Vector2f(-1, -1);
        currentPos = new Vector2f(0, 0);
        displVec = new Vector2f();

        init();

    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void init() {


         posCallback = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {

                currentPos.x = (float) xpos;
                currentPos.y = (float) ypos;
            }
        };


        enterCallback = new GLFWCursorEnterCallback() {
            public void invoke(long window, boolean entered) {
                inWindow = entered;
            }
        };

        mouseCallback = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;

            }
        };

    }

public void input(long window){
        displVec.x = 0;
        displVec.y = 0;
        if(previousPos.x > 0 && previousPos.y > 0 && inWindow){

            float deltaX = currentPos.x - previousPos.x;
            float deltaY = currentPos.y - previousPos.y;

            boolean rotateX = deltaX !=0;
            boolean rotateY = deltaY !=0;

            if(rotateX){
                displVec.y = (float) deltaX;
            }
            if(rotateY){
                displVec.x = (float) deltaY;
            }

        }
    previousPos.x = currentPos.x;
    previousPos.y = currentPos.y;

}

    public GLFWCursorPosCallback getCursorPosCallback() {
        return posCallback;
    }

    public GLFWMouseButtonCallback getMouseCallback() {
        return mouseCallback;
    }

    public GLFWCursorEnterCallback getEnterCallback() {
        return enterCallback;
    }

    public Vector2f getPreviousPos() {
        return previousPos;
    }

    public Vector2f getCurrentPos() {
        return currentPos;
    }

}