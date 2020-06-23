package pl.polsl.gk.tabletopSimulator.water;

import java.util.List;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import pl.polsl.gk.tabletopSimulator.engine.managers.TransformManager;
import pl.polsl.gk.tabletopSimulator.entities.Camera;
import pl.polsl.gk.tabletopSimulator.entities.Loader;
import pl.polsl.gk.tabletopSimulator.models.ModelInfo;


public class WaterRenderer {

    private ModelInfo quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    private Matrix4f waterMatrix = new Matrix4f();


    public  Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){

        waterMatrix.identity();
        waterMatrix.translate(translation);
        waterMatrix.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0));
        waterMatrix.rotate((float)Math.toRadians(ry), new Vector3f(0,1,0));
        waterMatrix.rotate((float)Math.toRadians(rz), new Vector3f(0,0,1));
        waterMatrix.scale(new Vector3f(scale, scale, scale));
        return waterMatrix;
    }

    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        shader.use();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unbind();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, Matrix4f viewMatrix) {
        prepareRender(viewMatrix);
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }

    private void prepareRender(Matrix4f viewMatrix){
        shader.use();
        shader.loadViewMatrix(viewMatrix);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
    }

    private void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vertex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadVAO(vertices, 2);
    }

}
