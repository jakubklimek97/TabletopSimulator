package pl.polsl.gk.tabletopSimulator.Handlers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;


public final class KeyboardInput {

    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    private static int[] keyStates = new int[GLFW.GLFW_KEY_LAST];

    private GLFWKeyCallback keyboard;


    private static int NO_STATE = -1;

    public KeyboardInput() {
        init();
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isKeyPressed(int key)
    {
        return keyStates[key] == GLFW.GLFW_PRESS;
    }

    public static boolean isKeyReleased(int key)
    {
        return keyStates[key] == GLFW.GLFW_RELEASE;
    }

    private static void resetKeyboard()
    {
        for (int i = 0; i < keyStates.length; i++)
        {
            keyStates[i] = NO_STATE;
        }
    }


    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public void init(){

        keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != org.lwjgl.glfw.GLFW.GLFW_RELEASE);
                keyStates[key] = action;

            }
        };

        resetKeyboard();

    }


}
