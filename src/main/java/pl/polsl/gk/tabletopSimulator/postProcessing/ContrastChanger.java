package pl.polsl.gk.tabletopSimulator.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ContrastChanger {
    private ImageRenderer renderer;
    private ContrastShader shader;

    public ContrastChanger() {
        shader = new ContrastShader();
        shader.bindAllUniforms();
        renderer = new ImageRenderer();
    }

    public void render(int texture) {
        shader.use();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.unbind();
    }

    public void cleanUp() {
        renderer.cleanUp();
        shader.cleanup();
    }

}