#version 410

layout (location = 0) out vec4 color;

in vec2 TexCoord;
in vec3 normals;

uniform sampler2D texture_diffuse1;
uniform sampler2D texture_specular1;
uniform sampler2D texture_normal1;

void main(){
	color = texture(texture_diffuse1, TexCoord);
}