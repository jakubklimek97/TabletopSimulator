#version 330 core
layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec2 parameters; //mouseover and selected


uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
out vec3 position;
void main()
{
    position = pos;
    vec4 mvPos = modelViewMatrix * vec4(pos, 1.0);
    gl_Position = projectionMatrix * mvPos;
}
