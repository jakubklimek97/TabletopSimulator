package pl.polsl.gk.tabletopSimulator.EngineManagers;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final List<Integer> vboIdList;

    private final int vaoId;

    private final int vertexCount;

    public Mesh(int[] indices, float[] texCoords, float[] positions, float[] textureCoords, float[] normals) {
        IntBuffer indicesBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        FloatBuffer normalsVecBuffer = null;
        FloatBuffer positionsBuffer = null;

        try {

            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position of VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            positionsBuffer = MemoryUtil.memAllocFloat(positions.length);
            positionsBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coords of VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            texCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
            texCoordsBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            normalsVecBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalsVecBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, normalsVecBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            //Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (positionsBuffer != null) {
                MemoryUtil.memFree(positionsBuffer);
            }
            if (texCoordsBuffer != null) {
                MemoryUtil.memFree(texCoordsBuffer);
            }
            if (normalsVecBuffer != null) {
                MemoryUtil.memFree(normalsVecBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }

    }

    public int getVaoId(){
        return vaoId;
    }

    public int getVertexCount(){
        return vertexCount;
    }

    public void render(){

        // draw the mesh
        glBindVertexArray(getVaoId());

        glDrawElements(GL_TRIANGLES,getVertexCount(), GL_UNSIGNED_INT,0);

        //Restore state
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D,0);
    }

    public void clean(){
        glDisableVertexAttribArray(0);

        //Delete VBOs
        glBindBuffer(GL_ARRAY_BUFFER,0);
        for(int vboId : vboIdList){
            glDeleteBuffers(vboId);
        }
        //Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);

    }



}
