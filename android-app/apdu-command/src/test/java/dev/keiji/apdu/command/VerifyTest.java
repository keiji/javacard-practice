package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class VerifyTest {

    private final Random rand = new Random();

    @Test
    public void test1() {
        byte[] expected = new byte[]{0x00, 0x20, (byte) 0x00, (byte) 0x00};

        Verify command = new Verify(
                0x00,
                Verify.P2.GLOBAL,
                null
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test2() throws IOException {
        byte[] header = new byte[]{0x00, 0x20, (byte) 0x00, (byte) 0x00};
        byte dataSize = 16;
        byte[] data = new byte[16];

        rand.nextBytes(data);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        byte[] expectedByteArray = expected.toByteArray();

        Verify command = new Verify(
                0x00,
                Verify.P2.GLOBAL,
                data
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void test2_exception1() {
        byte[] emptyData = new byte[0];

        try {
            Verify command = new Verify(
                    0x00,
                    Verify.P2.GLOBAL,
                    emptyData
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test2_exception2() {
        byte[] hugeData = new byte[256];

        try {
            Verify command = new Verify(
                    0x00,
                    Verify.P2.GLOBAL,
                    hugeData
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
    @Test
    public void test3() throws IOException {
        byte[] header = new byte[]{0x00, 0x20, (byte) 0x00, (byte) 0b100_00000};
        byte dataSize = 16;
        byte[] data = new byte[16];

        rand.nextBytes(data);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        byte[] expectedByteArray = expected.toByteArray();

        Verify command = new Verify(
                0x00,
                Verify.P2.SPECIFIC,
                data
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void test4() throws IOException {
        byte[] header = new byte[]{0x00, 0x20, (byte) 0x00, (byte) 0b100_11111};
        byte dataSize = 16;
        byte[] data = new byte[16];

        rand.nextBytes(data);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        byte[] expectedByteArray = expected.toByteArray();

        Verify command = new Verify(
                0x00,
                Verify.P2.SPECIFIC,
                data,
                0b000_11111
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void test4_exception1() {
        byte[] data = new byte[16];

        try {
            Verify command = new Verify(
                    0x00,
                    Verify.P2.GLOBAL,
                    data,
                    0b001_00000
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
