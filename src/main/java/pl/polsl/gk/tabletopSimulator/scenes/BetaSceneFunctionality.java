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
import pl.polsl.gk.tabletopSimulator.gui.*;
import pl.polsl.gk.tabletopSimulator.handlers.KeyboardInput;
import pl.polsl.gk.tabletopSimulator.handlers.MouseInput;
import pl.polsl.gk.tabletopSimulator.lights.DirectionalLight;
import pl.polsl.gk.tabletopSimulator.models.Material;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.loaders.OBJLoader;
import pl.polsl.gk.tabletopSimulator.particles.Emitter;
import pl.polsl.gk.tabletopSimulator.particles.Particle;
import pl.polsl.gk.tabletopSimulator.skybox.SkyboxManager;
import pl.polsl.gk.tabletopSimulator.sun.Light2DSprite;
import pl.polsl.gk.tabletopSimulator.sun.Light2DSpriteRenderer;
import pl.polsl.gk.tabletopSimulator.utility.Shader;
import pl.polsl.gk.tabletopSimulator.engine.managers.FontManager;


import java.io.InputStream;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBSeamlessCubeMap.GL_TEXTURE_CUBE_MAP_SEAMLESS;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL33C.*;

public class BetaSceneFunctionality implements IScene {

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
    private Emitter emitter;
    private Emitter[] emitters;


    //TEST NA TERAZ
    float[] vertices = {
            -0.5f, 0.5f, 0.0f,
            0.5f, 0.5f,0.0f,
            -0.5f, -0.5f,0.0f,
    };
    int tvao, tvbo;
    MenuShader menuSh;

    //TEST NA TERAZ

    @Override
    public void Init() {

        setCallbacks();
       menu = new HorizontalButtonMenu(MenuSide.BOTTOM, 72.0f, 1280, 720);
        menu.AddButton(new TexturedButton(
                new TextureManager("pngFiles/menuTestBtn/normal.png",0),
                new TextureManager("pngFiles/menuTestBtn/hover.png",0),
                new TextureManager("pngFiles/menuTestBtn/active.png",0),
                () -> {
                    System.out.print("Trafiono przycisk");
                }
        ));
        menu.AddButton(new TexturedButton(
                new TextureManager("pngFiles/menuTestBtn/normal.png",0),
                new TextureManager("pngFiles/menuTestBtn/hover.png",0),
                new TextureManager("pngFiles/menuTestBtn/active.png",0),
                () -> {
                    System.out.print("Trafiono przycisk2");
                }
        ));
        menu.Prepare();


        float reflectFactor = 9.0f;
        TextureManager texture = new TextureManager("pngFiles/sun.png", 1);
        TextureManager texture2 = new TextureManager("pngFiles/colormap-lowres.png", 1);
        TextureManager sun2DSprite = new TextureManager("sprites/sun2D.png", 0);
        TextureManager moon2DSprite = new TextureManager("sprites/moon2D.png", 0);
        TextureManager particleTexture = new TextureManager("particles/particle.png",4,4);

        Mesh mesh = null;
        Mesh mesh2 = null;
        Mesh mesh3 = null;
        Mesh mesh4 = null;
        Mesh particleMesh = null;
        Material material = new Material(texture, reflectFactor);
        Material material2 = new Material(texture2, reflectFactor);
        Material material3 = new Material(texture2, reflectFactor);
        Material material4 = new Material(texture, reflectFactor);
        Material particleMaterial = new Material(particleTexture,reflectFactor);



        try{ mesh = OBJLoader.load("/OBJs/sphere.obj");
            mesh2 = OBJLoader.load("/OBJs/sphere.obj");
            mesh3 = OBJLoader.load("/OBJs/Small Tropical Island.obj");
            mesh4 = OBJLoader.load("/OBJs/cube.obj");
            particleMesh = OBJLoader.load("/OBJs/particle/particle.obj");
        }
        catch (Exception e){
            System.out.println("ERROR");
        }
        mesh.setMaterial(material);
        mesh2.setMaterial(material2);
        mesh3.setMaterial(material3);
        mesh4.setMaterial(material4);
        particleMesh.setMaterial(particleMaterial);
        Entity item1 = new Entity(mesh2);
        item1.setPosition(60f,90f,-165f);
        item1.setScale(2f);
        item1.setRotation(1f,-20f,10f);
        item1.setPickColor(new Vector3f(1.0f, 1.0f, 0.0f));
        item1.setName("Item1");

        Entity item2 = new Entity(mesh);
        item2.setPosition(-741,262f,299f);
        item2.setScale(3f);
        item2.setRotation(1f,-20f,10f);
        item2.setPickColor(new Vector3f(0.0f, 1.0f, 0.0f));
        item2.setName("Item2");

        Entity item3 = new Entity(mesh3);
        item3.setPosition(10f,-142f,0f);
        item3.setScale(3f);
        item3.setRotation(1f,5.5f,10f);
        item3.setPickColor(new Vector3f(0.0f, 0.0f, 1.0f));
        item3.setName("Item3");

        items = new Entity[]{item1,item2,item3};


        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(-741,290f,299f);
        float lightIntensity = 0.6f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);


        float lightIntensity2 = 0.2f;
        Vector3f lightColour2 = new Vector3f(1, 1, 1);
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        directionalLight = new DirectionalLight(lightColour2, lightDirection, lightIntensity2);
        fog = new Fog();
        Vector3f fogColour = new Vector3f(0.419f, 0.419f, 0.419f);
        float density = 0.002f;

        fog.setColour(fogColour);
        fog.setDensityFactor(density);
        fog.setFogStart(2);
        fog.setFogEnd(10);
        fog.setEquationType(2);
        fog.setActive(true);

