#version 330 core
layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;
in VS_OUT {
    vec2 parameters;
    vec2 texture;
} gs_in[];

flat out int mouseover;
flat out int selected;
out vec2 textureCords;

void main(){
        mouseover = gs_in[0].parameters.x > 0 ? 1 : 0;
        selected = gs_in[0].parameters.y > 0 ? 1 : 0;
        gl_Position = gl_in[0].gl_Position;
        textureCords = gs_in[0].texture;
        EmitVertex();
        gl_Position = gl_in[1].gl_Position;
        textureCords = gs_in[1].texture;
        EmitVertex();
        gl_Position = gl_in[2].gl_Position;
        textureCords = gs_in[2].texture;
        EmitVertex();
        EndPrimitive();
}
