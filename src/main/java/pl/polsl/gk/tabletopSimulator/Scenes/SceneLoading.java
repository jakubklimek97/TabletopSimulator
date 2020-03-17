package pl.polsl.gk.tabletopSimulator.Scenes;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class SceneLoading implements IScene {
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
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    private void freeCallbacks(){
        glfwSetKeyCallback(window, null);
    }
    private void setCallbacks(){
        glfwSetKeyCallback(window, this::handleEvents);
    }
    public void handleEvents(long window, int key,  int scancode, int action, int mods){
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            glfwSetWindowShouldClose(window, true);
    }
    private SceneManager sceneManager;
    long window;
}
