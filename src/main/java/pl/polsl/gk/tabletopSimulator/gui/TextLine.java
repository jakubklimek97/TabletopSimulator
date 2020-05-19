package pl.polsl.gk.tabletopSimulator.gui;

import org.joml.Vector3f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

import static org.lwjgl.opengl.GL33C.*;

import java.nio.IntBuffer;

public class TextLine {
    public TextLine(Font font, int maxLineLength){
        this.font = font;
        this.textQuadArray = new float[8*maxLineLength];
        this.characters = new char[maxLineLength];
        this.calledCharPositionArray = new float[2];
        this.charPositionArray = new float[2*maxLineLength];
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer buf = stack.callocInt(1);
            glGenVertexArrays(buf);
            vao = buf.get(0);
            glGenBuffers(buf);
            vbo = buf.get(0);
        }
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 4, GL_FLOAT,false, 4*4, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
    }
    public void SetScreenResolution(int width, int height){
        int oldWidth = this.width;
        int oldHeight = this.height;
        this.width = width;
        this.height = height;
        this.SetPosition(charPositionArray[0]/oldWidth*width, charPositionArray[1]/oldHeight*height);
    }
    public void SetPosition(float x, float y){
        charPositionArray[0] = x;
        charPositionArray[1] = y;
        int charCount = characterCount;
        characterCount = 0;
        for(int i = 0; i < charCount; ++i){
            AddCharacter(characters[i]);
        }
    }
    public void Render(float r, float g, float b){
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Shader shader = this.font.getShader();
        shader.Use();
        shader.setUniform("textColor", new Vector3f(r,g,b));
        glBindVertexArray(vao);
        glBindTexture(GL_TEXTURE_2D, this.font.getBitmapTexture());
        glDrawArrays(GL_LINES, 0, characterCount*2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D,0);
    }
    public void AddCharacter(char character){
        characters[characterCount] = character;
        calledCharPositionArray[0] = charPositionArray[characterCount*2];
        calledCharPositionArray[1] = charPositionArray[characterCount*2+1];
        STBTTAlignedQuad characterQuad = this.font.getCharacterQuad(character - 32, calledCharPositionArray);
        int quadPos = 8*characterCount;
        this.textQuadArray[quadPos] = (characterQuad.x0()/width)*2-1.0f;
        this.textQuadArray[quadPos+1] = -((characterQuad.y0()/height)*2-1.0f);
        this.textQuadArray[quadPos+2] = characterQuad.s0();
        this.textQuadArray[quadPos+3] = characterQuad.t0();
        this.textQuadArray[quadPos+4] = (characterQuad.x1()/width)*2-1.0f;
        this.textQuadArray[quadPos+5] = -((characterQuad.y1()/height)*2-1.0f);
        this.textQuadArray[quadPos+6] = characterQuad.s1();
        this.textQuadArray[quadPos+7] = characterQuad.t1();
        characterCount++;
        charPositionArray[characterCount*2] = calledCharPositionArray[0];
        charPositionArray[characterCount*2+1] = calledCharPositionArray[1];
        glBindVertexArray(vao);
        glBufferData(GL_ARRAY_BUFFER, textQuadArray, GL_DYNAMIC_DRAW);
        glBindVertexArray(0);
        characterQuad.free();
    }
    public void DeleteCharacter(){
        characterCount--;
    }
    public void SetText(String text){
        characterCount = 0;
        for(char ch : text.toCharArray()){
            AddCharacter(ch);
        }
    }
    private Font font;
    private int characterCount = 0;
    private char[] characters;
    private float[] textQuadArray;
    private float[] charPositionArray;
    private float[] calledCharPositionArray;
    private int width, height; //screen resolution
    //normal opengl stuff
    private int vao;
    private int vbo;
}
