package pl.polsl.gk.tabletopSimulator.bloom;

import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class BrightFilterShader extends Shader {
	
	private static final String BRIGHT_FILE = "BrightFilter";

	public BrightFilterShader() {
		super(BRIGHT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAllUniforms() {

	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
