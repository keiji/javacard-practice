package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class ApduHeaderTest {

    @Test
    public void createHeaderTest1() {
        byte[] expected = new byte[]{(byte) 0xFF, 0x02, 0x03, 0x04};

        ApduCommand.Header header = new ApduCommand.Header(
                0xFF, 0x02, 0x03, 0x04
        );
        int actualSize = header.size();
        assertEquals(4, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(header.size());
        header.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createHeaderTest2() {
        byte[] expected = new byte[]{0x01, (byte) 0xFF, 0x03, 0x04};

        ApduCommand.Header header = new ApduCommand.Header(
                0x01, 0xFF, 0x03, 0x04
        );
        int actualSize = header.size();
        assertEquals(4, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(header.size());
        header.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createHeaderTest3() {
        byte[] expected = new byte[]{0x01, 0x02, (byte) 0xFF, 0x04};

        ApduCommand.Header header = new ApduCommand.Header(
                0x01, 0x02, 0xFF, 0x04
        );
        int actualSize = header.size();
        assertEquals(4, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(header.size());
        header.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createHeaderTest4() {
        byte[] expected = new byte[]{0x01, 0x02, 0x03, (byte) 0xFF};

        ApduCommand.Header header = new ApduCommand.Header(
                0x01, 0x02, 0x03, 0xFF
        );
        int actualSize = header.size();
        assertEquals(4, actualSize);

        ByteBuffer bb = ByteBuffer.allocate(header.size());
        header.writeTo(bb);
        byte[] actual = bb.array();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void createHeaderTestException1() {
        try {
            ApduCommand.Header header = new ApduCommand.Header(
                    0x1FF, 0x02, 0x03, 0x04
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createHeaderTestException2() {
        try {
            ApduCommand.Header header = new ApduCommand.Header(
                    0x01, 0x1FF, 0x03, 0x04
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createHeaderTestException3() {
        try {
            ApduCommand.Header header = new ApduCommand.Header(
                    0x01, 0x02, 0x1FF, 0x04
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createHeaderTestException4() {
        try {
            ApduCommand.Header header = new ApduCommand.Header(
                    0x01, 0x02, 0x03, 0x1FF
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
