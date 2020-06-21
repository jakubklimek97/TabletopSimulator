package pl.polsl.gk.tabletopSimulator.postProcessing;

import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ContrastShader extends Shader {

    private static final String VERTEX_FILE = "/postProcessing/contrastVertex.txt";
    private static final String FRAGMENT_FILE = "/postProcessing/contrastFragment.txt";
	
    public ContrastShader() {
	super(VERTEX_FILE, FRAGMENT_FILE);
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

