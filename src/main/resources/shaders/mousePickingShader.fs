#version 330

out vec4 color;

in vec3 pickingColor;

void main()
{
    color = vec4(pickingColor, 1.0f);
}
