uniform mat4 uMVPMatrix;
uniform mat2 uRotationMatrix;
uniform vec2 uCenterPosition;
attribute vec2 vPosition;
attribute vec2 aTexCoordinate;
varying vec2 vTexCoordinate;
void main(){
    vec2 centeredVector = vec2(vPosition.x - uCenterPosition.x, vPosition.y - uCenterPosition.y);
    vec2 rotatedCenteredVector = centeredVector * uRotationMatrix;
    vec2 resultedVector = vec2(rotatedCenteredVector.x + uCenterPosition.x, rotatedCenteredVector.y + uCenterPosition.y);
    gl_Position = uMVPMatrix * vec4(resultedVector.x / 5.0 - 1.5, resultedVector.y / 5.0 - 1.0, 0.0, 1.0);
    vTexCoordinate = vec2(aTexCoordinate.x, (1.0 - (aTexCoordinate.y)));
}