package pl.polsl.gk.tabletopSimulator.utility;
import org.lwjgl.system.MemoryStack;
import pl.polsl.gk.tabletopSimulator.Math.Matrix.Matrix4f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector2f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector4f;

import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Shader {

    private final Map<String, Integer> uniforms;

    private int shaderId;

    private boolean isGood;

    public Shader(String name){
        uniforms = new HashMap<>();
        this.isGood = false;
        String vertexCode = new String();
        String fragmentCode = new String();
        InputStream vertexUrl = getClass().getClassLoader().getResourceAsStream("shaders/"+name+".vs");
        InputStream fragmentUrl = getClass().getClassLoader().getResourceAsStream("shaders/"+name+".fs");
        if(!(vertexUrl != null && fragmentUrl != null)){
            System.err.println("ERROR::Couldn't load shader " + name);
        }
        try {
            vertexCode = ParseFileToString(vertexUrl);
            fragmentCode = ParseFileToString(fragmentUrl);
        } catch (IOException e) {
            //TODO
            // Exception when can't parse files
            e.printStackTrace();
        }
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertexShader, vertexCode);
        glShaderSource(fragmentShader, fragmentCode);
        glCompileShader(vertexShader);
        glCompileShader(fragmentShader);
        int vertexCompileStatus, fragmentCompileStatus;
        try(MemoryStack stack = stackPush()){
            IntBuffer ip = stack.callocInt(1);
            glGetShaderiv(vertexShader, GL_COMPILE_STATUS, ip);
            vertexCompileStatus = ip.get(0);
            glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, ip);
            fragmentCompileStatus = ip.get(0);
        }
        if(fragmentCompileStatus != 1){
            System.out.println("ERROR::FRAGMENTSHADER::"+name);
            System.out.println(glGetShaderInfoLog(fragmentShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        }
        else if(vertexCompileStatus != 1){
            System.out.println("ERROR::VERTEXSHADER::"+name);
            System.out.println(glGetShaderInfoLog(fragmentShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        }
        else{
            shaderId = glCreateProgram();
            glAttachShader(shaderId, vertexShader);
            glAttachShader(shaderId, fragmentShader);
            glLinkProgram(shaderId);
            int shaderCompileStatus;
            try(MemoryStack stack = stackPush()){
                IntBuffer ip = stack.callocInt(1);
                glGetProgramiv(shaderId, GL_LINK_STATUS, ip);
                shaderCompileStatus = ip.get(0);
            }
            if(shaderCompileStatus != 1){
                System.out.println("ERROR::SHADER_LINK::"+name);
                System.out.println(glGetProgramInfoLog(shaderId));
                glDeleteShader(vertexShader);
                glDeleteShader(fragmentShader);
            }
            else{
                this.isGood = true;
            }
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        }

    }
    private String ParseFileToString(InputStream file) throws IOException {
        StringBuilder strB = new StringBuilder();
        try(      BufferedReader br = new BufferedReader(new InputStreamReader(file));){
            String tmp;
            while((tmp = br.readLine()) != null) {
                strB.append(tmp).append("\n");
            }
        }
        return strB.toString();
    }
    public boolean Use(){
        if(!isGood){
            return false;
        }
        glUseProgram(shaderId);
        return true;
    }

    public boolean isGood() {
        return isGood;
    }

    public void createUniform(String uniform) {
        int uniformLocation = glGetUniformLocation(shaderId, uniform);
        if(uniformLocation < 0) {

            System.out.println("ERROR::UNIFORM::"+ uniform);
        }
        uniforms.put(uniform,uniformLocation);
    }

    public void setUniform(String uniform, int value){
        glUniform1i(uniforms.get(uniform), value);
    }

    public void setUniform(String uniform, Matrix4f value){
        // Send matrix into a float buffer
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer buffer = stack.mallocFloat(16);
            value.toBuffer(buffer);
            glUniformMatrix4fv(uniforms.get(uniform), false, buffer);
        }
    }

    public void setUniform(String uniform, float x)
    {
        glUniform1f(uniforms.get(uniform), x);
    }

    public void setUniform(String uniform, boolean x)
    {
        glUniform1i(uniforms.get(uniform), x ? 1 : 0);
    }

    public void setUniform(String uniform, Vector2f vec)
    {
        glUniform2f(uniforms.get(uniform), vec.x, vec.y);
    }

    public void setUniform(String uniform, Vector3f vec)
    {
        glUniform3f(uniforms.get(uniform), vec.x, vec.y, vec.z);
    }

    public void setUniform(String uniform, Vector4f vec)
    {
        glUniform4f(uniforms.get(uniform), vec.x, vec.y, vec.z, vec.w);
    }


    public void unbind(){
        glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(shaderId != 0){
            glDeleteProgram(shaderId);
        }
    }

}
