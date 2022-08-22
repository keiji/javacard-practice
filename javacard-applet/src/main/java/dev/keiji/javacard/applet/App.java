package dev.keiji.javacard.applet;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISOException;

public class App extends Applet {
    private App() {
        register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        App theApplet = new App();
    }

    @Override
    public boolean select() {
        return true;
    }

    private int a = 0;

    @Override
    public void process(APDU apdu) throws ISOException {
        a++;
    }
}
