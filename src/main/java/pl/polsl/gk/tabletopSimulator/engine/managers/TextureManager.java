package pl.polsl.gk.tabletopSimulator.engine.managers;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import org.lwjgl.opengl.GL13;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureManager {

    private final int textureId;
    private int width;
    private int height;

    private int numRows = 1;

    private int numCols = 1;


    public TextureManager(String fileName, int type) {
        this(loadTexture(fileName, type));

    }
    public TextureManager(int width, int height, int pixelFormat){
        this.textureId = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);


    }

    public TextureManager(int textureId) {
        this.textureId = textureId;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public int getTextureId() {
        return textureId;
    }

    public static int loadTexture(String fileName, int type) {

        int textureId = -1;

            // Error

        try(InputStream vertexUrl1 = TextureManager.class.getClassLoader().getResourceAsStream("textures/" + fileName)){

                // Load texture file
                PNGDecoder decoder = new PNGDecoder(vertexUrl1);

                int width = decoder.getWidth();
                int height = decoder.getHeight();
                ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
                decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
                // Switching from writing to reading mode
                buffer.flip();

                // Create texture
                textureId = glGenTextures();
                // BindTexture
                glBindTexture(GL_TEXTURE_2D, textureId);
                // Unpack rgba bytes. Each element is 1 byte size
                glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                if(type == 0){
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
                }
                else {
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                }
                // Upload the texture data

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
                        GL_UNSIGNED_BYTE, buffer);
                // Generate Mip Map
                glGenerateMipmap(GL_TEXTURE_2D);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Unable to load texture" + fileName);
                System.exit(-1);
            }

        return textureId;
    }

    public void cleanUp() {
        glDeleteTextures(textureId);
    }



}
