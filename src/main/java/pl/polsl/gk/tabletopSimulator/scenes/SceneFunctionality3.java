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

    private float lightAngle;
    private final float angleInc;

    @Override
    public void Init() {

        setCallbacks();
        float reflectFactor = 5.0f;
        TextureManager texture = new TextureManager("src\\main\\resources\\textures\\moon.png");
        TextureManager texture2 = new TextureManager("src\\main\\resources\\textures\\sun.png");
        TextureManager texture3 = new TextureManager("src\\main\\resources\\textures\\moon.png");
        TextureManager texture4 = new TextureManager("src\\main\\resources\\textures\\moon.png");
        Mesh mesh = null;
        Mesh mesh2 = null;
        Mesh mesh3 = null;
        Mesh mesh4 = null;
        Material material = new Material(texture, reflectFactor);
        Material material2 = new Material(texture2, reflectFactor);
        Material material3 = new Material(texture3,reflectFactor);
        Material material4 = new Material(texture4,reflectFactor);
        try{ mesh = OBJLoader.load("/OBJs/sphere.obj");
            mesh2 = OBJLoader.load("/OBJs/sphere.obj");
            mesh3 = OBJLoader.load("/OBJs/plane.obj");
            mesh4 = OBJLoader.load("/OBJs/cube.obj");
        }
        catch (Exception e){
            System.out.println("ERROR");
        }
        mesh.setMaterial(material);
        mesh2.setMaterial(material2);
        mesh3.setMaterial(material3);
        mesh4.setMaterial(material4);
        Entity item1 = new Entity(mesh2);
        item1.setScale(.3f);
        item1.setRotation(0f,0f,0f);
        item1.setPosition(13f,8.5f,19f);
        Entity item2 = new Entity(mesh);
        item2.setPosition(1f,5.5f,15f);
        item2.setScale(.3f);
        item2.setRotation(1f,5.5f,10f);
        Entity item3 = new Entity(mesh3);
        item3.setPosition(0f,-3f,0f);
        item3.setScale(30f);
        item3.setRotation(1f,5.5f,10f);
        Entity item4 = new Entity(mesh4);
        item4.setPosition(-15f,9.5f,15f);
        item4.setScale(3f);
        item4.setRotation(1f,5.5f,10f);

        items = new Entity[]{item1,item2,item3, item4};

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(1f,8.5f,5f);
        float lightIntensity = 0.5f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);


        float lightIntensity2 = 0.1f;
        Vector3f lightColour2 = new Vector3f(1, 1, 1);
         Vector3f lightDirection = new Vector3f(0, 1, 1);
         directionalLight = new DirectionalLight(lightColour2, lightDirection, lightIntensity2);
         fog = new Fog();
         Vector3f fogColour = new Vector3f(0.419f, 0.419f, 0.419f);
         float density = 0.019f;
         fog.setColour(fogColour);
         fog.setDensityFactor(density);
         fog.setFogStart(2);
         fog.setFogEnd(10);
         fog.setEquationType(2);
         fog.setActive(true);

         directionalLight.setShadowPosotionMultiplier(15);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Set the clear color
        glEnable(GL_DEPTH_TEST);
        //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);


    }

    @Override
    public void UnInit() {
        freeCallbacks();
    }

    @Override
    public void Run() {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        while ( !glfwWindowShouldClose(window) ) {
            mouseInput.input(window);
            glfwPollEvents();
            glfwSwapBuffers(window);
            glEnable(GL_DEPTH_TEST);
            camera.input();
            camera.update(mouseInput);
            render(window);
            skybox.render(camera,R,G,B);

            // Update camera position

            float rotX = items[1].getRotation().x;
            rotX += 0.05f;
            if ( rotX >= 360 ) {
                rotX -= 360;
            }
            items[1].getRotation().x = rotX;

            lightAngle += angleInc;
            if ( lightAngle < 0 ) {
                lightAngle = 0;
            } else if (lightAngle > 180 ) {
                lightAngle = 180;
            }
            float zValue = (float)Math.cos(Math.toRadians(lightAngle));
            float yValue = (float)Math.sin(Math.toRadians(lightAngle));
            directionalLight.getDirection().x = 0;
            directionalLight.getDirection().y = yValue;
            directionalLight.getDirection().z = zValue;
            directionalLight.getDirection().normalize();
            float lightAngle = (float)Math.toDegrees(Math.acos(directionalLight.getDirection().z));


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
        skybox = new SkyboxManager(loader, transformManager.updateProjectionMatrix(FOV,1280,720,Z_NEAR,Z_FAR));
        lightAngle = 45f;
        angleInc = 0.01f;
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
