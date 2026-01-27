package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class ApduCommandReadFromBytesTest {

    private final Random rand = new Random(System.currentTimeMillis());

    @Test
    public void readFromBytesCase1Test1_Exception1() {
        try {
            ApduCommand actual = ApduCommand.readFrom(null, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readFromBytesCase1Test1_Exception2() {
        try {
            ApduCommand actual = ApduCommand.readFrom(new byte[3], 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readFromBytesCase1Test1_Exception3() {
        try {
            ApduCommand actual = ApduCommand.readFrom(new byte[4], -1);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readFromBytesCase1Test1_Exception4() {
        try {
            ApduCommand actual = ApduCommand.readFrom(new byte[4], 1);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readFromBytesCase1Test1() {
        ApduCommand expected = ApduCommand.createCase1(0x00, 0xA4, 0x04, 0x00);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase2NotExtendedTest1() {
        ApduCommand expected = ApduCommand.createCase2(0x00, 0xA4, 0x04, 0x00, 0x02, false);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase2NotExtendedTest2() {
        ApduCommand expected = ApduCommand.createCase2(0x00, 0xA4, 0x04, 0x00, 0x00, false);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase2ExtendedTest1() {
        ApduCommand expected = ApduCommand.createCase2(0x00, 0xA4, 0x04, 0x00, 0x0100, true);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase3NotExtendedTest1() {
        byte[] data = new byte[255];
        rand.nextBytes(data);

        ApduCommand expected = ApduCommand.createCase3(0x00, 0xA4, 0x04, 0x00, data, false);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase3ExtendedTest1() {
        byte[] data = new byte[256];
        rand.nextBytes(data);
        ApduCommand expected = ApduCommand.createCase3(0x00, 0xA4, 0x04, 0x00, data, true);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase4NotExtendedTest1() {
        byte[] data = new byte[255];
        rand.nextBytes(data);
        ApduCommand expected = ApduCommand.createCase4(0x00, 0xA4, 0x04, 0x00, data, 0x01, false);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytesCase4ExtendedTest1() {
        byte[] data = new byte[256];
        rand.nextBytes(data);
        ApduCommand expected = ApduCommand.createCase4(0x00, 0xA4, 0x04, 0x00, data, 0x0100, true);

        byte[] apduData = expected.getBytes();

        ApduCommand actual = ApduCommand.readFrom(apduData, 0);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void readFromBytes_Exception_TruncatedData() {
        // Case 3 APDU: Header(4) + Lc(1) + Data(Lc).
        // Let's create one with Lc=2 but only provide 1 byte of data.
        byte[] truncatedApdu = new byte[] {
            0x00, (byte)0xA4, 0x04, 0x00, // Header
            0x02, // Lc = 2
            0x01 // Data (only 1 byte)
        };

        try {
            ApduCommand.readFrom(truncatedApdu, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`byteArray` length must be greater or equal 7", exception.getMessage());
        }
    }

    @Test
    public void readFromBytes_Exception_TruncatedData_Extended() {
        // Case 3e APDU: Header(4) + Lc(3) + Data(Lc).
        // Lc = 256 (0x00 0x01 0x00)
        // Expected Length = 4 + 3 + 256 = 263
        byte[] truncatedApdu = new byte[4 + 3 + 255]; // 1 byte short
        truncatedApdu[0] = 0x00;
        truncatedApdu[1] = (byte) 0xA4;
        truncatedApdu[2] = 0x04;
        truncatedApdu[3] = 0x00;
        // Extended Lc = 256
        truncatedApdu[4] = 0x00;
        truncatedApdu[5] = 0x01;
        truncatedApdu[6] = 0x00;

        try {
            ApduCommand.readFrom(truncatedApdu, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`byteArray` length must be greater or equal 263", exception.getMessage());
        }
    }
}
