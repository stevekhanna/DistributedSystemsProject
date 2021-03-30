#!/bin/bash

#Copy Client jar into testDir
mv out/artifacts/Client/DistributedSystemsProject.jar out/artifacts/Client/Client.jar
cp out/artifacts/Client/Client.jar testDir/TestJARS/
#Copy Server jar into testDir
mv out/artifacts/ServerIter3/DistributedSystemsProject.jar out/artifacts/ServerIter3/ServerIter3.jar
cp out/artifacts/ServerIter3/ServerIter3.jar testDir/TestJARS/