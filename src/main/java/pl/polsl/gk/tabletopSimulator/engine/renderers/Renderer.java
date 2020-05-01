package pl.polsl.gk.tabletopSimulator.engine.renderers;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Entity;


import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private final TransformManager transformation;
    public Matrix4f projectionMatrix;
    public Matrix4f viewMatrix;
    public Matrix4f modelViewMatrix;

    private final RendererShader shaderProgram;

    public Renderer() {
        transformation = new TransformManager();
        shaderProgram = new RendererShader();
        shaderProgram.use();
        shaderProgram.bindAllUniforms();
        shaderProgram.unbind();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Camera camera, Entity[] items, int width, int height) {
        clear();
        shaderProgram.use();
        // update projection matrix
        projectionMatrix = transformation.getProjectionMatrix(FOV, width, height, Z_NEAR, Z_FAR);
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        // update view matrix
        items[0].setPosition(items[0].getPosition().x,items[0].getPosition().y,items[0].getPosition().z -= 0.01f);
        viewMatrix = transformation.getViewMatrix(camera);
        shaderProgram.loadTextureSampler(0);
        // Render each Item in game
        for (Entity item : items) {
            modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
            shaderProgram.loadModelViewMatrix(modelViewMatrix);
            item.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void clean() {
        if (shaderProgram != null)
            shaderProgram.cleanup();
    }


}
