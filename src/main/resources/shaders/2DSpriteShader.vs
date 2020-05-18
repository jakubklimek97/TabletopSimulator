#version 330

in vec2 textureCoordinates;

out vec2 vertexTextureCoordinates;

uniform mat4 modelViewMatrix;

void main(void){

	vertexTextureCoordinates = textureCoordinates + vec2(0.5, 0.5);
	vertexTextureCoordinates.y = 1.0 - vertexTextureCoordinates.y;
	gl_Position = modelViewMatrix * vec4(textureCoordinates, 0.0, 1.0);

}