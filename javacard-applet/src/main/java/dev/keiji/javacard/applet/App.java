package dev.keiji.javacard.applet;

import javacard.framework.*;

public class App extends Applet {
    private static final byte APDU_SELECT_CLA = 0x00;
    private static final byte APDU_SELECT_INS = (byte) 0xA4;

    private static final byte INS_VERIFY = 0x20;

    private static final byte[] DEFAULT_PIN = new byte[]{0x04, 0x05, 0x06, 0x07};

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        App theApplet = new App();
        theApplet.register();
    }

    private OwnerPIN ownerPin;
    private boolean pinLocked = true;

    private App() {
        initialize();
    }

    private void initialize() {

        // Initialize PIN
        ownerPin = new OwnerPIN((byte) 3, (byte) 4);
        ownerPin.update(DEFAULT_PIN, (short) 0, (byte) DEFAULT_PIN.length);
    }

    @Override
    public void process(APDU apdu) throws ISOException {
        byte cla = apdu.getBuffer()[ISO7816.OFFSET_CLA];
        byte ins = apdu.getBuffer()[ISO7816.OFFSET_INS];

        if (cla == APDU_SELECT_CLA && ins == APDU_SELECT_INS) {
            return;
        }

        switch (ins) {
            case INS_VERIFY:
                processVerify(apdu);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_UNKNOWN);
                break;
        }
    }

    private static final byte SW1_MEMORY_CHANGED = 0x63;

    private void processVerify(APDU apdu) {
        byte[] buffer = apdu.getBuffer();

        short lc = apdu.setIncomingAndReceive();
        pinLocked = !ownerPin.check(buffer, ISO7816.OFFSET_CDATA, (byte) lc);
        if (pinLocked) {
            short statusWord = (short) ((SW1_MEMORY_CHANGED << 8) | ownerPin.getTriesRemaining());
            ISOException.throwIt(statusWord);
        }
    }
}
