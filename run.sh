#!/bin/bash

# Deletes all .class files in the current directory
find . -type f -name "*.class" -delete

# Compile Aliens.java
javac Aliens.java

# Run the compiled Aliens.java program
java Aliens
