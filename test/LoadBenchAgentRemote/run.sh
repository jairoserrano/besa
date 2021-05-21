#!/usr/bin/bash
java -Djava.library.path=/home/jairo/BESA/opencv/build/lib/ -cp dist/LoadBenchAgentRemote.jar ContainersLauncher.LoadContainer_$1  > $1.txt 2>&1