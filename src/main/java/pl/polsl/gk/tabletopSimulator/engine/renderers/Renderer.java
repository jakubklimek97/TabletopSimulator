package pl.polsl.gk.tabletopSimulator.engine.renderers;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Entity;
import pl.polsl.gk.tabletopSimulator.entities.Mesh;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.lights.PointLightShader;


import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private final TransformManager transformation;
     private final float specularPower;

    private final PointLightShader pointLightShader;

    public Renderer() {
        transformation = new TransformManager();
        pointLightShader = new PointLightShader();
        pointLightShader.use();
        pointLightShader.bindAllUniforms();
        pointLightShader.unbind();
        specularPower = 10f;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Camera camera, Entity[] items, int width, int height, Vector3f ambientLight,
                       PointLight light) {
        clear();
        pointLightShader.use();
        Matrix4f  projectionMatrix = transformation.getProjectionMatrix(FOV, width, height, Z_NEAR, Z_FAR);
        pointLightShader.loadProjectionMatrix(projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);


        pointLightShader.loadAmbientLight(ambientLight);
        pointLightShader.loadSpecularPower(specularPower);
        light.setPosition(new Vector3f(light.getPosition().x,light.getPosition().y,light.getPosition().z += 0.01f));
        items[1].setPosition(light.getPosition().x,light.getPosition().y + 1f,light.getPosition().z);

        PointLight currPointLight = new PointLight(light);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;

        pointLightShader.loadPointLight(currPointLight);

        pointLightShader.loadTextureSampler(0);

        // Render each Item in game
        for (Entity item : items) {
            Matrix4f  modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
            pointLightShader.loadModelViewMatrix(modelViewMatrix);

            pointLightShader.loadMaterial(item.getMesh().getMaterial());
            item.getMesh().render();

        }

        pointLightShader.unbind();
    }

    public void clean() {
        if (pointLightShader != null)
            pointLightShader.cleanup();
    }


}
