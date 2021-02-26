#!/bin/bash          
            DIR_TO_Remove=$PWD"/reports"
            if [ -e "$DIR_TO_Remove" ];
            then
              echo "REMOVING: "$DIR_TO_Remove;
              rm -rf "$DIR_TO_Remove";
            else
              echo "reports folder doesn't exist.";
            fi
            DIR_TO_Remove=$PWD"/sourceCode"
            if [ -e "$DIR_TO_Remove" ];
            then
              echo "REMOVING: "$DIR_TO_Remove;
              rm -rf "$DIR_TO_Remove";
            else
              echo "sourceCode folder doesn't exist.";
            fi
            echo "Clean up complete"
