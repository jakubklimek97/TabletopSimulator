package pl.polsl.gk.tabletopSimulator.entities;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import pl.polsl.gk.tabletopSimulator.models.ModelInfo;
import pl.polsl.gk.tabletopSimulator.textures.TextureInfo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final List<Integer> vaoList = new ArrayList<Integer>();
    private final List<Integer> vboList = new ArrayList<Integer>();
    private final List<Integer> texturesList = new ArrayList<Integer>();

    public ModelInfo loadVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        this.setAttributeListData(0, dimensions, positions);
        unbindVAO();
        return new ModelInfo(vaoID, positions.length / dimensions);
    }

    public void cleanUp() {
        for (int vao : vaoList) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vboList) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : texturesList) {
            GL11.glDeleteTextures(texture);
        }
    }

    public int loadToCubeMap(String skyboxType, String[] textureFiles) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < textureFiles.length; ++i) {
            TextureInfo data = decodeTextureFile(skyboxType,textureFiles[i]);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
                    data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                    data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        texturesList.add(texID);
        return texID;
    }

    private TextureInfo decodeTextureFile(String skyboxType, String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            InputStream in = new FileInputStream("src\\main\\resources\\textures\\skybox\\"+ skyboxType + "\\" + fileName + ".png");
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't load file texture: " + fileName);
            System.exit(-1);
        }
        return new TextureInfo(buffer, width, height);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaoList.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void setAttributeListData(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = setFloatBufferData(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer setFloatBufferData(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}