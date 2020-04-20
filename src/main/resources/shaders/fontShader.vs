#version 330 core
layout (location = 0) in vec4 quad;
uniform vec3 textColor;
out VS_OUT {
    vec3 color;
} vs_out;
void main()
{
    vs_out.color = textColor;
    gl_Position = quad;
}
