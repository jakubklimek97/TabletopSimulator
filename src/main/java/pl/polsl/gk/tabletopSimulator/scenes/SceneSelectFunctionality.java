package pl.polsl.gk.tabletopSimulator.scenes;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.nuklear.NkMouse;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.engine.anim.AnimatedFrame;
import pl.polsl.gk.tabletopSimulator.engine.anim.Animation;
import pl.polsl.gk.tabletopSimulator.engine.assimp.Mesh;
import pl.polsl.gk.tabletopSimulator.engine.managers.AnimMeshesLoader;
import pl.polsl.gk.tabletopSimulator.engine.managers.FontManager;
import pl.polsl.gk.tabletopSimulator.engine.managers.StaticMeshesLoader;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.engine.renderers.Renderer;
import pl.polsl.gk.tabletopSimulator.entities.*;
import pl.polsl.gk.tabletopSimulator.fog.Fog;
import pl.polsl.gk.tabletopSimulator.gui.Font;
import pl.polsl.gk.tabletopSimulator.gui.HorizontalButtonMenu;
import pl.polsl.gk.tabletopSimulator.gui.TextLine;
import pl.polsl.gk.tabletopSimulator.handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.handlers.MouseInput;
import pl.polsl.gk.tabletopSimulator.lights.DirectionalLight;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.nuklear.InterfaceHandler;
import pl.polsl.gk.tabletopSimulator.nuklear.UiLayoutMainMenu;
import pl.polsl.gk.tabletopSimulator.skybox.SkyboxManager;
import pl.polsl.gk.tabletopSimulator.sun.Light2DSprite;
import pl.polsl.gk.tabletopSimulator.sun.Light2DSpriteRenderer;
import pl.polsl.gk.tabletopSimulator.utility.AnimatedEntityShader;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.nk_input_begin;
import static org.lwjgl.nuklear.Nuklear.nk_input_end;
import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class SceneSelectFunctionality implements IScene {

    private final KeyboardInput keyboardInput;

    private final MouseInput mouseInput;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.0f;
    private int width = 1280;
    private int height = 720;

    private InterfaceHandler uiHandler;

    @Override
    public void Init() {
        //setCallbacks();
        uiHandler = new InterfaceHandler(window);
        uiHandler.Init();
    }

    @Override
    public void UnInit() {
        //freeCallbacks();
        uiHandler.UnInit();
    }

    @Override
    public void Run() {
        UiLayoutMainMenu menu = new UiLayoutMainMenu(sceneManager);
        glfwMakeContextCurrent(window);
        while (menu.nextScene == SceneList.SCENE_SELECT) {
            glClearColor(1.0f, 0f, 0f,0.5f);
            glClear(GL_COLOR_BUFFER_BIT);

            uiHandler.newFrame();
            //calc.layout(uiHandler.getContext(), 50, 50);
            //demo.layout(uiHandler.getContext(), 620, 360);
            menu.layout(uiHandler.getContext(), 590, 247);
            uiHandler.drawInterface();

            glfwSwapBuffers(window);
        }
        if(glfwWindowShouldClose(window))
            sceneManager.SwitchScene(SceneList.QUIT);
        else{
            sceneManager.SwitchScene(menu.nextScene);
        }
    }

    public SceneSelectFunctionality(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
        mouseInput = new MouseInput();
        keyboardInput = new KeyboardInput(window);
    }

    private void freeCallbacks() {

        glfwSetKeyCallback(window, null);
    }

    private void setCallbacks() {
        glfwSetKeyCallback(window, keyboardInput.getKeyboardCallback());
        glfwSetCursorPosCallback(window, mouseInput.getCursorPosCallback());
        glfwSetMouseButtonCallback(window, mouseInput.getMouseCallback());
        glfwSetCursorEnterCallback(window, mouseInput.getEnterCallback());
    }

    public void handleEvents(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true);
    }

    private final SceneManager sceneManager;
    private final long window;

    public void render(long window) {
        //renderer.render(camera, items, 1280, 720, ambientLight, pointLight, directionalLight, fog);

    }

    private int vao, vbo;
    private Shader firstShader;


}
