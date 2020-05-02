package pl.polsl.gk.tabletopSimulator.lights;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.lights.Material;
import pl.polsl.gk.tabletopSimulator.lights.PointLight;
import pl.polsl.gk.tabletopSimulator.utility.Shader;


public class PointLightShader extends Shader {

    private static final String FILE = "secondShader";
    private int textureSampler;
    private int projectionMatrix;
    private int modelViewMatrix;
    private int specularPower;
    private int ambientLight;


    public PointLightShader() {
        super(FILE);

    }

    public void bindAllUniforms() {
        super.createUniform("projectionMatrix", projectionMatrix);
        super.createUniform("modelViewMatrix", modelViewMatrix);
        super.createUniform("texture_sampler", textureSampler);
        createMaterialUniform("material");
        super.createUniform("specularPower", specularPower);
        super.createUniform("ambientLight", ambientLight);
        createPointLightUniform("pointLight");

    }

    public void getAllUniformLocations() {
        textureSampler = super.getUniformLocation("texture_sampler");
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        modelViewMatrix = super.getUniformLocation("modelViewMatrix");
        specularPower = super.getUniformLocation("specularPower");
        ambientLight = super.getUniformLocation("ambientLight");


    }

    public void loadLight(String uniformName, PointLight pointLight) {
        super.loadVector(uniformName + ".colour", pointLight.getColor());
        super.loadVector(uniformName + ".position", pointLight.getPosition());
        super.loadFloat(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation attenuation = pointLight.getAttenuation();
        super.loadFloat(uniformName + ".att.constant", attenuation.getConstant());
        super.loadFloat(uniformName + ".att.linear", attenuation.getLinear());
        super.loadFloat(uniformName + ".att.exponent", attenuation.getExponent());
    }

    public void loadMaterial(String uniformName, Material material) {
        super.loadVector(uniformName + ".ambient", material.getAmbientLight());
        super.loadVector(uniformName + ".diffuse", material.getDiffuseLight());
        super.loadVector(uniformName + ".specular", material.getSpecularLight());
        super.loadInt( uniformName +  ".hasTexture", material.CheckTexture() ? 1 : 0);
        super.loadFloat(uniformName + ".reflectance", material.getReflectFactor());
    }


    public void createPointLightUniform(String uniformName)  {
        super.createUniform(uniformName + ".colour");
        super.createUniform(uniformName + ".position");
        super.createUniform(uniformName + ".intensity");
        super.createUniform(uniformName + ".att.constant");
        super.createUniform(uniformName + ".att.linear");
        super.createUniform(uniformName + ".att.exponent");
    }

    public void createMaterialUniform(String uniformName)  {
        super.createUniform(uniformName + ".ambient");
        super.createUniform(uniformName + ".diffuse");
        super.createUniform(uniformName + ".specular");
        super.createUniform(uniformName + ".hasTexture");
        super.createUniform(uniformName + ".reflectance");
    }

    public void loadMaterial(Material material){
        loadMaterial("material",material);
    }

    public void loadSpecularPower(float specularPower){
        super.loadFloat("specularPower", specularPower);
    }

    public void loadAmbientLight(Vector3f ambientLight){
        super.loadVector("ambientLight", ambientLight);
    }

    public void loadPointLight(PointLight pointLight){
        loadLight("pointLight",pointLight);
    }

    public void bindAttributes() {
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix("projectionMatrix", projectionMatrix);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix("modelViewMatrix", modelViewMatrix);
    }

    public void loadTextureSampler(int textureSampler) {
        super.loadInt("texture_sampler", textureSampler);
    }


}
