#version 310 es

#define SHADOW_COLOR vec3(0.153, 0.216, 0.224)*0.5

precision mediump float;
uniform vec4 uColor;

in vec3 vNormal;

out vec4 fragColor;

void main() {
    float intensity = dot(normalize(vNormal), normalize(vec3(0.5, 1.0, 0.5)))*0.5 + 0.5;
    fragColor = vec4(mix(SHADOW_COLOR, uColor.rgb, intensity), uColor.a);
}
