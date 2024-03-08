A snowy Jogl scene with two aliens and a security spotlight

The java program that renders a scene with a GUI of buttons to control the scene.

The scene contains:
-A vertical backdrop of glaciers
-An animation of snow falling mapped onto the vertical backdrop
-A horizontal plane of snow and glaciers
-Two textured aliens
-A security spotlight
-Two global lights
-A skybox for the sky
	
Both the aliens, the Spotlight and the snow fall are animated. The code for their animation, and modelling aliens and spotlight
is contained in their respective classes. Modelling and animating the snow fall is done in the Event Listener class and the shaders.

The horizontal plane and vertical backdrop are modelled directly whilst the other models utilise a heirarchical model.

Both the alien animations are for a defined number of seconds(max. 6 seconds).The alien animations can be executed at the same time, 
by pressing the buttons (i.e To start the head animation you don't have to wait for the body animation to finish).

There must be no class files when compiling the program.
The programme can be compiled using the command: javac Aliens.java
Then the programme can be run using: java Aliens
OR
The programme can be run using the script file run.sh
You will need to be in the directory which contains this script file, and the file also needs to be executable (chmod +x run.sh).
