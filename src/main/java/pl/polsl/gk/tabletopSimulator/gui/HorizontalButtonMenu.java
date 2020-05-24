package pl.polsl.gk.tabletopSimulator.gui;

import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.engine.managers.TextureManager;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33C.*;

public class HorizontalButtonMenu {

    public HorizontalButtonMenu(MenuSide side, float sizeInScreenSpace, float screenWidth, float screenHeight){
        int[] eboArray = {
                0,1,2,2,1,3
        };
        width = 0.0f;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.side = side;
        this.iconSize = sizeInScreenSpace;
        this.buttonList = new ArrayList<TexturedButton>();
            try(MemoryStack stack = MemoryStack.stackPush()){
                IntBuffer tmp = stack.callocInt(1);
                glGenVertexArrays(tmp);
                vao = tmp.get(0);
                glGenBuffers(tmp);
                vbo = tmp.get(0);
                glGenBuffers(tmp);
                ebo = tmp.get(0);
            }
        float[] vertices = {
                -0.5f, 0.5f,
                0.5f, 0.5f,
                -0.5f, -0.5f,
                0.5f, 0.5f
        };
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,eboArray, GL_STATIC_DRAW);
            glVertexAttribPointer(0,2, GL_FLOAT, false, 2*4, 0);
            glEnableVertexAttribArray(0);
            glBindVertexArray(0);
            this.menuShader = new MenuShader();
    }

    public void AddButton(TexturedButton btn){
        buttonList.add(btn);
        width += iconSize;
    }
    public void Prepare(){
        float glSpaceWidth, glSpaceHeight;
        float[] vertices = {
          -0.5f, 0.5f,
          0.5f, 0.5f,
          -0.5f, -0.5f,
          0.5f, 0.5f
        };
        switch(side){
            case BOTTOM:{
                glSpaceWidth = width/screenWidth;
                glSpaceHeight = iconSize/screenHeight;
                vertices[1] = vertices[3] = -1.0f+glSpaceHeight;
                vertices[5] = vertices[7] = -1.0f;
                vertices[2] = vertices[6] = glSpaceWidth /2.0f;
                vertices[0] = vertices[4] = -vertices[2];
                break;
            }
            default:{
                System.out.println("ERROR::MENU::Prepare: Side not yet implemented");
            }
        }
        glBindVertexArray(vao);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindVertexArray(0);

    }
    public void Render(){
        menuShader.use();
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        //sofar without buttons
    }
    int vao, vbo, ebo;
    float iconSize;
    float width;
    float screenWidth, screenHeight;
    ArrayList<TexturedButton> buttonList;
    MenuShader menuShader;
    MenuSide side;
}
