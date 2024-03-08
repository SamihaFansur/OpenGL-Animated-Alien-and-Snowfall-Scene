/* I declare that this code is my own work */
/* Author Samiha Fansur sfansur1@sheffield.ac.uk */

import java.util.List;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;
import com.jogamp.opengl.util.texture.Texture;

/**
 * The Aliens_GLEventListener class implements GLEventListener and provides the necessary
 * methods for rendering a snowy scene with aliens.
 */

public class Aliens_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private TextureLibrary textures;
    
  public Aliens_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(0f,4f,14f));
    this.camera.setTarget(new Vec3(0f,4f,0f));
  }

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);   // default is 'CCW'
    gl.glEnable(GL.GL_DEPTH_TEST);   // default is 'back', assuming CCW
    startTime = getSeconds();

    initialise(gl);
  }

  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    for (Light value : allSceneLight) {
      value.dispose(gl);
    }
    verticalBackDrop.dispose(gl);
    horizontalPlane.dispose(gl);
    skyboxCube.dispose(gl);
    spotlight.dispose(gl);
    globalLight1.dispose(gl);
    globalLight2.dispose(gl);
  }
  
  //Turns GlobalLight1 on or off based on intensity.
  public void GlobalLight1() {
    if (allSceneLight.get(0).getLightIntensity() == 0)
      allSceneLight.get(0).setLightIntensity(1);
    else
      allSceneLight.get(0).setLightIntensity(0);
  }

  //Turns GlobalLight2 on or off based on intensity.
  public void GlobalLight2() {
    if (allSceneLight.get(1).getLightIntensity() == 0)
      allSceneLight.get(1).setLightIntensity(1);
    else
      allSceneLight.get(1).setLightIntensity(0);
  }

  //Turns Spotlight on of off based on intensity
  public void SpotLight() {
    if (allSceneLight.get(2).getLightIntensity() == 0)
      allSceneLight.get(2).setLightIntensity(1);
    else
      allSceneLight.get(2).setLightIntensity(0);
  }

  //Rocks Alien1
  public void Rock1() {
    alien1.startAnimation1(true); //starting animation
    alien1.setAnimation1Time(getSeconds());
  }
  
  //Rolls Alien1 head
  public void Roll1() {
    alien1.startAnimation2(true);  //starting animation
    alien1.setAnimation2Time(getSeconds());
  }
  
  //Rocks Alien2
  public void Rock2() {
    alien2.startAnimation1(true);  //starting animation
    alien2.setAnimation1Time(getSeconds());
  }
  
  //Rolls Alien2 Head
  public void Roll2() {
    alien2.startAnimation2(true);  //starting animation
    alien2.setAnimation2Time(getSeconds());
  }

  private Camera camera;

  //All models in the scene without own classes
  private Model sphere, cube, 
                 //Planes
                 verticalBackDrop, horizontalPlane,
                 //Skybox
                 skyboxCube,
                 //Spotlight
                 spotlightCubes, spotlightSpheres, 
                 //Alien 1
                 alien1BodySphere, alien1HeadSphere, alien1arm1Sphere, alien1arm2Sphere, alien1eye1Sphere,
                 alien1eye2Sphere, alien1ear1Sphere, alien1ear2Sphere, alien1antennabaseSphere, alien1antennatopSphere,
                 //Alien 2
                 alien2BodySphere, alien2HeadSphere, alien2arm1Sphere, alien2arm2Sphere, alien2eye1Sphere,
                 alien2eye2Sphere, alien2ear1Sphere, alien2ear2Sphere, alien2antennabaseSphere, alien2antennatopSphere;

  //Models with their own classes
  private Skybox skybox;
  private Alien alien1;  
  private Alien alien2;
  private Security_Spotlight securitySpotlight;  

  //lights
  private List<Light> allSceneLight;
  private Light globalLight1, globalLight2, spotlight;

  //Scene node
  private SGNode scene;
  private double startTime;

  private void initialise(GL3 gl) {
    TextureLibrary textureLibrary = new TextureLibrary();
    // Loading the textures
    //Vertical backdrop textures
    textureLibrary.add(gl, "textureId0", "textures/snowfall.jpg");
    textureLibrary.add(gl, "textureId1", "textures/wallpaper.jpg");
    //Skybox texture
    textureLibrary.add(gl, "textureId2", "textures/skybox.jpg");
    //Spotlight textures
    textureLibrary.add(gl, "textureId3", "textures/spotlight.jpg");
    textureLibrary.add(gl, "textureId4", "textures/spotlight_specular.jpg");
    //Horizontal plane textures (floor)
    textureLibrary.add(gl, "textureId5", "textures/floor.jpg");
    textureLibrary.add(gl, "textureId6", "textures/floor_specular.jpg");
    //Alien1 textures
    textureLibrary.add(gl, "textureId7", "textures/alien1body.jpg");
    textureLibrary.add(gl, "textureId8", "textures/alien1head.jpg");
    textureLibrary.add(gl, "textureId9", "textures/alien1arm1.jpg");
    textureLibrary.add(gl, "textureId10", "textures/alien1arm2.jpg");
    textureLibrary.add(gl, "textureId11", "textures/alien1eye1.jpg");
    textureLibrary.add(gl, "textureId12", "textures/alien1eye2.jpg");
    textureLibrary.add(gl, "textureId13", "textures/alien1ear1.jpg");
    textureLibrary.add(gl, "textureId14", "textures/alien1ear2.jpg");
    textureLibrary.add(gl, "textureId15", "textures/alien1antennabase.jpg");
    textureLibrary.add(gl, "textureId16", "textures/alien1antennatop.jpg");
    //Alien 2 textures
    textureLibrary.add(gl, "textureId17", "textures/alien2body.jpg");
    textureLibrary.add(gl, "textureId18", "textures/alien2head.jpg");
    textureLibrary.add(gl, "textureId19", "textures/alien2arm1.jpg");
    textureLibrary.add(gl, "textureId20", "textures/alien2arm2.jpg");
    textureLibrary.add(gl, "textureId21", "textures/alien2eye1.jpg");
    textureLibrary.add(gl, "textureId22", "textures/alien2eye2.jpg");
    textureLibrary.add(gl, "textureId23", "textures/alien2ear1.jpg");
    textureLibrary.add(gl, "textureId24", "textures/alien2ear2.jpg");
    textureLibrary.add(gl, "textureId25", "textures/alien2antennabase.jpg");
    textureLibrary.add(gl, "textureId26", "textures/alien2antennatop.jpg");
    //Alien 1 specular textures
    textureLibrary.add(gl, "textureId27", "textures/alien1body_specular.jpg");
    textureLibrary.add(gl, "textureId28", "textures/alien1head_specular.jpg");
    textureLibrary.add(gl, "textureId29", "textures/alien1arm1_specular.jpg");
    textureLibrary.add(gl, "textureId30", "textures/alien1arm2_specular.jpg");
    textureLibrary.add(gl, "textureId31", "textures/alien1eye1_specular.jpg");
    textureLibrary.add(gl, "textureId32", "textures/alien1eye2_specular.jpg");
    textureLibrary.add(gl, "textureId33", "textures/alien1ear1_specular.jpg");
    textureLibrary.add(gl, "textureId34", "textures/alien1ear2_specular.jpg");
    textureLibrary.add(gl, "textureId35", "textures/alien1antennabase_specular.jpg");
    textureLibrary.add(gl, "textureId36", "textures/alien1antennatop_specular.jpg");
    //Alien 2 specular textures
    textureLibrary.add(gl, "textureId37", "textures/alien2body_specular.jpg");
    textureLibrary.add(gl, "textureId88", "textures/alien2head_specular.jpg");
    textureLibrary.add(gl, "textureId39", "textures/alien2arm1_specular.jpg");
    textureLibrary.add(gl, "textureId40", "textures/alien2arm2_specular.jpg");
    textureLibrary.add(gl, "textureId41", "textures/alien2eye1_specular.jpg");
    textureLibrary.add(gl, "textureId42", "textures/alien2eye2_specular.jpg");
    textureLibrary.add(gl, "textureId43", "textures/alien2ear1_specular.jpg");
    textureLibrary.add(gl, "textureId44", "textures/alien2ear2_specular.jpg");
    textureLibrary.add(gl, "textureId45", "textures/alien2antennabase_specular.jpg");
    textureLibrary.add(gl, "textureId46", "textures/alien2antennatop_specular.jpg");

    // Retrieving the textures to use in Models
    Texture textureId0 = textureLibrary.get("textureId0");
    Texture textureId1 = textureLibrary.get("textureId1");
    Texture textureId2 = textureLibrary.get("textureId2");
    Texture textureId3 = textureLibrary.get("textureId3");
    Texture textureId4 = textureLibrary.get("textureId4");
    Texture textureId5 = textureLibrary.get("textureId5");
    Texture textureId6 = textureLibrary.get("textureId6");
    Texture textureId7 = textureLibrary.get("textureId7");
    Texture textureId8 = textureLibrary.get("textureId8");
    Texture textureId9 = textureLibrary.get("textureId9");
    Texture textureId10 = textureLibrary.get("textureId10");
    Texture textureId11 = textureLibrary.get("textureId11");
    Texture textureId12 = textureLibrary.get("textureId12");
    Texture textureId13 = textureLibrary.get("textureId13");
    Texture textureId14 = textureLibrary.get("textureId14");
    Texture textureId15 = textureLibrary.get("textureId15");
    Texture textureId16 = textureLibrary.get("textureId16");
    Texture textureId17 = textureLibrary.get("textureId17");
    Texture textureId18 = textureLibrary.get("textureId18");
    Texture textureId19 = textureLibrary.get("textureId19");
    Texture textureId20 = textureLibrary.get("textureId20");
    Texture textureId21 = textureLibrary.get("textureId21");
    Texture textureId22 = textureLibrary.get("textureId22");
    Texture textureId23 = textureLibrary.get("textureId23");
    Texture textureId24 = textureLibrary.get("textureId24");
    Texture textureId25 = textureLibrary.get("textureId25");
    Texture textureId26 = textureLibrary.get("textureId26");
    Texture textureId27 = textureLibrary.get("textureId27");
    Texture textureId28 = textureLibrary.get("textureId28");
    Texture textureId29 = textureLibrary.get("textureId29");
    Texture textureId30 = textureLibrary.get("textureId30");
    Texture textureId31 = textureLibrary.get("textureId31");
    Texture textureId32 = textureLibrary.get("textureId32");
    Texture textureId33 = textureLibrary.get("textureId33");
    Texture textureId34 = textureLibrary.get("textureId34");
    Texture textureId35 = textureLibrary.get("textureId35");
    Texture textureId36 = textureLibrary.get("textureId36");
    Texture textureId37 = textureLibrary.get("textureId37");
    Texture textureId38 = textureLibrary.get("textureId38");
    Texture textureId39 = textureLibrary.get("textureId39");
    Texture textureId40 = textureLibrary.get("textureId40");
    Texture textureId41 = textureLibrary.get("textureId41");
    Texture textureId42 = textureLibrary.get("textureId42");
    Texture textureId43 = textureLibrary.get("textureId43");
    Texture textureId44 = textureLibrary.get("textureId44");
    Texture textureId45 = textureLibrary.get("textureId45");
    Texture textureId46 = textureLibrary.get("textureId46");

    //Scene
    scene = new NameNode("Snowy Scene");

    //All lights in the scene
    this.allSceneLight = new java.util.ArrayList<>();

    globalLight1 = new Light(gl);
    globalLight1.setCamera(camera);
    allSceneLight.add(globalLight1);

    globalLight2 = new Light(gl);
    globalLight2.setCamera(camera);
    allSceneLight.add(globalLight2);

    spotlight = new Light(gl);
    spotlight.setCamera(camera);
    allSceneLight.add(spotlight);

    //Horizontal plane
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "shaders/vs_allObjs.txt", "shaders/fs_allObjs.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), 16.0f);
    Mat4 modelMatrix = Mat4Transform.scale(12,1f,12);
    horizontalPlane = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId5, textureId6);

    //Animated texture map of snow falling on vertical plane
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "shaders/vs_animatedTex.txt", "shaders/fs_animatedTex.txt");
    material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), 5);
    modelMatrix = Mat4Transform.scale(12f,1f,12);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,6.0f,-6.0f), modelMatrix);
    verticalBackDrop = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId1, textureId0);

    //Resetting mesh, material and shaders
    mesh= new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone()); 
    material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), 5);
    shader = new Shader(gl, "shaders/vs_allObjs.txt", "shaders/fs_allObjs.txt");

    // Alien 1 Shapes
    alien1BodySphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId7, textureId27);
    alien1HeadSphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId8, textureId28);
    alien1arm1Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId9, textureId29);
    alien1arm2Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId10, textureId30);
    alien1eye1Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId11, textureId31);
    alien1eye2Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId12, textureId32);
    alien1ear1Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId13, textureId33);
    alien1ear2Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId14, textureId34);
    alien1antennabaseSphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId15, textureId35);
    alien1antennatopSphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId16, textureId36);
    
    //Alien 2 Shapes
    alien2BodySphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId17, textureId37);
    alien2HeadSphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId18, textureId38);
    alien2arm1Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId19, textureId39);
    alien2arm2Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId20, textureId40);
    alien2eye1Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId21, textureId41);
    alien2eye2Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId22, textureId42);
    alien2ear1Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId23, textureId43);
    alien2ear2Sphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId24, textureId44);
    alien2antennabaseSphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId25, textureId45);
    alien2antennatopSphere = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId26, textureId46);
    
    //Security spotlight
    spotlightSpheres = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId3, textureId4);

    // Skybox Object
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    //Different shaders to create skybox effect and to not have scene lights affect it
    shader = new Shader(gl, "shaders/vs_skybox.txt", "shaders/fs_skybox.txt");
    material = new Material(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(50,50,50), Mat4Transform.translate(0,0,0));
    skyboxCube = new Model(gl, camera, allSceneLight, shader, material, modelMatrix, mesh, textureId2);

    //Scene Objects initialised from their own class
    skybox = new Skybox(gl, skyboxCube);
    alien1 = new Alien(gl, alien1BodySphere, alien1HeadSphere, alien1arm1Sphere, alien1arm2Sphere, alien1eye1Sphere,
                 alien1eye2Sphere, alien1ear1Sphere, alien1ear2Sphere, alien1antennabaseSphere, alien1antennatopSphere, 0,0,0);
    alien2 = new Alien(gl, alien2BodySphere, alien2HeadSphere, alien2arm1Sphere, alien2arm2Sphere, alien2eye1Sphere,
                 alien2eye2Sphere, alien2ear1Sphere, alien2ear2Sphere, alien2antennabaseSphere, alien2antennatopSphere, -4.2f,0,0);
    securitySpotlight = new Security_Spotlight(gl, spotlightSpheres, -4.2f,3.5f,0f);

    //Updates scene
    scene.update();  
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    //Skybox rendered outside of the depth test to make it appear far away.
    gl.glDisable(GL.GL_DEPTH_TEST); 
    skybox.render();
    gl.glEnable(GL.GL_DEPTH_TEST); //Re-initialising depth test for other objects.

    //Vertical backdrop and snowfall animation
    double elapsedTime = getSeconds() - startTime;
    float offsetY = (float) (elapsedTime * 0.1 % 1); //seamless animation loop
    float offsetX = 0.0f ;
    
    verticalBackDrop.getShader().use(gl);
    verticalBackDrop.getShader().setFloat(gl, "offset", offsetX, offsetY);
    verticalBackDrop.getShader().setInt(gl, "static_texture", 0); //texture 1
    verticalBackDrop.getShader().setInt(gl, "scrolling_texture", 1);  //Texture 2
    verticalBackDrop.render(gl);
    
    //Horizontal plane
    horizontalPlane.render(gl); 

    //Renders Global Light 1 if turned on (top centre left)
    globalLight1.setPosition(2.5f,10f,0f);
    if(globalLight1.getLightIntensity() == 1)
      globalLight1.render(gl);
    

    //Renders Global Light 2 if turned on (top centre right)
    globalLight2.setPosition(-2.5f,10f,0f);
    if(globalLight2.getLightIntensity() == 1)
      globalLight2.render(gl);

    //Renders Spotlight and its light
    spotlight.setPosition(securitySpotlight.getSpotlightLightPosition()); 
    spotlight.setLightDirection(securitySpotlight.getSpotlightLightDirection());
    securitySpotlight.render();

    //Alien 1 animations
    if(alien1.getAnimation1())
      alien1.rockAlien(); 
    if(alien1.getAnimation2())
      alien1.rollAlienHead();

    alien1.render();

    //Alien 2 animations
    if(alien2.getAnimation1())
      alien2.rockAlien(); 
    if(alien2.getAnimation2())
      alien2.rollAlienHead();

    alien2.render();
  }

  //Returns amount of time passed in system.
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
}