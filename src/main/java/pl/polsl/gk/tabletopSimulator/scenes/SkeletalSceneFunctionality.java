package pl.polsl.gk.tabletopSimulator.scenes;


import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.managers.FontManager;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.engine.renderers.Renderer;
import pl.polsl.gk.tabletopSimulator.entities.*;
import pl.polsl.gk.tabletopSimulator.fog.Fog;
import pl.polsl.gk.tabletopSimulator.gui.Font;
import pl.polsl.gk.tabletopSimulator.gui.HorizontalButtonMenu;
import pl.polsl.gk.tabletopSimulator.gui.MenuShader;
import pl.polsl.gk.tabletopSimulator.gui.TextLine;
import pl.polsl.gk.tabletopSimulator.handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.handlers.MouseInput;
import pl.polsl.gk.tabletopSimulator.lights.DirectionalLight;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.skybox.SkyboxManager;
import pl.polsl.gk.tabletopSimulator.sun.Light2DSprite;
import pl.polsl.gk.tabletopSimulator.sun.Light2DSpriteRenderer;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;

public class SkeletalSceneFunctionality implements IScene {

    private final SkyboxManager skybox;

    private TransformManager transformManager;

    private Loader loader;

    private final Renderer renderer;

    private final Camera camera;

    private Entity[] items;
    private HorizontalButtonMenu menu;

    private final KeyboardInput keyboardInput;

    private final MouseInput mouseInput;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.0f;

    private PointLight pointLight;

    private Vector3f ambientLight;

    private DirectionalLight directionalLight;

    private Fog fog;

    private Vector3f skyboxColourFog = new Vector3f(0.544f, 0.62f, 0.69f);

    private boolean dayOn = false;
    private boolean nightOn = true;

    private Light2DSpriteRenderer light2DSpriteRenderer;
    private Light2DSpriteRenderer moonRenderer;
    private Light2DSprite theLight2DSprite;
    private Light2DSprite theMoon;
    private float lightAngle;
    private final float angleInc;
    private FontManager fontManager;
    private Font font;
    private TextLine version;

    private Entity lastPicked;
    private AnimatedEntity testEntity;
    private Terrain terrain;


    @Override
    public void Init() {

        setCallbacks();
        terrain = new Terrain("Test.png");
        AnimatedMesh aMesh = new AnimatedMesh();
        aMesh.LoadMesh("human.fbx");
        testEntity = new AnimatedEntity(aMesh);
    }

    @Override
    public void UnInit() {
        freeCallbacks();
    }

    @Override
    public void Run() {
        glfwMakeContextCurrent(window);
        camera.setPosition(0, 0, 1);

        while (!glfwWindowShouldClose(window)) {

            mouseInput.input(window);

            glfwPollEvents();

            glfwSwapBuffers(window);
            glClearColor(1.0f, 0f, 0f,0.5f);
            glClear(GL_COLOR_BUFFER_BIT);
            //glEnable(GL_DEPTH_TEST);
            camera.input();
            camera.update(mouseInput);
            //renderer.renderTerrain(camera, terrain, 1280, 720, ambientLight, pointLight, directionalLight, fog);
            testEntity.Draw();



        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }

    public SkeletalSceneFunctionality(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
        mouseInput = new MouseInput();
        keyboardInput = new KeyboardInput(window);
        renderer = new Renderer();
        camera = new Camera();
        transformManager = new TransformManager();
        loader = new Loader();

        skybox = new SkyboxManager(loader, transformManager.updateProjectionMatrix(FOV, 1280, 720, Z_NEAR, Z_FAR));
        lightAngle = 45f;
        angleInc = 0.01f;
        renderer.setWindow(window);
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
        renderer.render(camera, items, 1280, 720, ambientLight, pointLight, directionalLight, fog);

    }

    private int vao, vbo;
    private Shader firstShader;
}
