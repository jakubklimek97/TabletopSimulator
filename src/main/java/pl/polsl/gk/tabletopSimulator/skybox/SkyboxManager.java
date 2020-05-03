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
    private static final float SIZE = 400f;
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

    private static final String[] FACES_FILES = {"skyday_right", "skyday_left", "skyday_top", "skyday_bottom", "skyday_back", "skyday_front"};
    private static final String[] FACES_FILES_NIGHT = {"skynight_right", "skynight_left", "skynight_top", "skynight_bottom", "skynight_back", "skynight_front"};
    private static final String SKYBOX_TYPE = "skyday";
    private static final String SKYBOX_TYPE_NIGHT = "skynight";
    private final int dayTextureId;
    private final int nightTextureId;
    private final ModelInfo cube;
    private final SkyboxShader shader;
    private  float time = 0.001f;

    public SkyboxManager(Loader loader, Matrix4f projectionMatrix) {
        cube = loader.loadVAO(VERTICES, 3);
        dayTextureId = loader.loadToCubeMap(SKYBOX_TYPE,FACES_FILES);
        nightTextureId = loader.loadToCubeMap(SKYBOX_TYPE_NIGHT,FACES_FILES_NIGHT);
        shader = new SkyboxShader();
        shader.use();
        shader.connectTextureUnits();
        shader.getAllUniformLocations();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unbind();

    }

    public void render(Camera camera, float R, float G, float B) {
        glDepthFunc(GL_LEQUAL);
        shader.use();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(R,G,B);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
        glDepthFunc(GL_LESS);

    }

    private void bindTextures(){
        time += 30f;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if(time >= 0 && time < 5000){
            texture1 = dayTextureId;
            texture2 = dayTextureId;
            blendFactor = (time - 0)/(5000);
        }else if(time >= 5000 && time < 8000){
            texture1 = dayTextureId;
            texture2 = nightTextureId;
            blendFactor = (time - 5000)/(3000); // 8000 - 5000
        }else if(time >= 8000 && time < 21000){
            texture1 = nightTextureId;
            texture2 = nightTextureId;
            blendFactor = (time - 8000)/(13000);  //21000 - 8000
        }else{
            texture1 = nightTextureId;
            texture2 = dayTextureId;
            blendFactor = (time - 21000)/(3000); // 24000 - 21000
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }



}
