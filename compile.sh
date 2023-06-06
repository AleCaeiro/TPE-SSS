#!/bin/bash
javac *.java
jar cfe SSSencoder.jar Main *.class
rm *.class
