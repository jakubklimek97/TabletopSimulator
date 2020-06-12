#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;


out vec2 outTextureCoords;


uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

uniform float textureXOffset;
uniform float textureYOffset;
uniform int numCols;
uniform int numRows;


void main()
 {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);

    //Support for texture atlas, update texture coordinates
    float x = (texCoord.x / numCols + textureXOffset);
    float y = (texCoord.y / numRows + textureYOffset);

    outTextureCoords = vec2(x,y);


 }