package pl.polsl.gk.tabletopSimulator.postProcessing;

import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ContrastShader extends Shader {

    private static final String CONTRAST_FILE = "contrastShader";

    public ContrastShader() {
	super(CONTRAST_FILE);
    }

    @Override
    protected void getAllUniformLocations() {

    }

    @Override
    protected void bindAttributes() {
	super.bindAttribute(0, "position");
    }

    @Override
    protected void bindAllUniforms() {
    }
}

