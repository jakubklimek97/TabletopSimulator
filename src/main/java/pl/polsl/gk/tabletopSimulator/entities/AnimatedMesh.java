package pl.polsl.gk.tabletopSimulator.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import pl.polsl.gk.tabletopSimulator.utility.AnimatedEntityShader;

import static org.lwjgl.opengl.GL33C.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.assimp.Assimp.*;

public class AnimatedMesh {
    public boolean LoadMesh(String filename){
        AIScene scene = aiImportFile(filename, aiProcess_JoinIdenticalVertices);
        if(scene == null){
            System.out.println("Error opening model: " + filename);
            return false;
        }
        initMesh(scene);
        return true;
    }
    public void Draw(){
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0 );
        glBindVertexArray(0);
    }
    private void initMesh(AIScene scene){
        int meshCount = scene.mNumMeshes();
        for(int i = 0; i < meshCount; ++i){

            AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
            verticesArray = new float[8*mesh.mNumVertices()];
            vertexBoneDataArray = new VertexBoneData[mesh.mNumVertices()];
            AIVector3D.Buffer txc = mesh.mTextureCoords(0);
            for(int vertice = 0; vertice < mesh.mNumVertices(); ++vertice){
                System.out.println(vertice);
                AIVector3D vtx = mesh.mVertices().get(vertice);
                verticesArray[vertice*8] = vtx.x();
                verticesArray[vertice*8+1] = vtx.y();
                verticesArray[vertice*8+2] = vtx.z();
                AIVector3D nml = mesh.mNormals().get(vertice);
                verticesArray[vertice*8+3] = nml.x();
                verticesArray[vertice*8+4] = nml.y();
                verticesArray[vertice*8+5] = nml.z();
                AIVector3D tt = txc.get(vertice);
                verticesArray[vertice*8+6] = tt.x();
                verticesArray[vertice*8+7] = tt.y();
            }
            int facesCount = mesh.mNumFaces();
            indicesCount = 3* facesCount;
            indices = new int[indicesCount];
            for(int faceI = 0; faceI < facesCount; ++faceI){
                AIFace face = mesh.mFaces().get(faceI);
                if(face.mNumIndices() != 3){
                    System.out.println("ERROR:: model contains face with no 3 vertex");
                }
                else{
                    face.mIndices().get(indices,faceI*3,3);
                }
            }
            if(i > 0){
                System.out.println("ERROR:: model contains more than 1 mesh");
            }
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            ebo = glGenBuffers();
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ARRAY_BUFFER, verticesArray, GL_STATIC_DRAW);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*4, 0);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*4, 3*4);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*4, 6*4);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            LoadMaterials(scene);
            LoadAnimation(scene);
        }

    }
    private void LoadBones(AIMesh mesh){
        int bonesCount = mesh.mNumBones();
        bones = new Bone[bonesCount];
        PointerBuffer ptrBonesArr = mesh.mBones();
        for(int boneIdx = 0; boneIdx < bonesCount; ++ boneIdx){
            AIBone bone = AIBone.create(ptrBonesArr.get(boneIdx));
            String boneName = bone.mName().dataString();
            int boneIndex;
            if(boneMapping.containsKey(boneName)){
                boneIndex = boneMapping.get(boneName);
            }
            else{
                boneMapping.put(boneName, insertedBonesIndex);
                boneIndex = insertedBonesIndex++;
            }
            bones[boneIndex].offsetMatrix = bone.mOffsetMatrix();
            for(int weight = 0; weight < bone.mNumWeights(); ++weight){
                AIVertexWeight vWeight = bone.mWeights().get(weight);
                vertexBoneDataArray[vWeight.mVertexId()].addBoneWeight(boneIndex, vWeight.mWeight());
            }
        }
    }
    private void LoadMaterials(AIScene scene){
        int materialNo = scene.mNumMaterials();
        PointerBuffer pMaterials = scene.mMaterials();
        for(int materialIt = 0; materialIt < materialNo; ++materialIt){
        }
    }
    private void LoadAnimation(AIScene scene){
        int animNo = scene.mNumAnimations();
        PointerBuffer pAnimations = scene.mAnimations();
        if(animNo == 1){




        }
    }
    private float[] verticesArray;
    private int[] indices;
    private VertexBoneData[] vertexBoneDataArray;
    private int vao, vbo, ebo;
    private int indicesCount;
    private HashMap<String, Integer> boneMapping = new HashMap<String, Integer>();
    private int insertedBonesIndex = 0;
    private Bone[] bones;
}
class Bone{
    public AIMatrix4x4 offsetMatrix;
}
class VertexBoneData{
    public int[] boneIds = new int[4];
    public float[] boneWeights = new float[4];
    public void addBoneWeight(int boneId, float boneWeight){
        if(!(numberOfInsertedBones < 4)){
            System.out.println("TOO MUCH BONES");
            return;
        }
        boneIds[numberOfInsertedBones] = boneId;
        boneWeights[numberOfInsertedBones++] = boneWeight;
    }
    private int numberOfInsertedBones = 0;
}




