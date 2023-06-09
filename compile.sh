#!/bin/bash
javac ./src/*.java
jar cfe SSSencoder.jar Main -C ./src .
rm ./src/*.class
