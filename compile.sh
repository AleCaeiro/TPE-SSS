#!/bin/bash
javac ./src/*.java
jar cfe ss.jar Main -C ./src .
rm ./src/*.class
