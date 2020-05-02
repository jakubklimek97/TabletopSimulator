#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 textureCoordinates;
layout (location=2) in vec3 normals;

out vec2 vertexTextureCoordinates;
out vec3 vertexNormals;
out vec3 vertexPosition;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    vertexPosition = mvPos.xyz;
    vertexTextureCoordinates = textureCoordinates;
    vertexNormals = normalize(modelViewMatrix * vec4(normals, 0.0)).xyz;
     gl_Position = projectionMatrix * mvPos;
}