        Vector3f particleSpeed = new Vector3f(1,1,0);
        particleSpeed.mul(2.5f);
        long timeLifeParticle = 4000;
        int maxParticleAmount = 500;
        long creationPeriodMillis = 300;
        float range = 1.2f;
        float scale = 0.9f;

        Particle particle = new Particle(particleMesh,particleSpeed,timeLifeParticle,100);
        particle.setScale(scale);
        particle.setPosition(60.8f,77.5f,-339f);
        emitter = new Emitter(particle,maxParticleAmount,creationPeriodMillis);
        emitter.setActive(true);
        emitter.setPositionRndRange(range);
        emitter.setSpeedRndRange(range);
        emitter.setAnimRange(20);
        emitters = new Emitter[] {emitter};

        theLight2DSprite = new Light2DSprite(sun2DSprite, 25);
        light2DSpriteRenderer = new Light2DSpriteRenderer();
        theLight2DSprite.setLightDir(directionalLight.getDirection());
        moonRenderer = new Light2DSpriteRenderer();
        theMoon = new Light2DSprite(moon2DSprite, 5);
        theMoon.setLightDir(directionalLight.getDirection().negate());

        directionalLight.setShadowPosotionMultiplier(15);
        glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Set the clear color
        glEnable(GL_DEPTH_TEST);

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

       glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        //TODO
        // USTAWIENIE MESHOW I TEKSTUR PO USTAWIENIU PICKOWANIA PSUJE PICKOWANIE
       this.fontManager = FontManager.GetManager();
       this.font = fontManager.GetFont("archivo-narrow/ArchivoNarrow-Regular");
       this.version = new TextLine(this.font, 45);
       this.version.SetScreenResolution(1280, 720);
       this.version.SetPosition(0, 42);
       this.version.SetText("Kliknij obiekt aby go wybrac");
       this.lastPicked = null;
        //TODO
        // NIE DODAWAJ TU MESTHOW I TEKSTUR

    }

    @Override
    public void UnInit() {
        freeCallbacks();
    }

    @Override
    public void Run() {
        glfwMakeContextCurrent(window);
        while (!glfwWindowShouldClose(window)) {
            glClearColor(1.0f, 0f, 0f,0.5f);
            mouseInput.input(window);

            glfwPollEvents();

            glfwSwapBuffers(window);
            int a = 5;
            if(a != 5)
                continue;
            glEnable(GL_DEPTH_TEST);
            camera.input();
            camera.update(mouseInput);
            render(window);
            skybox.render(camera, skyboxColourFog.x, skyboxColourFog.y, skyboxColourFog.z, dayOn, nightOn);
            // Now zValue and yValue below displace directionalLight z and y
            if (directionalLight.getDirection().z > 0.5f) {
                    nightOn = false;
                    dayOn = true;
                skyboxColourFog = new Vector3f(0.544f, 0.62f, 0.69f);
                if (ambientLight.x <= 1.0f) ambientLight.x += 0.01f;
                if (ambientLight.y <= 1.0f) ambientLight.y += 0.01f;
                if (ambientLight.z <= 1.0f) ambientLight.z +=0.01f;

                light2DSpriteRenderer.render(theLight2DSprite, camera);
                theLight2DSprite.setLightDir(directionalLight.getDirection());
            }

            if (directionalLight.getDirection().z < 0.5f) {
                if(directionalLight.getDirection().z > 0.489f) {
                    skybox.setBlendTextureDay(false);
                }
                else {
                    skybox.setBlendTextureDay(false);
                }
                nightOn = true;
                dayOn = false;
                skyboxColourFog = new Vector3f(0.419f, 0.419f, 0.419f);
                if (ambientLight.x >= -1.0f) ambientLight.x -= 0.01f;
                if (ambientLight.y >= -1.0f) ambientLight.y -= 0.01f;
                if (ambientLight.z >= -1.0f) ambientLight.z -= 0.01f;

                moonRenderer.render(theMoon, camera);
                theMoon.setLightDir(directionalLight.getDirection());
            }

            // Update camera position

            float rotX = items[1].getRotation().x;
            rotX += 0.05f;
            if (rotX >= 360) {
                rotX -= 360;
            }
            items[1].getRotation().x = rotX;

            lightAngle += angleInc;
            if (lightAngle < 0) {
                lightAngle = 0;
            } else if (lightAngle > 180) {
                lightAngle = 180;
            }
            float zValue = (float) Math.cos(Math.toRadians(lightAngle));
            float yValue = (float) Math.sin(Math.toRadians(lightAngle));
            directionalLight.getDirection().x = 0;
            directionalLight.getDirection().y = yValue;
            directionalLight.getDirection().z = zValue;
            directionalLight.getDirection().normalize();
            float lightAngle = (float) Math.toDegrees(Math.acos(directionalLight.getDirection().z));

           if (mouseInput.isLeftButtonPressed()) {
               Entity currentPick = renderer.returnPickedEntity();
               if (currentPick != null && lastPicked != currentPick) {
                   lastPicked = currentPick;
                   version.SetText(lastPicked.getName());
               }
           }

           emitter.update((long)(0.1 * 1000));

            glDisable(GL_DEPTH_TEST);
           glDisable(GL_CULL_FACE);
            menu.Render();
           version.Render(1.0f, 0.0f, 0.0f);
            glEnable(GL_CULL_FACE);
        }
        sceneManager.SwitchScene(SceneList.QUIT);
    }

    public BetaSceneFunctionality(SceneManager sceneManager) {
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
        renderer.render(camera, items, emitters, 1280, 720, ambientLight, pointLight, directionalLight, fog);

    }

    private int vao, vbo;
    private Shader firstShader;
}
