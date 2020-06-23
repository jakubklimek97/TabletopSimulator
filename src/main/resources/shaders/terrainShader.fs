#version 330 core


uniform sampler2D texture1;
flat in int mouseover;
flat in int selected;
in vec2 textureCords;
out vec4 color;
void main()
{
    if(mouseover == 1)
    {
        color = vec4(1.0f, 1.0f, 0.0f, 1.0f);
    }
    else
    {
        if(selected == 1)
        {
            color = vec4(1.0f, 0.0f, 1.0f, 1.0f);
        }
        else
        {
            //color = vec4(0.0f, 0.0f, 1.0f, 1.0f);
            color = vec4(texture(texture1, textureCords).xyz, 1.0f);
        }
    }
}
