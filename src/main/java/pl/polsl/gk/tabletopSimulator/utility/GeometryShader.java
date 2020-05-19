package pl.polsl.gk.tabletopSimulator.utility;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.opengl.GL20C.glDeleteShader;
import static org.lwjgl.system.MemoryStack.stackPush;

public class GeometryShader extends Shader {

    public GeometryShader(String name){
        super(name);
        uniforms = new HashMap<>();
        this.isGood = false;
        String vertexCode = "";
        String fragmentCode = "";
        String geometryCode = "";
        InputStream vertexUrl = getClass().getClassLoader().getResourceAsStream("shaders/"+name+".vs");
        InputStream fragmentUrl = getClass().getClassLoader().getResourceAsStream("shaders/"+name+".fs");
        InputStream geometryUrl = getClass().getClassLoader().getResourceAsStream("shaders/"+name+".gs");
        if(!(vertexUrl != null && fragmentUrl != null)){
            System.err.println("ERROR::Couldn't load shader " + name);
        }
        try {
            vertexCode = ParseFileToString(vertexUrl);
            fragmentCode = ParseFileToString(fragmentUrl);
            geometryCode = ParseFileToString(geometryUrl);
        } catch (IOException e) {
            //TODO
            // Exception when can't parse files
            e.printStackTrace();
        }
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        int geometryShader = glCreateShader(GL_GEOMETRY_SHADER);
        glShaderSource(vertexShader, vertexCode);
        glShaderSource(fragmentShader, fragmentCode);
        glShaderSource(geometryShader, geometryCode);
        glCompileShader(vertexShader);
        glCompileShader(fragmentShader);
        glCompileShader(geometryShader);
        int vertexCompileStatus, fragmentCompileStatus, geometryCompileStatus;
        try(MemoryStack stack = stackPush()){
            IntBuffer ip = stack.callocInt(1);
            glGetShaderiv(vertexShader, GL_COMPILE_STATUS, ip);
            vertexCompileStatus = ip.get(0);
            glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, ip);
            fragmentCompileStatus = ip.get(0);
            glGetShaderiv(geometryShader, GL_COMPILE_STATUS, ip);
            geometryCompileStatus = ip.get(0);
        }
        if(fragmentCompileStatus != 1){
            System.out.println("ERROR::FRAGMENTSHADER::"+name);
            System.out.println(glGetShaderInfoLog(fragmentShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            glDeleteShader(geometryShader);
        }
        else if(vertexCompileStatus != 1){
            System.out.println("ERROR::VERTEXSHADER::"+name);
            System.out.println(glGetShaderInfoLog(fragmentShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            glDeleteShader(geometryShader);
        }
        else if(geometryCompileStatus != 1){
            System.out.println("ERROR::GEOMETRYSHADER::"+name);
            System.out.println(glGetShaderInfoLog(geometryShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            glDeleteShader(geometryShader);
        }
        else{
            shaderId = glCreateProgram();
            glAttachShader(shaderId, vertexShader);
            glAttachShader(shaderId, fragmentShader);
            glAttachShader(shaderId, geometryShader);
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
            glDeleteShader(geometryShader);
        }

    }

    @Override
    protected void bindAttributes() {

    }

    @Override
    protected void getAllUniformLocations() {

    }

    @Override
    protected void bindAllUniforms() {

    }
}
