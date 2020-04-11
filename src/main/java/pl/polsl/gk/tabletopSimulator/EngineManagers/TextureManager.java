package pl.polsl.gk.tabletopSimulator.EngineManagers;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class TextureManager {

    private  int id;
    public TextureManager(String fileName){
        this(loadTexture(fileName));
    }

    public TextureManager(int id){
        this.id = id;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId(){
        return id;
    }

    private static int loadTexture(String fileName) {
        //Error
        int textureId = -1;
      try {
          InputStream in = new FileInputStream(fileName);
          // Load texture file
          PNGDecoder decoder = new PNGDecoder(in);

          int width = decoder.getWidth();
          int height = decoder.getHeight();
          ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
          decoder.decode(buffer, width * 4, Format.RGBA);
          // Switching from writing to reading mode
          buffer.flip();

          // Create texture
           textureId = glGenTextures();
          // BindTexture
          glBindTexture(GL_TEXTURE_2D, textureId);
          // Unpack rgba bytes. Each element is 1 byte size
          glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

          // Upload the texture data

          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
                  GL_UNSIGNED_BYTE, buffer);
          // Generate Mip Map
          glGenerateMipmap(GL_TEXTURE_2D);
          in.close();
      } catch (IOException e){
        e.printStackTrace();
      }
        return textureId;

    }

    public void cleanUp(){
        glDeleteTextures(id);
    }

}
