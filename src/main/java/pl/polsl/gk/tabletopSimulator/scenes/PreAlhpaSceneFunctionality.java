package pl.polsl.gk.tabletopSimulator.scenes;

import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class PreAlhpaSceneFunctionality implements IScene {
    @Override
    public void Init() {
        setCallbacks();
       // firstShader = new Shader("firstShader");
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer ip = stack.mallocInt(1);
            glGenVertexArrays(ip);
            vao = ip.get(0);
            glGenBuffers(ip);
            vbo = ip.get(0);

        }
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT,
                false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    @Override
    public void UnInit() {
        freeCallbacks();
    }

    @Override
    public void Run() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwPollEvents();
            glBindVertexArray(vao);
        //    firstShader.Use();
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glfwSwapBuffers(window);
            glBindVertexArray(0);
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    public PreAlhpaSceneFunctionality(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
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
    private long window;
    private float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };
    float texCoords[] = {
            0.0f, 0.0f,  // lower-left corner
            1.0f, 0.0f,  // lower-right corner
            0.5f, 1.0f   // top-center corner
    };
    private int vao, vbo;
    private  Shader firstShader;
}
