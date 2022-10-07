precision highp float;
uniform sampler2D u_Texture;
varying vec2 vTexCoordinate;
void main() {
  gl_FragColor = texture2D(u_Texture, vTexCoordinate);
}