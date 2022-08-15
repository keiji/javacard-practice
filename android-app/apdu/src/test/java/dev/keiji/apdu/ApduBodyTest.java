package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

        ByteBuffer bb = ByteBuffer.allocate(body.size());
        body.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTestException1() {
        try {
            ApduCommand.Body body = new ApduCommand.Body(null, 0x1FF, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTest2() {
        byte[] expected = new byte[]{(byte) 0xFF, 0x01, 0x00};

        int expectedSize = 3;

        ApduCommand.Body body = new ApduCommand.Body(null, 0x1FF, true);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(body.size());
        body.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTest3() {
        byte[] expected = new byte[]{0x01, 0x77};
        byte[] data = new byte[]{0x77};

        int expectedSize = 2;

        ApduCommand.Body body = new ApduCommand.Body(data, null, false);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(body.size());
        body.writeTo(bb);
        byte[] actual = bb.array();

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

        ByteBuffer bb = ByteBuffer.allocate(body.size());
        body.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTestException2() {
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

        byte[] le = new byte[]{(byte) 0xFF};

        int expectedSize = 3 + 256 + 1;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(dataSize);
        expected.write(data);
        expected.write(le);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand.Body body = new ApduCommand.Body(data, 0xFF, true);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(body.size());
        body.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void createBodyTestException3() {
        byte[] data = new byte[0xFFFF + 1];

        try {
            ApduCommand.Body body = new ApduCommand.Body(data, 0xFF, true);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createBodyTestException4() {
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
        byte[] expected = new byte[]{0x01, 0x77, (byte) 0xFF, 0x01, 0x00};
        byte[] data = new byte[]{0x77};

        int expectedSize = 5;

        ApduCommand.Body body = new ApduCommand.Body(data, 0x1FF, true);
        int actualSize = body.size();
        assertEquals(expectedSize, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(body.size());
        body.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createBodyTestException5() {
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
