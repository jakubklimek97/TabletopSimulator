package pl.polsl.gk.tabletopSimulator.entities;

import org.joml.Matrix4f;
import pl.polsl.gk.tabletopSimulator.utility.AnimatedEntityShader;

import java.util.ArrayList;

public class AnimatedEntity {
    public AnimatedEntity(AnimatedMesh mesh){
        this.mesh = mesh;
        sh = new AnimatedEntityShader();
    }
    public void Draw(){
        sh.use();
        ArrayList<Matrix4f> bones = mesh.prepareAndGetBonesTransformArrayList();
        sh.loadBoneTransformMatrix(bones.toArray(), bones.size());
        mesh.Draw();
    }
    private AnimatedMesh mesh;
    private AnimatedEntityShader sh;

}
