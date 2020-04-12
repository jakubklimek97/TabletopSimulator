package pl.polsl.gk.tabletopSimulator.Handlers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;


public final class KeyboardInput {
    
    private static long window;

    private GLFWKeyCallback keyboard;


    public KeyboardInput(long window) {
        KeyboardInput.window = window;
        init();
    }

    public static boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == GLFW_REPEAT;
    }

    public static boolean isKeyPressed(int key)
    {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    public static boolean isKeyReleased(int key)
    {
        return glfwGetKey(window, key) == GLFW_RELEASE;
    }


    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public void init(){

        keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }

            }
        };


    }


}
