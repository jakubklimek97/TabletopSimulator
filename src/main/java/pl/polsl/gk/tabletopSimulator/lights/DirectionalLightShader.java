package pl.polsl.gk.tabletopSimulator.lights;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import pl.polsl.gk.tabletopSimulator.utility.Shader;

public class DirectionalLightShader extends Shader {

    private static final String FILE = "directionalLightShader";
    private int textureSampler;
    private int projectionMatrix;
    private int modelViewMatrix;
    private int specularPower;
    private int ambientLight;


    public DirectionalLightShader() {
        super(FILE);

    }

    public void bindAllUniforms() {
        super.createUniform("projectionMatrix", projectionMatrix);
        super.createUniform("modelViewMatrix", modelViewMatrix);
        super.createUniform("texture_sampler", textureSampler);
        createMaterialUniform("material");
        super.createUniform("ambientLight", ambientLight);
        super.createUniform("specularPower", specularPower);
        createPointLightUniform("directionalLight");

    }

    public void getAllUniformLocations() {
        textureSampler = super.getUniformLocation("texture_sampler");
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        modelViewMatrix = super.getUniformLocation("modelViewMatrix");
        ambientLight = super.getUniformLocation("ambientLight");
        specularPower = super.getUniformLocation("specularPower");

    }

    public void loadDirectionalLight(String uniformName, DirectionalLight directionalLight) {
        super.loadVector(uniformName + ".colour", directionalLight.getColour());
        super.loadVector(uniformName + ".direction", directionalLight.getDirection());
        super.loadFloat(uniformName + ".intensity", directionalLight.getIntensity());
    }

    public void loadMaterial(String uniformName, Material material) {
        super.loadVector(uniformName + ".ambient", material.getAmbientLight());
        super.loadVector(uniformName + ".diffuse", material.getDiffuseLight());
        super.loadVector(uniformName + ".specular", material.getSpecularLight());
        super.loadInt( uniformName +  ".checkTexture", material.CheckTexture() ? 1 : 0);
        super.loadFloat(uniformName + ".reflectanceFactor", material.getReflectFactor());
    }


    public void createPointLightUniform(String uniformName)  {
        super.createUniform(uniformName + ".colour");
        super.createUniform(uniformName + ".direction");
        super.createUniform(uniformName + ".intensity");

    }

    public void createMaterialUniform(String uniformName)  {
        super.createUniform(uniformName + ".ambient");
        super.createUniform(uniformName + ".diffuse");
        super.createUniform(uniformName + ".specular");
        super.createUniform(uniformName + ".checkTexture");
        super.createUniform(uniformName + ".reflectanceFactor");
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

    public void loadDirectionalLight(DirectionalLight directionalLight){
        loadDirectionalLight("directionalLight",directionalLight);
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


