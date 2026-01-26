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

    @Test
    public void test4() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, 0b01111111, (byte) 0b11111111, 0x01,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                0b01111111_11111111, 1,
                false
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test4_exception1() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    0b11111111_11111111, 1,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test4_exception2() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    -1, 1,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testShortEfIdentifier1() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, (byte) 0b100_00000, 0x01, 0x02,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                0x00,
                1, 2,
                false
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testShortEfIdentifier2() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, (byte) 0b100_11111, (byte) 0b11111111, 0x02,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                0b000_11111,
                255, 2,
                false
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testShortEfIdentifier2_exception1() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    0b001_11111,
                    255, 2,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testShortEfIdentifier2_exception2() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    0b000_11111,
                    256, 2,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testShortEfIdentifier3() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, (byte) 0b100_11111, 0x01, 0x00, 0x01, 0x00,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                0b000_11111,
                1, 256,
                true
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testShortEfIdentifier3_exception1() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    0b000_11111,
                    1, 256,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testShortEfIdentifier4() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xB0, (byte) 0b100_11111, (byte) 0b11111111, 0x01,
        };

        ReadBinary readBinary = new ReadBinary(
                0x00,
                0b000_11111,
                0b00000000_11111111, 1,
                false
        );
        byte[] actual = readBinary.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testShortEfIdentifier4_exception1() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    0b000_11111,
                    0b00000001_00000000, 1,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testShortEfIdentifier4_exception2() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    0b000_11111,
                    -1, 1,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testShortEfIdentifier4_exception3() {
        try {
            ReadBinary readBinary = new ReadBinary(
                    0x00,
                    -1,
                    1, 1,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
