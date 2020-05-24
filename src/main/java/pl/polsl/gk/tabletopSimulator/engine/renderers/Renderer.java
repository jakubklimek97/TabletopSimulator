package pl.polsl.gk.tabletopSimulator.engine.renderers;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Entity;
import pl.polsl.gk.tabletopSimulator.fog.Fog;
import pl.polsl.gk.tabletopSimulator.lights.DirectionalLight;
import pl.polsl.gk.tabletopSimulator.engine.managers.OrthogonalCoordsManager;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.lights.LightAndFogShader;
import pl.polsl.gk.tabletopSimulator.shadows.ShadowShader;
import pl.polsl.gk.tabletopSimulator.shadows.Shadows;
import pl.polsl.gk.tabletopSimulator.utility.Shader;


import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;
    private long window;
    private final TransformManager transformation;
    private final float specularPower;
    private final LightAndFogShader lightShader;
    private ShadowShader shadowShader;
    private Shadows shadows;
    private final OrthogonalCoordsManager orthogonalCoordsManager;

    private Entity lastPickedEntity;
    private int framebuffer;
    private int rbo;
    private int textureColorbuffer;
    private MousepickingShader mousePickingShader;


    public Renderer() {

        mousePickingShader = new MousepickingShader();
        mousePickingShader.getAllUniformLocations();
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer tmp = stack.callocInt(1);
            glGenFramebuffers(tmp);
            framebuffer = tmp.get(0);
            glGenTextures(tmp);
            textureColorbuffer = tmp.get(0);
            glGenRenderbuffers(tmp);
            rbo = tmp.get(0);
        }
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glBindTexture(GL_TEXTURE_2D, textureColorbuffer);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 1280, 720, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureColorbuffer, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, 1280, 720);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            System.out.println("ERROR::FRAMEBUFFER:: Framebuffer is not complete!");
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        orthogonalCoordsManager = new OrthogonalCoordsManager(-800.0f, 800.0f, -800.0f, 885.0f, -885.0f, 850.0f);
        shadows = new Shadows();
        shadowShader = new ShadowShader();
        transformation = new TransformManager();
        lightShader = new LightAndFogShader();
        lightShader.use();
        lightShader.bindAllUniforms();
        lightShader.unbind();
        specularPower = 10.0f;

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Camera camera, Entity[] items, int width, int height, Vector3f ambientLight,
                       PointLight light, DirectionalLight directionalLight, Fog fog) {
        clear();
        renderShadows(camera,items,width,height,ambientLight,light,directionalLight);

        glViewport(0, 0, 1280, 720);

       renderScene(camera,items,width,height,ambientLight,light,directionalLight, fog);
       renderPickableEntities(camera, items, width, height,fog);
    }


    public void renderShadows(Camera camera, Entity[] items, int width, int height, Vector3f ambientLight,
                              PointLight light, DirectionalLight directionalLight){

        glBindFramebuffer(GL_FRAMEBUFFER, shadows.getFBO());
        glViewport(0, 0, Shadows.mapWidth, Shadows.mapHeight);
        glClear(GL_DEPTH_BUFFER_BIT);

        shadowShader.use();
        DirectionalLight light1 = directionalLight;
        Vector3f lightDir = light1.getDirection();
        OrthogonalCoordsManager orthogonalCoordsManager1 = orthogonalCoordsManager;
        float lightAngleX = (float)Math.toDegrees(Math.acos(lightDir.z));
        float lightAngleY = (float)Math.toDegrees(Math.asin(lightDir.x));
        float lightAngleZ = 0;

        Matrix4f lightViewMatrix = transformation.updateLightViewMatrix(new Vector3f(lightDir).mul(directionalLight.getShadowPosotionMultiplier()),
                new Vector3f(lightAngleX,lightAngleY,lightAngleZ));
        Matrix4f orthoProjectionMatrix = transformation.updateOrthoProjectionMatrix(orthogonalCoordsManager1.getLeft(), orthogonalCoordsManager1.getRight(),
                orthogonalCoordsManager1.getBottom(), orthogonalCoordsManager1.getTop(), orthogonalCoordsManager1.getNear(), orthogonalCoordsManager1.getFar());
        shadowShader.loadOrthoProjectionMatrix(orthoProjectionMatrix);

        for (Entity item : items) {
            Matrix4f  modelLightViewMatrix = transformation.setupModelViewMatrix(item, lightViewMatrix);
            shadowShader.loadModelLightViewMatrix(modelLightViewMatrix);
            item.getMesh().render();

        }

        shadowShader.unbind();
        //Shadow shader
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


    }

    public void renderPickableEntities(Camera camera, Entity[] items, int width, int height, Fog fog){

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            System.out.println("ERROR::FRAMEBUFFER:: Framebuffer is not complete!");
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        double xPos, yPos;
        try(MemoryStack stack = MemoryStack.stackPush()){
            DoubleBuffer xPosBuf = stack.mallocDouble(1);
            DoubleBuffer yPosBuf = stack.mallocDouble(1);
            org.lwjgl.glfw.GLFW.glfwGetCursorPos(window, xPosBuf,yPosBuf);
            xPos = xPosBuf.get(0);
            yPos = yPosBuf.get(0);
        }
        yPos = Math.abs(yPos-720.0);
        mousePickingShader.use();
        Matrix4f  projectionMatrix = transformation.updateProjectionMatrix(FOV, width, height, Z_NEAR, Z_FAR);
        mousePickingShader.loadProjectionMatrix(projectionMatrix);
        Matrix4f viewMatrix = transformation.setupViewMatrix(camera);
        for(Entity item: items){
            Matrix4f  modelViewMatrix = transformation.setupModelViewMatrix(item, viewMatrix);
            mousePickingShader.loadModelViewMatrix(modelViewMatrix);
            mousePickingShader.setColor(item.getPickColor());
            item.getMesh().render();
        }
        float[] pixel = new float[3];
        glReadBuffer(GL_COLOR_ATTACHMENT0);
        glReadPixels((int)xPos, (int)yPos, 1, 1,GL_RGB, GL_FLOAT, pixel);
        Vector3f selectedColor = new Vector3f(pixel[0], pixel[1], pixel[2]);
        lastPickedEntity = null;
        if(!selectedColor.equals(0.0f, 0.0f, 0.0f)) {
            for (Entity item : items) {
                Vector3f itemColor = item.getPickColor();
                if(selectedColor.equals(itemColor.x,itemColor.y,itemColor.z)){
                    lastPickedEntity = item;
                    break;
                }
            }
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Entity returnPickedEntity(){
        return lastPickedEntity;
    }

    public void renderScene(Camera camera, Entity[] items, int width, int height, Vector3f ambientLight,
                            PointLight light, DirectionalLight directionalLight, Fog fog){


        // Light shader
        lightShader.use();
        Matrix4f  projectionMatrix = transformation.updateProjectionMatrix(FOV, width, height, Z_NEAR, Z_FAR);
        lightShader.loadProjectionMatrix(projectionMatrix);
        Matrix4f viewMatrix = transformation.setupViewMatrix(camera);
        Matrix4f orthoProjectionMatrix = transformation.getOrthoProjectionMatrix();
        Matrix4f lightViewMatrix = transformation.getLightViewMatrix();
        lightShader.loadOrthogonalProjectionMatrix(orthoProjectionMatrix);


        PointLight currPointLight = new PointLight(light);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;


        lightShader.loadTextureSampler(0);
        lightShader.loadFog(fog);

        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f direction = new Vector4f(currDirLight.getDirection(), 0);
        direction.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(direction.x, direction.y, direction.z));

        lightShader.loadAmbientLight(ambientLight);
        lightShader.loadSpecularPower(specularPower);
        lightShader.loadPointLight(currPointLight);
        lightShader.loadDirectionalLight("directionalLight", currDirLight);


        lightShader.loadTextureSampler(0);
        lightShader.loadShadowMap(1);

       // Render each Item in game
        for (Entity item : items) {
            lightShader.loadMaterial(item.getMesh().getMaterial());
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D,shadows.getDepthMap().getTextureId());
            Matrix4f  modelViewMatrix = transformation.setupModelViewMatrix(item, viewMatrix);
            lightShader.loadModelViewMatrix(modelViewMatrix);
            Matrix4f modelLightViewMatrix = transformation.setupModelLightViewMatrix(item,lightViewMatrix);
            lightShader.loadModelLightViewMatrix(modelLightViewMatrix);
            item.getMesh().render();

        }


        lightShader.unbind();

    }
    public void clean() {
        if (lightShader != null)
            lightShader.cleanup();
    }


    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }
}
