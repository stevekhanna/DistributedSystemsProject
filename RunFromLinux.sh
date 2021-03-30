#!/bin/bash

gnome-terminal -e "bash -c jdk-14.0.2/bin/java -jar testDir/tmp/ServerIter3.jar"

gnome-terminal -e "bash -c jdk-14.0.2/bin/java -jar testDir/tmp/Client.jar 136.159.5.27 55921 \"Steve & Issack\""