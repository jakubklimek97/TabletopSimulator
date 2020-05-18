package pl.polsl.gk.tabletopSimulator.sun;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;

public class Light2DSpriteRenderer {

    private final Light2DShader light2DShader;
    private TransformManager transformation;

    private static final float[] POSITIONS = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private final Light2DSpriteVAO spriteVAO;
    public Light2DSpriteRenderer() {
        this.light2DShader = new Light2DShader();
        transformation = new TransformManager();
        this.spriteVAO = Light2DSpriteVAO.create();
        spriteVAO.bind();
        spriteVAO.storeData(4,POSITIONS);
        spriteVAO.unbind();
    }

    public void render(Light2DSprite light2DSprite, Camera camera){
        prepare(light2DSprite,camera);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,0,4);
        endRender();
    }
    private void setOpenglFunctionalities(){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void cleanUp(){
        light2DShader.cleanup();
    }

    private void prepare(Light2DSprite light2DSprite, Camera camera){
        setOpenglFunctionalities();
        light2DShader.use();
        Matrix4f modelViewMatrix = calculateModelViewMatrix(light2DSprite,camera);
        light2DShader.loadModelViewMatrix(modelViewMatrix);
        spriteVAO.bind(0);
        light2DShader.loadSunTexture(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, light2DSprite.getTexture().getTextureId());

    }

    private Matrix4f calculateModelViewMatrix(Light2DSprite light2DSprite, Camera camera){
        Matrix4f modelMatrix = new Matrix4f();
        Vector3f sunPos = light2DSprite.getWorldGamePosition(camera.getPosition());
        modelMatrix.translate(sunPos);
        Matrix4f modelViewMatrix = transformation.setViewSpriteMatrix(modelMatrix,transformation.updateViewMatrix(camera));
        modelViewMatrix.scale(new Vector3f(light2DSprite.getScale(), light2DSprite.getScale(), light2DSprite.getScale()), modelViewMatrix);
        return transformation.updateProjectionMatrix(FOV,WINDOW_WIDTH,WINDOW_HEIGHT,Z_NEAR,Z_FAR).mul(modelViewMatrix);
    }



    private void endRender(){
    spriteVAO.unbind(0);
    light2DShader.unbind();
    }
}
