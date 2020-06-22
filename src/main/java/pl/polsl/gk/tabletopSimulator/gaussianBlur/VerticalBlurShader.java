package pl.polsl.gk.tabletopSimulator.gaussianBlur;

import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class VerticalBlurShader extends Shader {

	private static final String VERTICAL_BLUR_FILE = "verticalBlurShader";
	
	private int location_targetHeight;
	
	protected VerticalBlurShader() {
		super(VERTICAL_BLUR_FILE);
	}
	
	protected void loadTargetHeight(float height){
		super.loadFloat("targetHeight", height);
	}

	@Override
	protected void getAllUniformLocations() {	
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	@Override
	protected void bindAllUniforms() {
		super.createUniform("targetHeight", location_targetHeight);

	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
