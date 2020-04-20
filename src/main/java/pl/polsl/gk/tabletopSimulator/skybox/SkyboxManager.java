package pl.polsl.gk.tabletopSimulator.skybox;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Loader;
import pl.polsl.gk.tabletopSimulator.models.ModelInfo;

import static org.lwjgl.opengl.GL11.*;



public class SkyboxManager {
    private static final float SIZE = 200f;
    private static final float[] VERTICES = {

            -SIZE, SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            -SIZE, SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE, SIZE,
            SIZE, -SIZE, SIZE

    };

    private static final String[] FACES_FILES = {"skyhsky_right", "skyhsky_left", "skyhsky_top", "skyhsky_bottom", "skyhsky_back", "skyhsky_front"};
    private static final String SKYBOX_TYPE = "skyhsk";
    private final int dayTexture;
    private final ModelInfo cube;
    private final SkyboxShader shader;

    public SkyboxManager(Loader loader, Matrix4f projectionMatrix) {
        cube = loader.loadVAO(VERTICES, 3);
        dayTexture = loader.loadToCubeMap(SKYBOX_TYPE,FACES_FILES);
        shader = new SkyboxShader();
        shader.use();
        shader.getAllUniformLocations();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unbind();

    }

    public void render(Camera camera) {
        glDepthFunc(GL_LEQUAL);
        shader.use();
        shader.loadViewMatrix(camera);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexture);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
        glDepthFunc(GL_LESS);

    }


}
