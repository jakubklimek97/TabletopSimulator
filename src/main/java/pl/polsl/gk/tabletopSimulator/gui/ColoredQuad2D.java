package pl.polsl.gk.tabletopSimulator.gui;

import org.joml.Vector4f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class ColoredQuad2D extends GuiElement{
    public ColoredQuad2D(float posX, float posY, float width, float height, Vector4f color){
        super(posX, posY, width, height);
        this.color = color;
    }
    public void AssignColorId(float r, float g, float b){

    }
    public float[] GetColorId(){
        return new float[4];
    }
    public void Render(Shader shader){

    }
    float[] vertices;
    int vao;
    int vbo;
    float posX, posY, width, height;
    Vector4f color;

}
