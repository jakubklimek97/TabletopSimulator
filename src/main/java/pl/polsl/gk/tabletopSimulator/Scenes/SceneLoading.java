package pl.polsl.gk.tabletopSimulator.Scenes;

import org.lwjgl.glfw.GLFWKeyCallback;
import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.Handlers.MouseInput;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
public class SceneLoading implements IScene {

    private GLFWKeyCallback keyCallback;
    public KeyboardInput keyboardInput = new KeyboardInput();
    public MouseInput mouseInput = new MouseInput();
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
            System.out.println("x:" + mouseInput.getPreviousPos().x + "y:" + mouseInput.getPreviousPos().y);

            if(KeyboardInput.isKeyReleased(GLFW_KEY_ESCAPE)){
                glfwSetWindowShouldClose(window, true);
            }
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    private void freeCallbacks(){

        glfwSetKeyCallback(window, null);
    }
    private void setCallbacks(){
        glfwSetKeyCallback(window, keyboardInput.getKeyboardCallback());
        glfwSetCursorPosCallback(window, mouseInput.getPosCallback());
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
