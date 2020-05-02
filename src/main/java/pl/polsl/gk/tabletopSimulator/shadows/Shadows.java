package pl.polsl.gk.tabletopSimulator.shadows;

import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL30.*;

public class Shadows {

    private int FBO;

    private TextureManager depthMap;

    public Shadows() throws Exception {
        FBO = glGenFramebuffers();

        int mapHeight = 1024;
        int mapWidth = 1024;
        depthMap = new TextureManager(mapWidth, mapHeight, GL_DEPTH_COMPONENT);
        glBindFramebuffer(GL_FRAMEBUFFER,FBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_TEXTURE_2D, depthMap.getTextureId(),0);
        //Depth only
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            throw new Exception("Could not create FrameBuffer!");
        }

    }

    public int getFBO() {
        return FBO;
    }

    public void setFBO(int FBO) {
        this.FBO = FBO;
    }

    public TextureManager getDepthMap() {
        return depthMap;
    }

    public void setDepthMap(TextureManager depthMap) {
        this.depthMap = depthMap;
    }

    public void cleanup(){
        glDeleteFramebuffers(FBO);
        depthMap.cleanUp();
    }


}
