package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ComputeDigitalSignatureTest {

    private final Random rand = new Random();

    @Test
    public void test1() throws IOException {
        byte[] header = new byte[]{0x00, 0x2A, (byte) 0x9E, (byte) 0x9A};
        byte[] dataSize = new byte[]{(byte) 0xFF}; // 255
        byte[] data = new byte[255];
        byte[] ne = new byte[]{0x00};

        rand.nextBytes(data);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        expected.write(ne);
        byte[] expectedByteArray = expected.toByteArray();

        ComputeDigitalSignature command = new ComputeDigitalSignature(
                0x00,
                data,
                false
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void test1_exception1() {
        try {
            ComputeDigitalSignature command = new ComputeDigitalSignature(
                    0x00,
                    null,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test1_exception2() {
        byte[] hugeData = new byte[256];

        try {
            ComputeDigitalSignature command = new ComputeDigitalSignature(
                    0x00,
                    hugeData,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test2() throws IOException {
        byte[] header = new byte[]{0x00, 0x2A, (byte) 0x9E, (byte) 0x9A};
        byte[] dataSize = new byte[]{0x00, (byte) 0xFF, (byte) 0xFF}; // 65,535
        byte[] data = new byte[0xFFFF];
        byte[] ne = new byte[]{0x00};

        rand.nextBytes(data);

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        expected.write(ne);
        byte[] expectedByteArray = expected.toByteArray();

        ComputeDigitalSignature command = new ComputeDigitalSignature(
                0x00,
                data,
                true
        );
        byte[] actual = command.getBytes();

        Assertions.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void test2_exception1() {
        byte[] hugeData = new byte[0x10000];

        try {
            ComputeDigitalSignature command = new ComputeDigitalSignature(
                    0x00,
                    hugeData,
                    true
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
