import gmaths.*;
import com.jogamp.opengl.*;
import java.util.List;
import com.jogamp.opengl.util.texture.Texture;


public class Model {
  
  private final Mesh mesh;
  private final Texture textureId1, textureId2;
  private final Material material;
  private final Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private final List<Light> allSceneLight;

  //Edited constructors to allow light parameter to be passed in
  public Model(GL3 gl, Camera camera, List<Light> allSceneLight, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, Texture textureId1, Texture textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.allSceneLight = allSceneLight;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  // Constructor for a single texture
  public Model(GL3 gl, Camera camera, List<Light> allSceneLight, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, Texture textureId1) {
    this(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  // Constructor for no textures
  public Model(GL3 gl, Camera camera, List<Light> allSceneLight, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, null, null);
  }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  //Adding new light to exisitng lights in scene
  public void addLight(Light light) {
    this.allSceneLight.add(light);
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    //GlobalLight 1
    shader.setVec3(gl, "globalLight1.position", allSceneLight.get(0).getPosition());
    shader.setFloat(gl,"globalLight1.globalLightOnOff", allSceneLight.get(0).getLightIntensity());
    shader.setVec3(gl, "globalLight1.diffuse", allSceneLight.get(0).getMaterial().getDiffuse());
    shader.setVec3(gl, "globalLight1.specular", allSceneLight.get(0).getMaterial().getSpecular());    
    shader.setVec3(gl, "globalLight1.ambient", allSceneLight.get(0).getMaterial().getAmbient());

    //GlobalLight 2
    shader.setVec3(gl, "globalLight2.position", allSceneLight.get(1).getPosition());
    shader.setFloat(gl,"globalLight2.globalLightOnOff", allSceneLight.get(1).getLightIntensity());
    shader.setVec3(gl, "globalLight2.diffuse", allSceneLight.get(1).getMaterial().getDiffuse());
    shader.setVec3(gl, "globalLight2.specular", allSceneLight.get(1).getMaterial().getSpecular());
    shader.setVec3(gl, "globalLight2.ambient", allSceneLight.get(1).getMaterial().getAmbient());

    //Security Spotlight
    shader.setVec3(gl, "spotlightLight.position", allSceneLight.get(2).getPosition());
    shader.setFloat(gl,"spotlightLight.spotlightOnOff", allSceneLight.get(2).getLightIntensity());
    shader.setVec3(gl, "spotlightLight.diffuse", allSceneLight.get(2).getMaterial().getDiffuse());
    shader.setVec3(gl, "spotlightLight.specular", allSceneLight.get(2).getMaterial().getSpecular());
    shader.setVec3(gl, "spotlightLight.ambient", allSceneLight.get(2).getMaterial().getAmbient());
    shader.setVec3(gl, "spotlightLight.direction", allSceneLight.get(2).getLightDirection());
    shader.setFloat(gl,"spotlightLight.cutOffAngle1", (float)Math.cos(Math.toRadians(10.0f))); // Smaller angle
    shader.setFloat(gl,"spotlightLight.cutOffAngle2", (float)Math.cos(Math.toRadians(15.0f))); // Smaller angle

    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setFloat(gl, "material.shininess", material.getShininess());  
    
    if (textureId1!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      textureId1.bind(gl);
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      textureId2.bind(gl);
    }
    
    mesh.render(gl);
  }
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }

  public Material getMaterial() {
    return this.material;
  }
  
  private boolean mesh_null() {
    return (mesh==null);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);  // only need to dispose of mesh
  }

  // Accessor for the model's shader program
  public Shader getShader() {
    return this.shader;
  }
}
