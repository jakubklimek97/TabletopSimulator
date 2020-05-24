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
    private boolean start = true;
    public boolean blendTextureDay = false;
    public boolean blendTextureNight = false;
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

    public void render(Camera camera, float R, float G, float B, boolean dayOn, boolean nightOn) {
        glDepthFunc(GL_LEQUAL);
        shader.use();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(R,G,B);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures(dayOn, nightOn);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
        glDepthFunc(GL_LESS);

    }

    public boolean isBlendTextureDay() {
        return blendTextureDay;
    }

    public void setBlendTextureDay(boolean blendTextureDay) {
        this.blendTextureDay = blendTextureDay;
    }

    public boolean isBlendTextureNight() {
        return blendTextureNight;
    }

    public void setBlendTextureNight(boolean blendTextureNight) {
        this.blendTextureNight = blendTextureNight;
    }

    public void bindTextures(boolean dayOn, boolean nightOn){
        time += 30f;
        time %= 82000;
        int texture1 = 0;
        int texture2 = 0;
        float blendFactor = 0;
        if(start){
            texture1 = dayTextureId;
            texture2 = dayTextureId;
            start = false;
            blendFactor = (time - 0)/(5000);
        }

        if(dayOn){
            if(blendTextureNight ) {
                texture1 = dayTextureId;
                texture2 = nightTextureId;
                blendFactor = (time - 5000)/(3000);
            }

            else{
                texture1 = dayTextureId;
                texture2 = dayTextureId;
                blendFactor = (time - 0)/(5000);
            }

        }

        if(nightOn){
            if(!blendTextureDay){
                texture1 = nightTextureId;
                texture2 = nightTextureId;
                blendFactor =  Math.abs((time - 8000)/(13000)) + 0.1f;

            }
            else{
                texture1 = dayTextureId;
                texture2 = nightTextureId;
                blendFactor = Math.abs((time - 20000)/(3000) * 0.5f) + 0.1f;
            }

        }


        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }



}
