#version 330 core
layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec2 parameters; //mouseover and selected

flat out int mouseover;
flat out int selected;
out vec2 textureCords;

out VS_OUT {
    vec2 parameters;
    vec2 texture;
} vs_out;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    vs_out.parameters = parameters;
    vs_out.texture = texCoords;

    vec4 mvPos = modelViewMatrix * vec4(pos, 1.0);
    gl_Position = projectionMatrix * mvPos;
}
