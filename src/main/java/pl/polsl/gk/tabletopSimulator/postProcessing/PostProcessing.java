package pl.polsl.gk.tabletopSimulator.postProcessing;


import org.dom4j.rule.Mode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


import pl.polsl.gk.tabletopSimulator.bloom.BrightFilter;
import pl.polsl.gk.tabletopSimulator.bloom.CombineFilter;
import pl.polsl.gk.tabletopSimulator.entities.Loader;
import pl.polsl.gk.tabletopSimulator.entities.Mesh;
import pl.polsl.gk.tabletopSimulator.gaussianBlur.HorizontalBlur;
import pl.polsl.gk.tabletopSimulator.gaussianBlur.VerticalBlur;
import pl.polsl.gk.tabletopSimulator.models.ModelInfo;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static ModelInfo quad;
	private static ContrastChanger contrastChanger;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;


	public static void init(Mesh mesh){
			quad = mesh.loadVAO(POSITIONS, 2);
			contrastChanger = new ContrastChanger();

		//hBlur = new HorizontalBlur(1280/5, 720/5);
		//vBlur = new VerticalBlur(1280/5, 720/5);
	//	brightFilter = new BrightFilter(1280 / 2, 720 / 2);
	//	combineFilter = new CombineFilter();
	}

	public static void doPostProcessing(int colourTexture){
		start();
		//brightFilter.render(colourTexture);
		//hBlur.render(brightFilter.getOutputTexture());
		//vBlur.render(hBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		//combineFilter.render(colourTexture, vBlur.getOutputTexture());
		end();
	}

	public static void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		combineFilter.cleanUp();
	}

	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}