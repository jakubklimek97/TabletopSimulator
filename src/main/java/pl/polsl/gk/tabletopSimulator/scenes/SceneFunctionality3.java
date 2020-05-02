package pl.polsl.gk.tabletopSimulator.scenes;


import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Entity;
import pl.polsl.gk.tabletopSimulator.entities.Loader;
import pl.polsl.gk.tabletopSimulator.entities.Mesh;
import pl.polsl.gk.tabletopSimulator.engine.renderers.Renderer;
import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.fog.Fog;
import pl.polsl.gk.tabletopSimulator.handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.handlers.MouseInput;
import pl.polsl.gk.tabletopSimulator.lights.DirectionalLight;
import pl.polsl.gk.tabletopSimulator.lights.Material;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.loaders.OBJLoader;
import pl.polsl.gk.tabletopSimulator.skybox.SkyboxManager;
import pl.polsl.gk.tabletopSimulator.utility.Shader;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBSeamlessCubeMap.GL_TEXTURE_CUBE_MAP_SEAMLESS;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL33C.*;

public class SceneFunctionality3 implements IScene {

    private final SkyboxManager skybox;

    private TransformManager transformManager;

    private Loader loader;

    private final Renderer renderer;

    private final Camera camera;

    private Entity[] items;

    private final KeyboardInput keyboardInput;

    private final MouseInput mouseInput;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static  final float Z_FAR = 1000.0f;

    private PointLight pointLight;

    private Vector3f ambientLight;

    private DirectionalLight directionalLight;

    private Fog fog;

    private static final float R = 0.544f;

    private  static final float G = 0.62f;

    private  static final float B = 0.69f;


    @Override
    public void Init() {

        setCallbacks();
        float reflectFactor = 9.0f;
        TextureManager texture = new TextureManager("src\\main\\resources\\textures\\moon.png");
        TextureManager texture2 = new TextureManager("src\\main\\resources\\textures\\sun.png");
        Mesh mesh = null;
        Mesh mesh2 = null;
        Material material = new Material(texture, reflectFactor);
        Material material2 = new Material(texture2, reflectFactor);

        try{ mesh = OBJLoader.load("/OBJs/sphere.obj");
            mesh2 = OBJLoader.load("/OBJs/sphere.obj");
        }
        catch (Exception e){
            System.out.println("ERROR");
        }
        mesh.setMaterial(material);
        mesh2.setMaterial(material2);
        Entity item1 = new Entity(mesh2);
        item1.setScale(.1f);
        item1.setRotation(1f,5.5f,10f);
        item1.setPosition(1f,5.5f,10f);
        Entity item2 = new Entity(mesh);
        item2.setPosition(1f,5.5f,5f);
        item2.setScale(.1f);
        item2.setRotation(1f,5.5f,10f);

        ambientLight = new Vector3f(1f, 1f, 1f);
        Vector3f lightColour = new Vector3f(0.1f, 0.1f, 0.1f);
        Vector3f lightPosition = new Vector3f(1f,8.5f,5f);
        float lightIntensity = 15f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        float lightIntensity2 = 15f;
        Vector3f lightColour2 = new Vector3f(0.1f, 0.1f, 0.1f);
         Vector3f lightDirection = new Vector3f(-1, 0, 0);
         directionalLight = new DirectionalLight(lightColour2, lightDirection, lightIntensity2);
         fog = new Fog();
         Vector3f fogColour = new Vector3f(0,1,0);
         float density = 0.0f;
         fog.setColour(fogColour);
         fog.setDensityFactor(density);
         fog.setFogStart(2);
         fog.setFogEnd(10);
         fog.setEquationType(2);
         fog.setActive(true);

    items = new Entity[]{item1, item2};
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
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
            glfwPollEvents();
            glfwSwapBuffers(window);
            glEnable(GL_DEPTH_TEST);
            camera.input();
            camera.update(mouseInput);
            render(window);
            skybox.render(camera,R,G,B);
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }
    public SceneFunctionality3(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        this.window = this.sceneManager.getWindow();
        mouseInput = new MouseInput();
        keyboardInput = new KeyboardInput(window);
        renderer = new Renderer();
        camera = new Camera();
        transformManager = new TransformManager();
        loader = new Loader();
        skybox = new SkyboxManager(loader, transformManager.getProjectionMatrix(FOV,1280,720,Z_NEAR,Z_FAR));
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
    private final SceneManager sceneManager;
    private final long window;

    public void render(long window){
        renderer.render(camera,items,1280, 720, ambientLight, pointLight, directionalLight, fog);
    }

    private int vao, vbo;
    private  Shader firstShader;
}
