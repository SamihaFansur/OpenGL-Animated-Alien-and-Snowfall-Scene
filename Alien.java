/* I declare that this code is my own work */
/* Author Samiha Fansur sfansur1@sheffield.ac.uk */
import com.jogamp.opengl.*;
import gmaths.*;

/**
 * The Alien class represents an animated alien character in a 3D environment.
 * It includes methods for rendering and animating the alien.
 */
public class Alien {

    private GL3 gl;

    //Animation time variables
    private double startTime1 = getSeconds(), startTime2 = getSeconds();
    private double animation1Time, animation2Time;
    private boolean startAnimation1 = false, startAnimation2 = false;

    //Animation angles
    private float rockAllAngleStart = 0, rockAllAngle = rockAllAngleStart;
    private float moveLeftArmStart = 0, moveLeftArmAngle = moveLeftArmStart;
    private float moveRightArmStart = 0, moveRightArmAngle = moveRightArmStart;

    private float rollHeadAngleStart = 0, rollHeadAngle = rollHeadAngleStart;
    private float moveLeftEarStart = 0, moveLeftEarAngle = moveLeftEarStart;
    private float moveRightEarStart = 0, moveRightEarAngle = moveRightEarStart;

    public SGNode beginAlien; //root node
    private TransformNode rockAll, moveLeftArm, moveRightArm, rollHead, moveLeftEar, moveRightEar;
    
