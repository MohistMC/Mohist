#!/bin/sh
find patches -type f -name '*.java.patch' -exec sed -i "s/\t/    /g" {} \;
find src/main/java -type f -name '*.java' -exec sed -i "s/\t/    /g" {} \;

