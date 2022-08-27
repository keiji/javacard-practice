package dev.keiji.javacard.applet;

import javacard.framework.*;

public class App extends Applet {
    private static final byte APDU_SELECT_CLA = 0x00;
    private static final byte APDU_SELECT_INS = (byte) 0xA4;

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

    @Override
    public void process(APDU apdu) throws ISOException {
        byte[] buffer = apdu.getBuffer();
        byte cla = buffer[ISO7816.OFFSET_CLA];
        byte ins = buffer[ISO7816.OFFSET_INS];
        byte p1 = buffer[ISO7816.OFFSET_P1];
        byte p2 = buffer[ISO7816.OFFSET_P2];

        if (cla == APDU_SELECT_CLA && ins == APDU_SELECT_INS) {
            return;
        }

        short bytesRead = apdu.setIncomingAndReceive();
        short echoOffset = 0;

        while (bytesRead > 0) {
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, buffer, echoOffset, bytesRead);
            echoOffset += bytesRead;
            bytesRead = apdu.receiveBytes(ISO7816.OFFSET_CDATA);
        }

        apdu.setOutgoing();
        apdu.sendBytes((short) 0, echoOffset);
    }
}
