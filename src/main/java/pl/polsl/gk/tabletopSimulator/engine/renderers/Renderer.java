package pl.polsl.gk.tabletopSimulator.engine.renderers;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Entity;
import pl.polsl.gk.tabletopSimulator.fog.Fog;
import pl.polsl.gk.tabletopSimulator.lights.DirectionalLight;
import pl.polsl.gk.tabletopSimulator.lights.OrthogonalCoords;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.lights.LightAndFogShader;
import pl.polsl.gk.tabletopSimulator.shadows.ShadowShader;
import pl.polsl.gk.tabletopSimulator.shadows.Shadows;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private final TransformManager transformation;
    private final float specularPower;
    private final LightAndFogShader lightShader;
    private ShadowShader shadowShader;
    private Shadows shadows;
    private final OrthogonalCoords orthogonalCoords;
    public Renderer() {
        orthogonalCoords = new OrthogonalCoords(-20.0f, 20.0f, -20.0f, 25.0f, -15.0f, 50.0f);
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


    }


    public void renderShadows(Camera camera, Entity[] items, int width, int height, Vector3f ambientLight,
                              PointLight light, DirectionalLight directionalLight){

        glBindFramebuffer(GL_FRAMEBUFFER, shadows.getFBO());
        glViewport(0, 0, Shadows.mapWidth, Shadows.mapHeight);
        glClear(GL_DEPTH_BUFFER_BIT);

        shadowShader.use();
        DirectionalLight light1 = directionalLight;
        Vector3f lightDir = light1.getDirection();
        OrthogonalCoords orthogonalCoords1 = orthogonalCoords;
        float lightAngleX = (float)Math.toDegrees(Math.acos(lightDir.z));
        float lightAngleY = (float)Math.toDegrees(Math.asin(lightDir.x));
        float lightAngleZ = 0;

        Matrix4f lightViewMatrix = transformation.updateLightViewMatrix(new Vector3f(lightDir).mul(directionalLight.getShadowPosotionMultiplier()),
                new Vector3f(lightAngleX,lightAngleY,lightAngleZ));
        Matrix4f orthoProjectionMatrix = transformation.updateOrthoProjectionMatrix(orthogonalCoords1.getLeft(),orthogonalCoords1.getRight(),
                orthogonalCoords1.getBottom(),orthogonalCoords1.getTop(),orthogonalCoords1.getNear(),orthogonalCoords1.getFar());
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


}
