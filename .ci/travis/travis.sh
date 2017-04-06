#!/bin/sh
./gradlew --stacktrace \
:louter:check :louter:jacocoReport \
:louter-compiler:check :louter-compiler:jacocoReport \
:louter-annotations:check :louter-annotations:jacocoReport \
:app:check