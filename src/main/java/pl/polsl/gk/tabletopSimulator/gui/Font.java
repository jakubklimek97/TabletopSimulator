package pl.polsl.gk.tabletopSimulator.gui;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.utility.GeometryShader;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;

public class Font {
    public Font(String name, float size, GeometryShader shader){
        this.shader = shader;
        good = false;
        ByteBuffer fontData = null;
         try(InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/"+ name + ".ttf")){
            byte[] fontByteArray = fontStream.readAllBytes();
            fontData = BufferUtils.createByteBuffer(fontByteArray.length);
            fontData.put(fontByteArray);
            fontData.flip();
            this.fontInfo = STBTTFontinfo.malloc();
            stbtt_InitFont(fontInfo, fontData);
            stbtt_ScaleForPixelHeight(fontInfo, 72.0f);
            ByteBuffer bitmapTextureBuffer = ByteBuffer.allocateDirect(512*512);
            STBTTPackContext fontStbBitmap = STBTTPackContext.calloc();
            if(stbtt_PackBegin(fontStbBitmap, bitmapTextureBuffer, 512, 512, 0, 1, 0 ) == true){
                charactersInfo = STBTTPackedchar.malloc(100);
                stbtt_PackFontRange(fontStbBitmap,fontData, 0, size, 32, charactersInfo);
                stbtt_PackEnd(fontStbBitmap);
                try(MemoryStack stack = MemoryStack.stackPush()){
                    IntBuffer buf = stack.mallocInt(1);
                    glGenTextures(buf);
                    bitmapTexture = buf.get(0);
                }
                glBindTexture(GL_TEXTURE_2D, bitmapTexture);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, 512, 512, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmapTextureBuffer);
                glGenerateMipmap(GL_TEXTURE_2D);
                glBindTexture(GL_TEXTURE_2D, 0);
                good = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public STBTTAlignedQuad getCharacterQuad(int character, float[] pos) {
        STBTTAlignedQuad alignedQuad = STBTTAlignedQuad.calloc();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xPos = stack.callocFloat(1);
            FloatBuffer yPos = stack.callocFloat(1);
            xPos.put(pos[0]);
            xPos.flip();
            yPos.put(pos[1]);
            yPos.flip();
            stbtt_GetPackedQuad(charactersInfo, 512, 512, character, xPos, yPos, alignedQuad, false);
            pos[0] = xPos.get(0);
            pos[1] = yPos.get(0);
        }
        return alignedQuad;

    }
    public boolean isGood(){
        return good;
    }
    public int getBitmapTexture(){ return bitmapTexture;}
    public Shader getShader(){return shader;}
    boolean good;
    private int bitmapTexture;
    private STBTTPackedchar.Buffer charactersInfo;
    private STBTTFontinfo fontInfo;
    private GeometryShader shader;
}
