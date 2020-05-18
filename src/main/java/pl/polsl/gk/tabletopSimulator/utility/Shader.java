package pl.polsl.gk.tabletopSimulator.utility;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector2f;

import java.io.*;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public abstract class Shader {

    private final Map<String, Integer> uniforms;

    private int shaderId;

    private boolean isGood;

    public Shader(String name) {
        uniforms = new HashMap<>();
        this.isGood = false;
        String vertexCode = new String();
        String fragmentCode = new String();
        InputStream vertexUrl = getClass().getClassLoader().getResourceAsStream("shaders/" + name + ".vs");
        InputStream fragmentUrl = getClass().getClassLoader().getResourceAsStream("shaders/" + name + ".fs");
        if (!(vertexUrl != null && fragmentUrl != null)) {
            System.err.println("ERROR::Couldn't load shader " + name + "::CLASS::"+ this.getClass().getSimpleName());
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
        try (MemoryStack stack = stackPush()) {
            IntBuffer ip = stack.callocInt(1);
            glGetShaderiv(vertexShader, GL_COMPILE_STATUS, ip);
            vertexCompileStatus = ip.get(0);
            glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, ip);
            fragmentCompileStatus = ip.get(0);
        }
        if (fragmentCompileStatus != 1) {
            System.out.println("ERROR::FRAGMENTSHADER::" + name + "::CLASS::"+ this.getClass().getSimpleName());
            System.out.println(glGetShaderInfoLog(fragmentShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        } else if (vertexCompileStatus != 1) {
            System.out.println("ERROR::VERTEXSHADER::" + name + "::CLASS::"+ this.getClass().getSimpleName());
            System.out.println(glGetShaderInfoLog(fragmentShader));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        } else {
            shaderId = glCreateProgram();
            glAttachShader(shaderId, vertexShader);
            glAttachShader(shaderId, fragmentShader);
            bindAttributes();
            glLinkProgram(shaderId);
            int shaderCompileStatus;
            try (MemoryStack stack = stackPush()) {
                IntBuffer ip = stack.callocInt(1);
                glGetProgramiv(shaderId, GL_LINK_STATUS, ip);
                shaderCompileStatus = ip.get(0);
            }
            if (shaderCompileStatus != 1) {
                System.out.println("ERROR::SHADER_LINK::" + name + "::CLASS::"+ this.getClass().getSimpleName());
                System.out.println(glGetProgramInfoLog(shaderId));
                glDeleteShader(vertexShader);
                glDeleteShader(fragmentShader);
            } else {
                this.isGood = true;
                getAllUniformLocations();
                bindAllUniforms();

            }
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
        }

    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    protected abstract void bindAllUniforms();

    private String ParseFileToString(InputStream file) throws IOException {
        StringBuilder strB = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file));) {
            String tmp;
            while ((tmp = br.readLine()) != null) {
                strB.append(tmp).append("\n");
            }
        }
        return strB.toString();
    }

    public boolean use() {
        if (!isGood) {
            return false;
        }
        glUseProgram(shaderId);
        return true;
    }

    public boolean isGood() {
        return isGood;
    }

    public void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(shaderId, attribute, variableName);
    }

    public void createUniform(String uniform, int uniformLocation) {
        if (uniformLocation < 0) {

            System.out.println("ERROR::UNIFORM::" + uniform +"::CLASS::"+this.getClass().getSimpleName());
            return;
        }
        uniforms.put(uniform, uniformLocation);
    }

    public void createUniform(String uniformName)  {
        int uniformLocation = glGetUniformLocation(shaderId, uniformName);
        if (uniformLocation < 0) {
            System.out.println("ERROR::UNIFORM::" + uniformName +"::CLASS::"+this.getClass().getSimpleName());
            return;
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void loadMatrix(String uniform, Matrix4f value) {
        // Send matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniform), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void loadInt(String uniform, int value) {
        glUniform1i(uniforms.get(uniform), value);
    }

    public void loadFloat(String uniform, float value) {
        glUniform1f(uniforms.get(uniform), value);
    }

    public void loadBoolean(String uniform, boolean value) {
        glUniform1i(uniforms.get(uniform), value ? 1 : 0);
    }

    public void loadVector(String uniform, Vector2f vec) {
        glUniform2f(uniforms.get(uniform), vec.x, vec.y);
    }

    public void loadVector(String uniform, Vector3f vec) {
        glUniform3f(uniforms.get(uniform), vec.x, vec.y, vec.z);
    }

    public void loadVector(String uniform, Vector4f vec) {
        glUniform4f(uniforms.get(uniform), vec.x, vec.y, vec.z, vec.w);
    }

    public int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(shaderId, uniformName);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (shaderId != 0) {
            glDeleteProgram(shaderId);
        }
    }


}
