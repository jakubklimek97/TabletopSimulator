package pl.polsl.gk.tabletopSimulator.postProcessing;

public class ContrastChanger  {
  private ImageRenderer renderer;
  private ContrastShader shader;
 
  public ContrastChanger(){
    shader = new ContrastShader();
    render = new ImageRenderer();
  }
  
  public void render(int texture){
    shader.start();
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D_texture);
    renderer.renderQuad();
    shader.stop();
   }
  
  public void cleanUp(){
    renderer.cleanUp();
    shader.cleanUp();
  }
  
  
