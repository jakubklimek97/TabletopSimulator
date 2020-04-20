#version 330 core
layout(lines) in;
layout(triangle_strip, max_vertices = 4) out;
in VS_OUT {
    vec3 color;
} gs_in[];
out vec2 texCords;
out vec3 fontColor;
void main(){
        fontColor = gs_in[0].color;
        gl_Position = vec4(gl_in[0].gl_Position.x, gl_in[1].gl_Position.y, 0.0f, 1.0f);
        texCords = vec2(gl_in[0].gl_Position.z, gl_in[1].gl_Position.w);
        EmitVertex();
        gl_Position = vec4(gl_in[1].gl_Position.x, gl_in[1].gl_Position.y, 0.0f, 1.0f);
        texCords = vec2(gl_in[1].gl_Position.z, gl_in[1].gl_Position.w);
        EmitVertex();
        gl_Position = vec4(gl_in[0].gl_Position.x, gl_in[0].gl_Position.y, 0.0f, 1.0f);
        texCords = vec2(gl_in[0].gl_Position.z, gl_in[0].gl_Position.w);
        EmitVertex();
        gl_Position = vec4(gl_in[1].gl_Position.x, gl_in[0].gl_Position.y, 0.0f, 1.0f);
        texCords = vec2(gl_in[1].gl_Position.z, gl_in[0].gl_Position.w);
        EmitVertex();
        EndPrimitive();
}
