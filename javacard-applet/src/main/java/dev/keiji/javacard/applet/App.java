package dev.keiji.javacard.applet;

import javacard.framework.*;
import javacard.security.Signature;

public class App extends Applet {
    private static final byte APDU_SELECT_CLA = 0x00;
    private static final byte APDU_SELECT_INS = (byte) 0xA4;

    private static final byte APDU_VERIFY_INS = 0x20;
    private static final byte APDU_READ_BINARY_INS = (byte) 0xB0;
    private static final byte APDU_COMPUTE_DIGITAL_SIGNATURE_INS = (byte) 0x2A;

    private static final byte[] DEFAULT_PIN = new byte[]{0x04, 0x05, 0x06, 0x07};

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        App theApplet = new App();
        theApplet.register();
    }

    private byte counter = 0;

    private OwnerPIN ownerPin;
    private KeyData keyData = null;

    private App() {
        initialize();
    }

    private void initialize() {

        // Initialize PIN
        ownerPin = new OwnerPIN((byte) 3, (byte) 4);
        ownerPin.update(DEFAULT_PIN, (short) 0, (byte) DEFAULT_PIN.length);

        keyData = new KeyData();
    }

    @Override
    public void process(APDU apdu) throws ISOException {
        byte cla = apdu.getBuffer()[ISO7816.OFFSET_CLA];
        byte ins = apdu.getBuffer()[ISO7816.OFFSET_INS];

        if (cla == APDU_SELECT_CLA && ins == APDU_SELECT_INS) {
            return;
        }

        switch (ins) {
            case APDU_VERIFY_INS:
                processVerify(apdu);
                break;
            case APDU_READ_BINARY_INS:
                processReadBinary(apdu);
                break;
            case APDU_COMPUTE_DIGITAL_SIGNATURE_INS:
                processComputeDigitalSignature(apdu);
                counter++;
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
        ownerPin.check(buffer, ISO7816.OFFSET_CDATA, (byte) lc);
        if (!ownerPin.isValidated()) {
            short statusWord = (short) ((SW1_MEMORY_CHANGED << 8) | ownerPin.getTriesRemaining());
            ISOException.throwIt(statusWord);
        }
    }

    private void processReadBinary(APDU apdu) {
        if (!ownerPin.isValidated()) {
            ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        }

        short le = apdu.setOutgoing();
        apdu.setOutgoingLength((short) 2);
        apdu.sendBytesLong(new byte[]{1, counter}, (short) 0, (short) 2);
    }

    private void processComputeDigitalSignature(APDU apdu) {
        if (!ownerPin.isValidated()) {
            ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        }

        short lc = apdu.setIncomingAndReceive();
        byte[] targetData = new byte[lc];
        Util.arrayCopy(apdu.getBuffer(), ISO7816.OFFSET_CDATA, targetData, (short) 0, lc);

        Signature signature = Signature.getInstance(keyData.algorithm, false);
        signature.init(keyData.keyPair.getPrivate(), Signature.MODE_SIGN);

        byte[] sign = new byte[signature.getLength()];
        signature.sign(targetData, (short) 0, lc, sign, (short) 0);

        apdu.setOutgoing();
        apdu.setOutgoingLength((short) sign.length);
        apdu.sendBytesLong(sign, (short) 0, (short) sign.length);
    }
}
