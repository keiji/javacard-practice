package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReadBinaryTest {
    @Test
    public void test1() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, 0x00, 0x01, 0x02,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                1, 2,
                false
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test2() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, 0x01, 0x00, 0x02,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                256, 2,
                false
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test3() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, 0x00, 0x01, 0x00, 0x01, 0x00,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                1, 256,
                true
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test3_exception1() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    1, 256,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
