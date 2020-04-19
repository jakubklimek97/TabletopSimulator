package pl.polsl.gk.tabletopSimulator.scenes;

import org.lwjgl.glfw.GLFWKeyCallback;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.handlers.MouseInput;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
public class SceneLoading implements IScene {

    private GLFWKeyCallback keyCallback;
    public KeyboardInput keyboardInput;
    public MouseInput mouseInput;
    public Camera camera;
    public SceneLoading(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
          keyboardInput = new KeyboardInput(window);
          mouseInput = new MouseInput();
          camera = new Camera();
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
             mouseInput.input(window);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwSwapBuffers(window);
            glfwPollEvents();

            if(KeyboardInput.isKeyPressed(GLFW_KEY_J)){
               System.out.println("J");
            }
            if(KeyboardInput.isKeyDown((GLFW_KEY_E))){
                System.out.println("E");
            }

            if(KeyboardInput.isKeyReleased(GLFW_KEY_ESCAPE)){
                glfwSetWindowShouldClose(window, true);
            }
            System.out.println("X:" + mouseInput.getPreviousPos().x + "Y:" + mouseInput.getPreviousPos().y);

        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    private void freeCallbacks(){

        glfwSetKeyCallback(window, null);
    }
    private void setCallbacks(){
        glfwSetKeyCallback(window, keyboardInput.getKeyboardCallback());
        glfwSetCursorPosCallback(window, mouseInput.getCursorPosCallback());
        glfwSetMouseButtonCallback(window, mouseInput.getMouseCallback());
        glfwSetCursorEnterCallback(window, mouseInput.getEnterCallback());
    }
    //Not in use
    public void handleEvents(long window, int key,  int scancode, int action, int mods){
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            glfwSetWindowShouldClose(window, true);
    }

    private SceneManager sceneManager;
    long window;
}
