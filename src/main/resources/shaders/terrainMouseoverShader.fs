#version 330 core
in vec3 position;
out vec4 color;
void main()
{
    color = vec4(0.0, position.x /25.0, position.z/25.0, 1.0);
}
