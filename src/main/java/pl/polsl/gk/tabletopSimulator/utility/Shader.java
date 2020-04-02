package pl.polsl.gk.tabletopSimulator.utility;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Shader {
    public Shader(String name){
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
    private int shaderId;
    private boolean isGood;

    public boolean isGood() {
        return isGood;
    }
}
