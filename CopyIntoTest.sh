#!/bin/bash

#Copy Client jar into testDir
cp out/artifacts/DistributedSystemsProject_jar/DistributedSystemsProject.jar testDir/TestJARS/
mv testDir/TestJARS/DistributedSystemsProject.jar testDir/TestJARS/Client.jar
#Copy Server jar into testDir
mv out/artifacts/ServerIter3/DistributedSystemsProject.jar out/artifacts/ServerIter3/ServerIter3.jar
cp out/artifacts/ServerIter3/ServerIter3.jar testDir/TestJARS/