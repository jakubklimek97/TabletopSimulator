package pl.polsl.gk.tabletopSimulator.textures;

import java.nio.ByteBuffer;

public class TextureInfo {

    private final ByteBuffer buffer;
    private final int height;
    private final int width;


    public TextureInfo(ByteBuffer buffer, int width, int height) {

        this.height = height;
        this.width = width;
        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
