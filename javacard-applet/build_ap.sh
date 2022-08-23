#!/bin/sh

converter.bat \
    dev.keiji.javacard.applet 0x0F:02:03:04:05 1.0 \
    -classdir build/classes/java/main \
    -applet 0x0F:02:03:04:05:06:07:03:02 dev.keiji.javacard.applet.App \
    -d build/applet \
    -target 3.0.4 \
    -i
