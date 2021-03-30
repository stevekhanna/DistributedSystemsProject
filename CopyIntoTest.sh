#!/bin/bash

#Copy Client jar into testDir
cp out/artifacts/DistributedSystemsProject_jar/DistributedSystemsProject.jar testDir/tmp/
mv testDir/tmp/DistributedSystemsProject.jar testDir/tmp/Client.jar
#Copy Server jar into testDir
cp out/artifacts/ServerIter3/ServerIter3.jar testDir/tmp/