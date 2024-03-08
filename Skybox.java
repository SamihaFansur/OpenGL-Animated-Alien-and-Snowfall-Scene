/* I declare that this code is my own work */
/* Author Samiha Fansur sfansur1@sheffield.ac.uk */
import com.jogamp.opengl.*;
import gmaths.*;

/**
 * The Skybox class represents a skybox in a 3D scene.
 * It includes methods for creating the skybox model and animating it.
 */

public class Skybox{

    private GL3 gl;
    private SGNode beginSkybox;
    public TransformNode makeSkybox;

    public Skybox(GL3 gl, Model skyboxCube){
        this.gl = gl;
        
        //Skybox tree hierarchy start
        beginSkybox = new NameNode("Snowflake Skybox");

        //Translates skybox to its initial position
        TransformNode moveSkyboxToOriginalPos = new TransformNode("Translate entire Skybox to initial position",Mat4Transform.translate(0,0,0));

        //Skybox
        NameNode skybox = new NameNode("Skybox");
        Mat4 mesh = Mat4Transform.scale(50,50,50);
        makeSkybox = new TransformNode("Transform skybox", mesh);
        ModelNode skyboxNode = new ModelNode("Skybox", skyboxCube);

        //Hierarchal tree scene graph for skybox
        beginSkybox.addChild(moveSkyboxToOriginalPos);
            moveSkyboxToOriginalPos.addChild(skybox);
                skybox.addChild(makeSkybox);
                    makeSkybox.addChild(skyboxNode);

        beginSkybox.update();
    }
    
    //Render skybox
    public void render(){
        beginSkybox.draw(gl);
        rotateSkybox(); //starts skybox animation
    }

    //Rotate skybox animation
    public void rotateSkybox() {
        Mat4 rotate = makeSkybox.getTransform();
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundY(0.3f));
        makeSkybox.setTransform(rotate);
        makeSkybox.update();
    }

}
