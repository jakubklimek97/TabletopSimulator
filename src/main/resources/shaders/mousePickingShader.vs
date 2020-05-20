#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 textureCoordinates;
layout (location=2) in vec3 normals;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 entityColor;

out vec3 pickingColor;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    pickingColor = entityColor;
}
