#version 330

in vec2 vertexTextureCoordinates;

out vec4 Frag_Colour;

uniform sampler2D sunTexture;

void main(void){

    Frag_Colour = texture(sunTexture, vertexTextureCoordinates);

}