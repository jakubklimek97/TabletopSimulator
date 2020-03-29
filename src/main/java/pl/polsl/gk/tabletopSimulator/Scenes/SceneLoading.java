package pl.polsl.gk.tabletopSimulator.Scenes;

import org.lwjgl.glfw.GLFWKeyCallback;
import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Entities.InputHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
public class SceneLoading implements IScene {

    private GLFWKeyCallback keyCallback;
    public InputHandler inputHandler = new InputHandler();
    public Camera camera = new Camera();
    public SceneLoading(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
    }

    @Override
    public void Init() {
        setCallbacks();
    }

    @Override
    public void UnInit() {
        freeCallbacks();
    }

    public void Run(){
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwSwapBuffers(window);
            glfwPollEvents();

            if(InputHandler.isKeyPressed(GLFW_KEY_J)){
               System.out.println("J");
            }
            if(InputHandler.isKeyDown((GLFW_KEY_E))){
                System.out.println("E");
            }

            if(InputHandler.isKeyReleased(GLFW_KEY_ESCAPE)){
                glfwSetWindowShouldClose(window, true);
            }
            camera.move();
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    private void freeCallbacks(){

        glfwSetKeyCallback(window, null);
    }
    private void setCallbacks(){
        glfwSetKeyCallback(window, inputHandler.getKeyboardCallback());
        glfwSetCursorPosCallback(window, inputHandler.getMouseMoveCallback());
        glfwSetMouseButtonCallback(window, inputHandler.getMouseButtonsCallback());
    }
    //Not in use
    public void handleEvents(long window, int key,  int scancode, int action, int mods){
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            glfwSetWindowShouldClose(window, true);
    }
    private SceneManager sceneManager;
    long window;
}
