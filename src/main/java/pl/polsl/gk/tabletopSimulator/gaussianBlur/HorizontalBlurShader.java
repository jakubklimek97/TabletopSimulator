package pl.polsl.gk.tabletopSimulator.gaussianBlur;

import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class HorizontalBlurShader extends Shader {

	private static final String HORIZONTAL_BLUR_FILE = "HorizontalBlurShader";
	
	private int location_targetWidth;
	
	protected HorizontalBlurShader() {
		super(HORIZONTAL_BLUR_FILE);
	}

	protected void loadTargetWidth(float width){
		super.loadFloat("targetWidth", width);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_targetWidth = super.getUniformLocation("targetWidth");
	}

	@Override
	protected void bindAllUniforms() {

		super.createUniform("targetWidth", location_targetWidth);


	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
