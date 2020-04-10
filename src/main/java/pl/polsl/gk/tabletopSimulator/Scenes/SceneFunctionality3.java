package pl.polsl.gk.tabletopSimulator.Scenes;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.EngineManagers.Items;
import pl.polsl.gk.tabletopSimulator.EngineManagers.Mesh;
import pl.polsl.gk.tabletopSimulator.EngineManagers.Renderer;
import pl.polsl.gk.tabletopSimulator.EngineManagers.TextureManager;
import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.Handlers.MouseInput;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class SceneFunctionality3 implements IScene {


    private final Renderer renderer;

    private final Camera camera;

    private Items[] items;

    private final KeyboardInput keyboardInput;

    private final MouseInput mouseInput;


    @Override
    public void Init() {
        setCallbacks();
        renderer.init();

        float[] positions = new float[]{
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,
                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,
                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,};
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,
                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,
                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,};

        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};


        TextureManager texture = new TextureManager("src\\main\\resources\\textures\\grassblock.png");
        Mesh mesh = new Mesh(indices,positions,textCoords,texture);
        Items item1 = new Items(mesh);
        item1.setScale(.5f);
        item1.setRotation(1f,5.5f,1.1f);
        item1.setPosition(0,0,-2f);
        Items item2 = new Items(mesh);
        item2.setPosition(1.5f, 2.5f, 1f);
        item2.setScale(.5f);
        item2.setRotation(3f,5f,1.1f);
        Items item3 = new Items(mesh);
        item3.setScale(.5f);
        item3.setPosition(0, 0, -2.5f);
        item3.setRotation(7f,7f,7.1f);

        items = new Items[]{item1, item2, item3};

    }

    @Override
    public void UnInit() {
        freeCallbacks();
    }

    @Override
    public void Run() {
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {
            mouseInput.input(window);
          //  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwPollEvents();
            glfwSwapBuffers(window);
           // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
          //  glDisable(GL_TEXTURE_2D);


             camera.input();
            camera.update(mouseInput);

            render(window);
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    public SceneFunctionality3(SceneManager sceneManager){
        mouseInput = new MouseInput();
        keyboardInput = new KeyboardInput();
        renderer = new Renderer();
        camera = new Camera();
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
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
    public void handleEvents(long window, int key,  int scancode, int action, int mods){
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            glfwSetWindowShouldClose(window, true);
    }
    private SceneManager sceneManager;
    private long window;

    public void render(long window){
        renderer.render(camera,items,1280, 720);
    }

    private int vao, vbo;
    private  Shader firstShader;
}
