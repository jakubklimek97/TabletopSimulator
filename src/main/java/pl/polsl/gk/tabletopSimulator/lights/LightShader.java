package pl.polsl.gk.tabletopSimulator.lights;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import pl.polsl.gk.tabletopSimulator.fog.FogRenderer;
import pl.polsl.gk.tabletopSimulator.utility.Shader;


public class LightShader extends Shader {

    private static final String FILE = "lightShader";
    private int textureSampler;
    private int projectionMatrix;
    private int modelViewMatrix;
    private int specularPower;
    private int ambientLight;


    public LightShader() {
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
        createDirectionalLightUniform("directionalLight");


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
        super.loadFloat(uniformName + ".attenuation.constant", attenuation.getConstant());
        super.loadFloat(uniformName + ".attenuation.linear", attenuation.getLinear());
        super.loadFloat(uniformName + ".attenuation.exponent", attenuation.getExponent());
    }

    public void loadMaterial(String uniformName, Material material) {
        super.loadVector(uniformName + ".ambient", material.getAmbientLight());
        super.loadVector(uniformName + ".diffuse", material.getDiffuseLight());
        super.loadVector(uniformName + ".specular", material.getSpecularLight());
        super.loadInt( uniformName +  ".checkTexture", material.CheckTexture() ? 1 : 0);
        super.loadFloat(uniformName + ".reflectanceFactor", material.getReflectFactor());
    }

    public void createDirectionalLightUniform(String uniformName)  {
        super.createUniform(uniformName + ".colour");
        super.createUniform(uniformName + ".direction");
        super.createUniform(uniformName + ".intensity");

    }

    public void loadDirectionalLight(String uniformName, DirectionalLight directionalLight) {
        super.loadVector(uniformName + ".colour", directionalLight.getColour());
        super.loadVector(uniformName + ".direction", directionalLight.getDirection());
        super.loadFloat(uniformName + ".intensity", directionalLight.getIntensity());
    }

    public void createPointLightUniform(String uniformName)  {
        super.createUniform(uniformName + ".colour");
        super.createUniform(uniformName + ".position");
        super.createUniform(uniformName + ".intensity");
        super.createUniform(uniformName + ".attenuation.constant");
        super.createUniform(uniformName + ".attenuation.linear");
        super.createUniform(uniformName + ".attenuation.exponent");
    }

    public void createMaterialUniform(String uniformName)  {
        super.createUniform(uniformName + ".ambient");
        super.createUniform(uniformName + ".diffuse");
        super.createUniform(uniformName + ".specular");
        super.createUniform(uniformName + ".checkTexture");
        super.createUniform(uniformName + ".reflectanceFactor");
    }

    public void createFogUniform(String uniformName){
        super.createUniform(uniformName + ".density");
        super.createUniform(uniformName + ".colour");
        super.createUniform(uniformName + ".active");

    }
    public void loadFog(String uniformName, FogRenderer fog){
        super.loadVector(uniformName + ".colour", fog.getColour());
        super.loadFloat(uniformName + ".density", fog.getDensityFactor());
        super.loadInt(uniformName + ".activeFog", fog.isActive() ? 1 : 0);


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

    public void loadFog(FogRenderer fog) {
        loadFog("fog", fog);
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
