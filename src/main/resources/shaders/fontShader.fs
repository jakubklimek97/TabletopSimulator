#version 330 core


uniform sampler2D texture1;
in vec3 fontColor;
in vec2 texCords;
out vec4 color;
void main()
{
    color = vec4(fontColor, texture(texture1, texCords).a);
}
