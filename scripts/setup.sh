#!/bin/sh
git submodule update --init --recursive
./gradlew setupCauldron installBundle
