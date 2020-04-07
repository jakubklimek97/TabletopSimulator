package pl.polsl.gk.tabletopSimulator.EngineManagers;

import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Math.Matrix.Matrix4f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static  final float Z_FAR = 1000.0f;

    private float specularPower;


    public Renderer(){
        specularPower = 10.0f;
    }

    public void init(long window) throws Exception {

        //Create shader



        //Create uniforms for modelView and projection matrices and textures


        // Create unifrom for material


        // Create ligthing related unifroms
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(long window, Camera camera, Items[] items, Vector3f ambientLight) {

        clear();

        // 800 x 600
        glViewport(0, 0, 800, 600);

        //bind shader program

        // update projection matrix


    }

    public void cleanu(){
        //clean shader program
    }


}
