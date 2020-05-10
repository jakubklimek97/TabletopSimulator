#version 330

in vec2 vertexTextureCoordinates;
in vec3 vertexNormals;
in vec3 vertexPosition;
in mat4 vertexModelViewMatrix;
in vec4 vertexLightViewPos;

out vec4 fragColor;
//ATTENUATION PARAMETERS
struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};
//POINTLIGHT PARAMETERS
struct PointLight
{
    vec3 colour;
    vec3 position;
    float intensity;
    Attenuation attenuation;
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

//DIRECTIONAL LIGHT PARAMETERS
struct DirectionalLight
{
    vec3 colour;
    vec3 direction;
    float intensity;
};

uniform sampler2D texture_sampler;
uniform sampler2D shadowMap;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;

vec4 ambientMaterialColour;
vec4 diffuseMaterialColour;
vec4 specularMaterialColour;
// SETUP MATERIAL COLOUR
void setupMaterialColours(Material material, vec2 textCoord)
{
    if (material.checkTexture == 1)
    {

                 // MATERIAL COLOUR CALCUALTION
                 ambientMaterialColour = texture(texture_sampler, textCoord);
                 diffuseMaterialColour = ambientMaterialColour;
                 specularMaterialColour = ambientMaterialColour;
     }
    else
    {
                // DEFAULT MATERIAL COLOUR (WITHOUT CUSTOM MATERIAL)
                ambientMaterialColour = material.ambient;
                diffuseMaterialColour = material.diffuse;
                specularMaterialColour = material.specular;
     }

}
// DIFFUSE AND SPECULAR COLOUR CALCULATION
vec4 calcLightColour(vec3 lightColour, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal)
{
    // FINAL RESULT OF CALCUALTION VARIABLES
    vec4 diffuseFinalColour  = vec4(0.0,0.0,0.0,0.0);
    vec4 specularFinalColour = vec4(0.0,0.0,0.0,0.0);

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
// ATTENUATION AND DIRECTION CALCULATION
vec4 calculationPointLight(PointLight pointlight, vec3 position, vec3 normal)
{   // CALCULATE LIGHT DIRECTION AND LIGHT COLOUR
    vec3 lightDirection = pointlight.position - position;
    vec3 toLightDirection  = normalize(lightDirection);
    vec4 lightColour = calcLightColour(pointlight.colour, pointlight.intensity, position, toLightDirection, normal);

    // CALCULATE LIGHT ATTENUATION FACTOR
    float distance = length(lightDirection);
    float attenuationConstant = pointlight.attenuation.constant;
    float attenuationLinear   = pointlight.attenuation.linear * distance;
    float attenuationExponent = pointlight.attenuation.exponent * distance * distance;
    // FINAL ATTENUATION BASED ON LIGHT COLOUR
    float attenuationFinalFactor = attenuationConstant + attenuationLinear + attenuationExponent;
    vec4 attenuationResult = lightColour / attenuationFinalFactor;
    return attenuationResult;
}




// CALCULATE DIRECTIONA LIGHT BASED ON LIGHT COLOUR
vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 position, vec3 normal)
{
    return calcLightColour(directionalLight.colour, directionalLight.intensity, position, normalize(directionalLight.direction), normal);
}


float calculateShadows(vec4 position)
{
    vec3 projectionCoords = position.xyz;

    projectionCoords = projectionCoords * 0.5 + 0.5;
    float bias = 0.21;

    float shadowFactor = 0.6;


    vec2 increment = 1.0 / textureSize(shadowMap, 0 );
    for( int row = -1; row <= 1; ++row)
    {

        for(int col = -1; col <= 1; ++col)
        {

            float textDepth = texture(shadowMap, projectionCoords.xy + vec2(row, col) * increment).r;
            shadowFactor += projectionCoords.z - bias > textDepth ? 1.0 : 0.0;


        }

    }
    shadowFactor /= 9.0;

    if(projectionCoords.z > 1.0)
    {

        shadowFactor = 1.0;
    }

    return 1 - shadowFactor;


}

void main()
{   // CHANGE ENTITY COLOUR BASED ON MATERIAL
    setupMaterialColours(material, vertexTextureCoordinates);
    // CALCULATE DIFFUSE  SPECULAR  FOR DIRECTIONAL  LIGHT
     vec4 diffuseSpecularComponent = calcDirectionalLight(directionalLight, vertexPosition, vertexNormals);
    // CALCULATE DIFFUSE  SPECULAR AND ATTENUATION FOR POINT LIGHT

    if(pointLight.intensity > 1f)
    {
     diffuseSpecularComponent += calculationPointLight(pointLight, vertexPosition, vertexNormals);
    }
    // ADD AMBIENT LIGHT TO FINAL CALCULATION
    //fragColor = diffuseSpecularComponent + ambientMaterialColour * vec4(ambientLight, 1) ;

    float shadow = calculateShadows(vertexLightViewPos);
    fragColor = clamp(ambientMaterialColour * vec4(ambientLight,1) + diffuseSpecularComponent *  shadow,0,1);


}