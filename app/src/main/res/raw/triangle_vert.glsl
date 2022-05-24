#version 300 es
uniform mat4 uMVPMatrix;
layout (location = 0) in vec4 aPosition;
layout(location = 1) in vec4 aColor;
out vec4 vColor;
void main() {
    gl_Position = uMVPMatrix * aPosition;
    vColor = aColor;
}

/*
uniform mat4 uMVPMatrix;
attribute  vec4 vPosition;
void main() {
    gl_Position = uMVPMatrix * vPosition;
}
*/