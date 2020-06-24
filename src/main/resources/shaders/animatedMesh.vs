#version 330 core
layout (location = 0) in vec3 vPos;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec3 vNormal;

layout (location = 3) in vec4 boneWeight;
layout (location = 4) in ivec4 boneId;



out vec2 textureCoords;


uniform mat4 bones[150];
uniform mat4 modelView;
uniform mat4 projection;
void main()
{
    textureCoords = texCoords;

    mat4 BoneTransform = bones[boneId[0]] * boneWeight[0];
    BoneTransform += bones[boneId[1]] * boneWeight[1];
    BoneTransform += bones[boneId[2]] * boneWeight[2];
    BoneTransform += bones[boneId[3]] * boneWeight[3];

    gl_Position = projection * modelView * BoneTransform * vec4(vPos, 1.0);
}
