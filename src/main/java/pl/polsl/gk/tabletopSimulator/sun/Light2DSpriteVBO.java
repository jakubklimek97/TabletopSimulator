package pl.polsl.gk.tabletopSimulator.sun;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class Light2DSpriteVBO {
        private final int vboId;
        private final int type;

        private Light2DSpriteVBO(int vboId, int type){
            this.vboId = vboId;
            this.type = type;
        }

        public static Light2DSpriteVBO create(int type){
            int id = GL15.glGenBuffers();
            return new Light2DSpriteVBO(id, type);
        }

        public void bind(){
            GL15.glBindBuffer(type, vboId);
        }

        public void unbind(){
            GL15.glBindBuffer(type, 0);
        }

        public void storeDataIntoBuffer(float[] data){
            FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            storeDataIntoBuffer(buffer);
        }

        public void storeDataIntoBuffer(FloatBuffer data){
            GL15.glBufferData(type, data, GL15.GL_STATIC_DRAW);
        }

        public void delete(){
            GL15.glDeleteBuffers(vboId);
        }
}
