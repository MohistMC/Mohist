#!/bin/sh
git submodule update --init --recursive
./gradlew launch4j
