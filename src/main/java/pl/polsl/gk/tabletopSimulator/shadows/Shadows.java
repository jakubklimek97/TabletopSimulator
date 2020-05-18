package pl.polsl.gk.tabletopSimulator.shadows;

import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL30.*;

public class Shadows {

    private int FBO;

    private final TextureManager depthMap;

    public static final int mapWidth = 400;

    public static final int mapHeight = 600;

    public Shadows()  {
        FBO = glGenFramebuffers();


        depthMap = new TextureManager(mapWidth, mapHeight, GL_DEPTH_COMPONENT);
        glBindFramebuffer(GL_FRAMEBUFFER,FBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_TEXTURE_2D, depthMap.getTextureId(),0);
        //Depth only
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            System.out.println("Could not create FrameBuffer!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }

    public int getFBO() {
        return FBO;
    }

    public TextureManager getDepthMap() {
        return depthMap;
    }

    public void cleanup(){
        glDeleteFramebuffers(FBO);
        depthMap.cleanUp();
    }


}
