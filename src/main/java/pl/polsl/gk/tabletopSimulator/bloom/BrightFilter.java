package pl.polsl.gk.tabletopSimulator.bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import pl.polsl.gk.tabletopSimulator.postProcessing.ImageRenderer;


public class BrightFilter {

	private ImageRenderer renderer;
	private BrightFilterShader shader;
	
	public BrightFilter(int width, int height){
		shader = new BrightFilterShader();
		renderer = new ImageRenderer(width, height);
	}
	
	public void render(int texture){
		shader.use();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.unbind();
	}
	
	public int getOutputTexture(){
		return renderer.getOutputTexture();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanup();
	}
	
}
