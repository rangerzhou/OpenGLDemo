#version 310 es

uniform mat4 uObjectToWorld;
uniform mat4 uWorldToView;
uniform mat4 uWorldToScreen;
in vec3 aPosition;
in vec3 aNormal;

out vec3 vNormal;

void main() {
    vec4 posWS = uObjectToWorld*vec4(aPosition, 1.0);
    vNormal = vec3(uWorldToView*uObjectToWorld*vec4(aNormal, 0.0));
    gl_Position = uWorldToScreen*posWS;
}