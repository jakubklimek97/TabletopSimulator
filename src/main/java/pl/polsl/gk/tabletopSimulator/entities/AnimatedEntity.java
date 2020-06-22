package pl.polsl.gk.tabletopSimulator.entities;

import pl.polsl.gk.tabletopSimulator.utility.AnimatedEntityShader;

public class AnimatedEntity {
    public AnimatedEntity(AnimatedMesh mesh){
        this.mesh = mesh;
        sh = new AnimatedEntityShader();
    }
    public void Draw(){
        sh.use();
        mesh.Draw();
    }
    private AnimatedMesh mesh;
    private AnimatedEntityShader sh;

}
