#version 330

in vec2 outTextureCoords;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D textureSampler;

void main()
{
    fragColor = texture(textureSampler, outTextureCoords);
}