    public Alien(
        GL3 gl, Model alienBodySphere, Model alienHeadSphere, Model alienArm1Sphere, Model alienArm2Sphere, Model alienEye1Sphere, 
        Model alienEye2Sphere, Model alienEar1Sphere, Model alienEar2Sphere, Model alienAntennaBaseSphere, Model alienAntennaTopSphere, 
        float x, float y, float z
        ) 
    {
        this.gl = gl;

        //Alien tree heirarchy start
        beginAlien = new NameNode("Begin Alien");

        //Translates entire alien to original position
        TransformNode moveAlienToOriginalPos = new TransformNode("Translate entire alien", Mat4Transform.translate(x, y, z));
        
        //Alien Body
        NameNode alienBody = new NameNode("Alien body");
        Mat4 mesh = Mat4Transform.scale(2,2,2);
        TransformNode makealienBody = new TransformNode("Transform alien body", mesh);
        ModelNode alienBodyNode = new ModelNode("Alien body", alienBodySphere);

        //Left arm 
        NameNode leftArm = new NameNode("Left Arm");
        mesh = Mat4Transform.translate(0f,0.8f,0f);
        mesh = Mat4.multiply(Mat4Transform.scale(0.2f, 1.4f, 0.2f), mesh);
        mesh = Mat4.multiply(Mat4Transform.rotateAroundZ(55), mesh);
        mesh = Mat4.multiply(Mat4Transform.translate(0.96f,-0.3f,0f),mesh);


        TransformNode makeLeftArm = new TransformNode("Transform left arm", mesh);
        ModelNode leftArmNode = new ModelNode("Left Arm", alienArm1Sphere);

        //Right arm
        NameNode rightArm = new NameNode("Right Arm");
        mesh = Mat4Transform.translate(0f,0.8f,0f);
        mesh = Mat4.multiply(Mat4Transform.scale(0.2f, 1.4f, 0.2f), mesh);
        mesh = Mat4.multiply(Mat4Transform.rotateAroundZ(-55), mesh);
        mesh = Mat4.multiply(Mat4Transform.translate(-0.95f,-0.3f,0f),mesh);
        TransformNode makeRightArm = new TransformNode("Transform right arm", mesh);
        ModelNode rightArmNode = new ModelNode("Right Arm", alienArm2Sphere);

        //Head
        NameNode head = new NameNode("head");
        mesh = Mat4Transform.scale(1.3f, 1.3f, 1.3f);
        TransformNode makeHead = new TransformNode("Transform alien head", mesh);
        ModelNode headNode = new ModelNode("Head", alienHeadSphere);

        //Left ear
        NameNode leftEar = new NameNode("Left Ear");
        mesh = Mat4Transform.translate(0.45f,0.34f,0f);
        mesh = Mat4.multiply(Mat4Transform.scale(0.2f, 1f, 0.4f),mesh);
        TransformNode makeLeftEar = new TransformNode("Transform left ear", mesh);
        ModelNode leftEarNode = new ModelNode("Left Ear", alienEar1Sphere);

        //Right ear
        NameNode rightEar = new NameNode("Right Ear");
        mesh = Mat4Transform.translate(-0.45f,0.34f,0f);
        mesh = Mat4.multiply(Mat4Transform.scale(0.2f, 1f, 0.4f),mesh);
        TransformNode makeRightEar = new TransformNode("Transform right ear", mesh);
        ModelNode rightEarNode = new ModelNode("Right Ear", alienEar2Sphere);

        //Left Eye
        NameNode leftEye = new NameNode("Left Eye");
        mesh = Mat4Transform.scale(0.25f,0.25f,0.25f);
        TransformNode makeLeftEye = new TransformNode("Transform left eye", mesh);
        ModelNode leftEyeNode = new ModelNode("Left Eye", alienEye1Sphere);

        //Right eye
        NameNode rightEye = new NameNode("Right Eye");
        mesh = Mat4Transform.scale(0.25f,0.25f,0.25f);
        TransformNode makeRightEye = new TransformNode("Transform right eye", mesh);
        ModelNode rightEyeNode = new ModelNode("Right Eye", alienEye2Sphere);

        //Antenna Base
        NameNode antennaBase = new NameNode("Antenna Base");
        mesh = Mat4Transform.scale(0.1f,0.5f,0.1f);
        TransformNode makeAntennaBase = new TransformNode("Transform antenna base", mesh);
        ModelNode antennaBaseNode = new ModelNode("Antenna Base", alienAntennaBaseSphere);

        //Antenna Top
        NameNode antennaTop=new NameNode("Antenna Top");
        mesh = Mat4Transform.scale(10,2,10);
        mesh = Mat4.multiply(mesh, Mat4Transform.scale(0.2f, 0.2f, 0.2f));
        TransformNode makeAntennaTop = new TransformNode("Transform antenna top", mesh);
        ModelNode antennaTopNode = new ModelNode("Antenna Top", alienAntennaTopSphere);

        //Moving parts back after rock animation/z-rotation
        TransformNode translateBodyToCorrectPos = new TransformNode("translate body to correct position", Mat4Transform.translate(2.5f,1f,0));
        TransformNode translateLArmToCorrectPos = new TransformNode("translate left arm to correct position", Mat4Transform.translate(-1.45f,0,0));
        TransformNode translateRArmToCorrectPos = new TransformNode("translate right arm to correct position", Mat4Transform.translate(1.45f,0,0));
        TransformNode translateHeadToCorrectPos = new TransformNode("translate head to correct position", Mat4Transform.translate(0,1.6f,0f));
        TransformNode translateLEarToCorrectPos = new TransformNode("translate left ear to correct position", Mat4Transform.translate(-0.65f,0.5f,0));
        TransformNode translateREarToCorrectPos = new TransformNode("translate right ear to correct position", Mat4Transform.translate(0.65f,0.5f,0));
        TransformNode translateLEyeToCorrectPos = new TransformNode("translate left eye to correct position", Mat4Transform.translate(-0.3f,0.3f,0.6f));
        TransformNode translateREyeToCorrectPos = new TransformNode("translate right eye to correct position", Mat4Transform.translate(0.3f,0.3f,0.6f));
        TransformNode translateAntennaBaseToCorrectPos = new TransformNode("translate antenna base to correct position", Mat4Transform.translate(0,0.9f,0));
        TransformNode translateAntennaTopToCorrectPos = new TransformNode("translate antenna top to correct position", Mat4Transform.translate(0,0.69f,0));
        
        rockAll = new TransformNode("Rock Alien side to side", Mat4Transform.rotateAroundZ(rockAllAngle));  
        moveLeftArm = new TransformNode("Move Alien left arm", Mat4Transform.rotateAroundZ(moveLeftArmAngle));
        moveRightArm = new TransformNode("Move Alien right Ear", Mat4Transform.rotateAroundZ(moveRightArmAngle));

        rollHead = new TransformNode("Roll Alien head up and down", Mat4Transform.rotateAroundX(rollHeadAngle));
        moveLeftEar = new TransformNode("Move Alien left Ear", Mat4Transform.rotateAroundX(moveLeftEarAngle));
        moveRightEar = new TransformNode("Move Alien right Ear", Mat4Transform.rotateAroundX(moveRightEarAngle));

        //Hierarchial tree scene graph for alien
        beginAlien.addChild(moveAlienToOriginalPos);
            moveAlienToOriginalPos.addChild(translateBodyToCorrectPos);
                translateBodyToCorrectPos.addChild(rockAll);
                    //Body
                    rockAll.addChild(alienBody);
                        alienBody.addChild(makealienBody);
                            makealienBody.addChild(alienBodyNode);
                                //left arm
                                alienBody.addChild(translateLArmToCorrectPos);
                                    translateLArmToCorrectPos.addChild(moveLeftArm);
                                        moveLeftArm.addChild(leftArm);
                                            leftArm.addChild(makeLeftArm);
                                                makeLeftArm.addChild(leftArmNode);
                                //right arm
                                alienBody.addChild(translateRArmToCorrectPos);
                                    translateRArmToCorrectPos. addChild(moveRightArm);
                                        moveRightArm.addChild(rightArm);
                                            rightArm.addChild(makeRightArm);
                                                makeRightArm.addChild(rightArmNode);
                                //head
                                alienBody.addChild(translateHeadToCorrectPos);
                                    translateHeadToCorrectPos.addChild(rollHead);
                                        rollHead.addChild(head);
                                            head.addChild(makeHead);
                                                makeHead.addChild(headNode);
                                                    //left ear
                                                    head.addChild(translateLEarToCorrectPos);
                                                        translateLEarToCorrectPos.addChild(moveLeftEar);
                                                            moveLeftEar.addChild(makeLeftEar);
                                                                makeLeftEar.addChild(leftEar);
                                                                    leftEar.addChild(leftEarNode);
                                                    //right ear
                                                    head.addChild(translateREarToCorrectPos);
                                                        translateREarToCorrectPos.addChild(moveRightEar);
                                                            moveRightEar.addChild(makeRightEar);
                                                                makeRightEar.addChild(rightEar);
                                                                    rightEar.addChild(rightEarNode);
                                                    //left eye
                                                    head.addChild(translateLEyeToCorrectPos);
                                                        translateLEyeToCorrectPos.addChild(makeLeftEye);
                                                            makeLeftEye.addChild(leftEye);
                                                                leftEye.addChild(leftEyeNode);
                                                    //right eye
                                                    head.addChild(translateREyeToCorrectPos);
                                                        translateREyeToCorrectPos.addChild(makeRightEye);
                                                            makeRightEye.addChild(rightEye);
                                                                rightEye.addChild(rightEyeNode);
                                                    //antenna base
                                                    head.addChild(translateAntennaBaseToCorrectPos);
                                                        translateAntennaBaseToCorrectPos.addChild(makeAntennaBase);
                                                            makeAntennaBase.addChild(antennaBase);
                                                                antennaBase.addChild(antennaBaseNode);
                                                                    //antenna top
                                                                    antennaBase.addChild(translateAntennaTopToCorrectPos);
                                                                        translateAntennaTopToCorrectPos.addChild(makeAntennaTop);
                                                                            makeAntennaTop.addChild(antennaTop);
                                                                                antennaTop.addChild(antennaTopNode);


        beginAlien.update();  // IMPORTANT – must be done every time any part of the scene graph changes
    }
    
