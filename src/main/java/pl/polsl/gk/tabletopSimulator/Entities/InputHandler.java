package pl.polsl.gk.tabletopSimulator.Entities;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;


public final class InputHandler {

    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static int[] keyStates = new int[GLFW.GLFW_KEY_LAST];
    private static double mouseX, mouseY;
    private long window;

    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mouseMove;
    private GLFWMouseButtonCallback mouseButtons;
    private static int NO_STATE = -1;
    public InputHandler() {
        keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != org.lwjgl.glfw.GLFW.GLFW_RELEASE);
                keyStates[key] = action;

            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        mouseButtons = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };
        resetKeyboard();
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

    public static boolean isMouseButtonDown(int button) {
        return buttons[button];
    }

    private static void resetKeyboard()
    {
        for (int i = 0; i < keyStates.length; i++)
        {
            keyStates[i] = NO_STATE;
        }
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public GLFWCursorPosCallback getMouseMoveCallback() {
        return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }




}
