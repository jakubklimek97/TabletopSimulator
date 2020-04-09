package pl.polsl.gk.tabletopSimulator.EngineManagers;

import org.lwjgl.system.CallbackI;
import pl.polsl.gk.tabletopSimulator.Entities.Camera;
import pl.polsl.gk.tabletopSimulator.Math.Matrix.Matrix4f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static  final float Z_FAR = 1000.0f;

    private final TransformManager transformation;

    private Shader shaderProgram;

    public Renderer(){
        transformation = new TransformManager();
        init();
    }

    public void init() {

        shaderProgram = new Shader("secondShader");

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(long window, Camera camera, Items[] items) {
        clear();
        // 800 x 600
        glViewport(0, 0, 800, 600);

        shaderProgram.Use();

        // update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV,800,600, Z_NEAR,Z_FAR);
        shaderProgram.setUniform("projectionMatrix",projectionMatrix);
        // update view matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler",0);
        // Render each Item in game
        for(Items item : items){

            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item,viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
           Mesh mesh =  item.getMesh();
           mesh.render();
        }

        shaderProgram.unbind();
    }

    public void clean(){
        if(shaderProgram != null)
            shaderProgram.cleanup();
    }


}
