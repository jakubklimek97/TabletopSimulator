package pl.polsl.gk.tabletopSimulator.entities;

import org.joml.Matrix4f;
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
        rootNode = scene.mRootNode();

        for(int i = 0; i < meshCount; ++i){

            AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
            verticesArray = new float[8*mesh.mNumVertices()];
            vertexBoneDataArray = new VertexBoneData[mesh.mNumVertices()];
            for(int it = 0 ; it <mesh.mNumVertices(); ++it ){
                vertexBoneDataArray[it] = new VertexBoneData();
            }
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
            LoadBones(mesh);
            LoadAnimation(scene);
            TransformBones(0);
        }

    }
    private Matrix4f getUsableMatrix(AIMatrix4x4 matrix){
        return new Matrix4f(
                matrix.a1(), matrix.a2(), matrix.a3(), matrix.a4(),
                matrix.b1(), matrix.b2(), matrix.b3(), matrix.b4(),
                matrix.c1(), matrix.c2(), matrix.c3(), matrix.c4(),
                matrix.d1(), matrix.d2(), matrix.d3(), matrix.d4()
        );
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
                bones[insertedBonesIndex] = new Bone();
                boneIndex = insertedBonesIndex++;
            }
            bones[boneIndex].offsetMatrix = getUsableMatrix(bone.mOffsetMatrix());
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
    public void TransformBones(float time){
        Matrix4f transformMatrix = new Matrix4f().identity();
        float ticksPerSecond =  animation.mTicksPerSecond() == 0 ? 25.0f : (float)animation.mTicksPerSecond();
        float timeTicks = ticksPerSecond * time;
        float elapsed = timeTicks % (float)animation.mDuration();
        applyTransform(elapsed, rootNode, transformMatrix);
    }
    private void applyTransform(float animationTime, AINode node, Matrix4f transform){
        String nodeName = node.mName().dataString();
        Matrix4f nodeTransform = getUsableMatrix(node.mTransformation());
        AINodeAnim animationNode = findNode(animation, nodeName);
        if(animationNode != null){

        }
        Matrix4f newTransformation = transform.mul(nodeTransform);

        if(boneMapping.containsKey(nodeName)) {
            int boneIndex = boneMapping.get(nodeName);
            bones[boneIndex].finalTransform = newTransformation.mul(bones[boneIndex].offsetMatrix);
        }

        PointerBuffer pChildren = node.mChildren();
        for(int i = 0; i < node.mNumChildren(); ++i){
            applyTransform(animationTime, AINode.create(pChildren.get(i)), newTransformation);
        }
    }
    private AINodeAnim findNode(AIAnimation animation, String nodeName){
        PointerBuffer pChannels = animation.mChannels();
        for(int i = 0; i < animation.mNumChannels(); ++i){
            AINodeAnim node = AINodeAnim.create(pChannels.get(i));
            if(node.mNodeName().dataString().equals(nodeName) == true){
                return node;
            }
        }
        return null;
    }
    private void LoadAnimation(AIScene scene){
        int animNo = scene.mNumAnimations();
        PointerBuffer pAnimations = scene.mAnimations();
        AIAnimation anim = AIAnimation.create(pAnimations.get(0));
        animation = anim;
        double dur = anim.mDuration();
        double ticks = anim.mTicksPerSecond();
        PointerBuffer pChannels = anim.mChannels();
        AINodeAnim nA = AINodeAnim.create(pChannels.get(0));
        int num = nA.mNumPositionKeys();
        num++;
    }
    private float[] verticesArray;
    private int[] indices;
    private VertexBoneData[] vertexBoneDataArray;
    private int vao, vbo, ebo;
    private int indicesCount;
    private HashMap<String, Integer> boneMapping = new HashMap<String, Integer>();
    private int insertedBonesIndex = 0;
    private Bone[] bones;
    private AINode rootNode;
    private AIAnimation animation;
}
class Bone{
    public Matrix4f offsetMatrix;
    public Matrix4f finalTransform;
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




