/* I declare that this code is my own work */
/* Author Samiha Fansur sfansur1@sheffield.ac.uk */

import javax.xml.crypto.dsig.Transform;
import com.jogamp.opengl.*;
import gmaths.*;

/**
 * The Security_Spotlight class represents an animated security spotlight in a 3D scene.
 * It includes methods for creating the spotlight model and animating it.
 */

public class Security_Spotlight {

    private GL3 gl;

    private double startTime = 0;

    public SGNode beginSpotlight;

    private TransformNode makeLightHolder, rotateAll;

    private NameNode spotlightLight;
    
    private float rotatespotlightLightStart = 15, rotateLight = rotatespotlightLightStart;

    public Security_Spotlight(GL3 gl, Model sphere,float x, float y, float z)  {

        this.gl = gl;

        //Security spotlight tree heirarchy start
        beginSpotlight = new NameNode("Begin Security Spotlight");

        //Translates entire spotlight to original position
        TransformNode moveSpotlightToOriginalPos = new TransformNode("Translate entire security spotlight", Mat4Transform.translate(x,y,z));

        //Spotlight Base
        NameNode spotlightBase = new NameNode("Spotlight Base");
        Mat4 mesh = Mat4Transform.scale(0.2f,7f,0.2f);
        TransformNode makespotlightBase = new TransformNode("Transform spotlight base", mesh);
        ModelNode spotlightBaseNode = new ModelNode("Spotlight Base", sphere);

        //Spotlight Head
        NameNode spotlightHead = new NameNode("Spotlight Head");
        mesh = Mat4Transform.scale(0.5f,1f,0.5f);
        mesh = Mat4.multiply(Mat4Transform.rotateAroundZ(30), mesh);
        TransformNode makespotlightHead = new TransformNode("Transform spotlight head", mesh);
        ModelNode spotlightHeadNode = new ModelNode("Spotlight Head", sphere);

        //Bulb
        NameNode lightHolder = new NameNode("Light Holder");
        mesh = Mat4Transform.scale(0.1f,0.1f,0.1f);
        mesh = Mat4.multiply(Mat4Transform.translate(0f,-0.5f,0), mesh);
        makeLightHolder = new TransformNode("Transform light holder", mesh);
        ModelNode lightHolderNode=new ModelNode("Light Holder Square", sphere);

        //Node to animate head roatation and to set intitial rotation
        TransformNode spotlightHeadRotate = new TransformNode("Rotate Head",Mat4Transform.rotateAroundY(0f));
        
        //Nodes for setting direction of the Spotlight
        TransformNode translateHolder = new TransformNode("Holder translate", Mat4Transform.translate(0f, -1f, -0.2f));                                           
        spotlightLight = new NameNode("Holder Spotlight");
        
        //Center spotlight parts
        TransformNode translateToHalfBaseHeightHeightandCentre = new TransformNode("translate to half spotlight base size and center", Mat4Transform.translate(0f,0f,0));
        TransformNode moveHeadToCorrectPos = new TransformNode("translate head to correct position", Mat4Transform.translate(0.2f, 3.6f,0));

        rotateAll = new TransformNode("Rotating spotlight", Mat4Transform.rotateAroundZ(rotateLight));
       
        //Hierarchial tree scene graph for spotlight
        beginSpotlight.addChild(moveSpotlightToOriginalPos);
            //Spotlight Base
            moveSpotlightToOriginalPos.addChild(spotlightBase);
                spotlightBase.addChild(makespotlightBase);
                    makespotlightBase.addChild(spotlightBaseNode);
                        spotlightBase.addChild(translateToHalfBaseHeightHeightandCentre); //Centre spotlight parts
                        //Spotlight head
                        translateToHalfBaseHeightHeightandCentre.addChild(moveHeadToCorrectPos);
                            moveHeadToCorrectPos.addChild(rotateAll);
                                rotateAll.addChild(spotlightHead);                                       
                                    spotlightHead.addChild(spotlightHeadRotate);
                                        spotlightHeadRotate.addChild(makespotlightHead);
                                            makespotlightHead.addChild(spotlightHeadNode);
                                        //Bulb
                                        spotlightHeadNode.addChild(lightHolder);
                                            lightHolder.addChild(makeLightHolder);
                                                makeLightHolder.addChild(lightHolderNode);
                                            //spotlight direction
                                            lightHolder.addChild(translateHolder);
                                                translateHolder.addChild(spotlightLight);
                            

        beginSpotlight.update();  // IMPORTANT – must be done every time any part of the scene graph changes
    }

    //Maps light onto the spotlight head and getter for spotlight light position
    public Vec3 getSpotlightLightPosition(){
        return spotlightLight.getWorldTransform().getLightPosition();
    }

    //Getter for spotlight light direction
    public Vec3 getSpotlightLightDirection() {
        Vec3 spotlightPosition = spotlightLight.getWorldTransform().getLightPosition();
        Vec3 bulbPosition = makeLightHolder.getWorldTransform().getLightPosition();
        return Vec3.subtract(spotlightPosition, bulbPosition);
    }

    //Function to render the spotlight
    public void render(){
        beginSpotlight.draw(gl);
        moveSpotlightLight(); //starts spotlight animation
    }

    private void moveSpotlightLight() {
        double elapsedTime = getSeconds() - startTime;
        
        // Vertical movement
        float rotateAroundX = rotatespotlightLightStart*(float)Math.sin(elapsedTime);
        
        // Horizontal movement
        float stretchFactor = 0.9f;
        float rotateAroundZ = rotatespotlightLightStart*stretchFactor*(float)Math.cos(elapsedTime);
        
        Mat4 rotationAroundX = Mat4Transform.rotateAroundX(rotateAroundX);
        Mat4 rotationAroundZ = Mat4Transform.rotateAroundZ(rotateAroundZ);
        
        rotateAll.setTransform(Mat4.multiply(rotationAroundZ, rotationAroundX));
        
        beginSpotlight.update(); // IMPORTANT – the scene graph has changed
    }  

    //Returns the time passed in the system.
    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

}
