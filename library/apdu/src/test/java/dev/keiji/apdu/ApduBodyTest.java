package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ApduBodyTest {
    private final Random rand = new Random(System.currentTimeMillis());

    @Test
    public void createBodyTest1() {
        byte[] expected = new byte[]{(byte) 0xFF};

        int expectedSize = 1;

        ApduCommand.Body body = new ApduCommand.Body(null, 0xFF, false);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[body.size()];
        body.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTestException0() {

        ApduCommand.Body body = new ApduCommand.Body(null, 0xFF, false);

        byte[] arrayLengthNotEnough = new byte[body.size() - 1];

        try {
            body.writeTo(arrayLengthNotEnough, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTestException1() {

        ApduCommand.Body body = new ApduCommand.Body(null, 0xFF, false);

        byte[] arrayLengthNotEnough = new byte[body.size()];

        try {
            body.writeTo(arrayLengthNotEnough, -1);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTestException2() {
        try {
            ApduCommand.Body body = new ApduCommand.Body(null, 0x1FF, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTest2() {
        byte[] expected = new byte[]{0x00, 0x01, (byte) 0xFF};

        int expectedSize = 3;

        ApduCommand.Body body = new ApduCommand.Body(null, 0x1FF, true);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[body.size()];
        body.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTest3() {
        byte[] expected = new byte[]{0x01, 0x77};
        byte[] data = new byte[]{0x77};

        int expectedSize = 2;

        ApduCommand.Body body = new ApduCommand.Body(data, (Integer) null, false);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[body.size()];
        body.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTest4() {
        byte[] expected = new byte[]{0x01, 0x77, (byte) 0xFF};
        byte[] data = new byte[]{0x77};

        int expectedSize = 3;

        ApduCommand.Body body = new ApduCommand.Body(data, 0xFF, false);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[body.size()];
        body.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTestException3() {
        byte[] data = new byte[256];

        try {
            ApduCommand.Body body = new ApduCommand.Body(data, 0xFF, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTest5() throws IOException {
        byte[] dataSize = new byte[]{0x00, 0x01, 0x00}; // 256

        byte[] data = new byte[256];
        rand.nextBytes(data);

        // Case 4e with Le=255. Even if Le fits in 1 byte, Extended format requires 2 bytes Le field.
        byte[] le = new byte[]{0x00, (byte) 0xFF};

        int expectedSize = 3 + 256 + 2;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(dataSize);
        expected.write(data);
        expected.write(le);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand.Body body = new ApduCommand.Body(data, 0xFF, true);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[body.size()];
        body.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void createBodyTestException4() {
        byte[] data = new byte[0xFFFF + 1];

        try {
            ApduCommand.Body body = new ApduCommand.Body(data, 0xFF, true);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTestException5() {
        byte[] data = new byte[1];

        try {
            ApduCommand.Body body = new ApduCommand.Body(data, 0x1FF, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTest6() {
        // Case 4e: Data=1 byte (Short range), Le=511 (Extended range).
        // Since extendedField=true, Lc is forced to Extended format (3 bytes).
        // Lc: 00 00 01
        // Data: 77
        // Le: 01 FF (2 bytes)
        byte[] expected = new byte[]{0x00, 0x00, 0x01, 0x77, 0x01, (byte) 0xFF};
        byte[] data = new byte[]{0x77};

        int expectedSize = 6;

        ApduCommand.Body body = new ApduCommand.Body(data, 0x1FF, true);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[body.size()];
        body.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTestException6() {
        byte[] data = new byte[1];
        int ne = 0xFFFF + 1;

        try {
            ApduCommand.Body body = new ApduCommand.Body(data, ne, true);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
