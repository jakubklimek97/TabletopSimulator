package pl.polsl.gk.tabletopSimulator.entities;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
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
    private void initMesh(AIScene scene){
        int meshCount = scene.mNumMeshes();
        for(int i = 0; i < meshCount; ++i){
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
        }
    }
    private void LoadMaterials(AIScene scene){
        int materialNo = scene.mNumMaterials();
        PointerBuffer pMaterials = scene.mMaterials();
        for(int materialIt = 0; materialIt < materialNo; ++materialIt){
        }
    }



}


