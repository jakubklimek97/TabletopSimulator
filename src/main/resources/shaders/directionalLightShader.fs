#version 330

in vec2 vertexTextureCoordinates;
in vec3 vertexNormals;
in vec3 vertexPosition;
out vec4 fragColor;

//DIRECTIONAL LIGHT PARAMETERS
struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

//MATERIAL PARAMETERS
struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int checkTexture;
    float reflectanceFactor;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform Material material;
uniform DirectionalLight directionalLight;
uniform float specularPower;
vec4 ambientMaterialColour;
vec4 diffuseMaterialColour;
vec4 specularMaterialColour;
// SETUP MATERIAL COLOUR
void setupMaterialColours(Material material, vec2 textCoord)
{
    if (material.checkTexture == 0)
    {       // DEFAULT MATERIAL COLOUR (WITHOUT CUSTOM MATERIAL)
            ambientMaterialColour = material.ambient;
            diffuseMaterialColour = material.diffuse;
            specularMaterialColour = material.specular;
    }
    else
    {       // MATERIAL COLOUR CALCUALTION
            ambientMaterialColour = texture(texture_sampler, textCoord);
            diffuseMaterialColour = ambientMaterialColour;
            specularMaterialColour = ambientMaterialColour;
    }
}
// DIFFUSE AND SPECULAR COLOUR CALCULATION
vec4 calcLightColour(vec3 lightColour, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal)
{
    // FINAL RESULT OF CALCUALTION VARIABLES
    vec4 diffuseFinalColour;
    vec4 specularFinalColour;

    // DIFFUSE LIGHT CALCULATION
    float diffuseFactor = max(dot(normal, toLightDir), 0.0);
     diffuseFinalColour =  lightIntensity * diffuseFactor *  vec4(lightColour, 1.0) * diffuseMaterialColour;

    // SPECULAR LIGHT CALCULATION
    vec3 cameraDirection = normalize(-position);
    vec3 fromLightDir = -toLightDir;
    vec3 reflectedLight = normalize(reflect(fromLightDir , normal));
    float specularFactor = max( dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularFinalColour =  lightIntensity  * specularFactor * material.reflectanceFactor * vec4(lightColour, 1.0) * specularMaterialColour ;

    // FINAL CALCULATION OF SPECULAR AND DIFFUSE LIGHT
    vec4 diffuseAndSpecularResult = specularFinalColour + diffuseFinalColour;
    return diffuseAndSpecularResult;
}
// CALCULATE DIRECTIONA LIGHT BASED ON LIGHT COLOUR
vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 position, vec3 normal)
{
    return calcLightColour(directionalLight.colour, directionalLight.intensity, position, normalize(directionalLight.direction), normal);
}

void main()
{   // CHANGE ENTITY COLOUR BASED ON MATERIAL
    setupMaterialColours(material, vertexTextureCoordinates);
    // CALCULATE DIFFUSE  SPECULAR  FOR DIRECTIONAL  LIGHT
     vec4 diffuseSpecularComponent = calcDirectionalLight(directionalLight, vertexPosition, vertexNormals);
    // ADD AMBIENT LIGHT TO FINAL CALCULATION
    fragColor = diffuseSpecularComponent + ambientMaterialColour * vec4(ambientLight, 1) ;
}