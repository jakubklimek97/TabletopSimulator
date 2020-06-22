package pl.polsl.gk.tabletopSimulator.bloom;

import pl.polsl.gk.tabletopSimulator.utility.Shader;


public class CombineShader extends Shader {

	private static final String COMBINE_FILE = "CombineShader";

	private int location_colourTexture;
	private int location_highlightTexture;
	
	protected CombineShader() {
		super(COMBINE_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_highlightTexture = super.getUniformLocation("highlightTexture");
	}

	@Override
	protected void bindAllUniforms() {
		super.createUniform("colourTexture", location_colourTexture);
		super.createUniform("highlightTexture", location_highlightTexture);
	}

	protected void connectTextureUnits() {
		super.loadInt("colourTexture", location_colourTexture);
		super.loadInt("highlightTexture", location_highlightTexture);
	}


	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
