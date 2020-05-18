#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 vertexNormals;

uniform mat4 modelLightViewMatrix;
uniform mat4 orthogonalProjectionMatrix;

void main()
{
   gl_Position = orthogonalProjectionMatrix * modelLightViewMatrix * vec4(position, 1.0);
}