    //Sets animation start time
    public void setAnimation1Time(double startTime1){
        this.startTime1 = startTime1;
    }

    //Set animation boolean, animation starts if true else stops
    public void startAnimation1(boolean startAnimation1){
        this.startAnimation1 = startAnimation1;
    }

    //Get animation boolean 
    public  boolean getAnimation1(){
        return this.startAnimation1;
    }
    
    //Sets animation start time
    public void setAnimation2Time(double startTime2){
        this.startTime2 = startTime2;
    }

    //Set animation boolean, animation starts if true else stops
    public void startAnimation2(boolean startAnimation2){
        this.startAnimation2 = startAnimation2;
    }

    //Get animation boolean 
    public  boolean getAnimation2(){
        return this.startAnimation2;
    }

    //Renders alien
    public void render(){
        beginAlien.draw(gl);        
    }

    //Rock alien body animation
    public void rockAlien() {
        animation1Time = getSeconds() - startTime1;
        if(animation1Time <= 6.1) {
            double timeFactor = (Math.PI * animation1Time) / 2.0;
            //Body movement animation
            rockAllAngle = 15 * (float)Math.sin(timeFactor);  // Starts from 0 and oscillates between 15 and -15
            rockAll.setTransform(Mat4Transform.rotateAroundZ(rockAllAngle));

            //Left arm movement animation
            moveLeftArmAngle = 30 * (float)Math.sin(timeFactor);  // Starts from 0 and oscillates between 30 and -30
            moveLeftArm.setTransform(Mat4Transform.rotateAroundZ(moveLeftArmAngle));

            //Right arm movement animation
            moveRightArmAngle = -30 * (float)Math.sin(timeFactor);  // Starts from 0 and oscillates between -30 and 30
            moveRightArm.setTransform(Mat4Transform.rotateAroundZ(moveRightArmAngle));

            beginAlien.update();
        }   
    }

    //Roll alien head animation
    public void rollAlienHead() {
        animation2Time = getSeconds() - startTime2;
        if(animation2Time <= 4.57){
            //Head movement animation
            rollHeadAngle = 15 * (float)Math.sin(animation2Time * 2); // Starts from 0 and oscillates between 15 and -15
            rollHead.setTransform(Mat4Transform.rotateAroundX(rollHeadAngle));

            //Left ear movement animation
            moveLeftEarAngle = 30 * (float)Math.sin(animation2Time * 2); // Starts from 0 and oscillates between 30 and -30
            moveLeftEar.setTransform(Mat4Transform.rotateAroundX(moveLeftEarAngle));

            //Right ear movement animation
            moveRightEarAngle = -30 * (float)Math.sin(animation2Time * 2); // Starts from 0 and oscillates between -30 and 30
            moveRightEar.setTransform(Mat4Transform.rotateAroundX(moveRightEarAngle));

            beginAlien.update();
        }  
    } 
    
    //Returns the time passed in the system.
    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }
    
}
