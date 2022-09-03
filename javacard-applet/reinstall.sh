#!/bin/sh

java -jar ~/bin/gp.jar --uninstall build/applet/dev/keiji/javacard/applet/javacard/applet.cap && \
  ./gradlew assemble convertToCap && \
  java -jar ~/bin/gp.jar --install build/applet/dev/keiji/javacard/applet/javacard/applet.cap