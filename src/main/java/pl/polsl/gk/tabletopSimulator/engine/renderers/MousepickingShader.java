package pl.polsl.gk.tabletopSimulator.engine.renderers;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class MousepickingShader extends Shader {
    public MousepickingShader() {
        super("mousePickingShader");
    }

    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {
            super.createUniform("entityColor");
            super.createUniform("modelViewMatrix");
            super.createUniform("projectionMatrix");
    }

    @Override
    protected void bindAllUniforms() {

    }
    public void setColor(Vector3f color){
        this.loadVector("entityColor", color);
    }
    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix("projectionMatrix", projectionMatrix);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("modelViewMatrix", modelViewMatrix);
    }


}
