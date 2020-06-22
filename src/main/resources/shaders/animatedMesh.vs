#version 330 core
layout (location = 0) in vec3 vPos;
layout (location = 1) in vec3 vNormal;
layout (location = 1) in vec2 texCoords;

out vec2 textureCoords;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    textureCoords = texCoords;
    //vec4 mvPos = modelViewMatrix * vec4(vPos, 1.0);
    //gl_Position = projectionMatrix * mvPos;
    gl_Position = vec4(vPos, 1.0);
}
