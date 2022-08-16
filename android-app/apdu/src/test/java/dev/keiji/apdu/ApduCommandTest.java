package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ApduCommandTest {

    private final Random rand = new Random(System.currentTimeMillis());

    @Test
    public void createCase1Test1() {
        byte[] expected = new byte[]{0x01, 0x02, 0x03, 0x04};

        int expectedSize = 4;

        ApduCommand apduCommand = ApduCommand.createCase1(0x01, 0x02, 0x03, 0x04);
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
        assertArrayEquals(expected, apduCommand.getBytes());
    }

    @Test
    public void createCase1TestException0() {
        ApduCommand apduCommand = ApduCommand.createCase1(0x01, 0x02, 0x03, 0x04);

        byte[] arrayLengthNotEnough = new byte[apduCommand.size() - 1];

        try {
            apduCommand.writeTo(arrayLengthNotEnough, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase2Test1() {
        byte[] expected = new byte[]{0x01, 0x02, 0x03, 0x04, (byte) 0xFF};

        int expectedSize = 4 + 1;

        ApduCommand apduCommand = ApduCommand.createCase2(
                0x01, 0x02, 0x03, 0x04,
                0xFF, false
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expected, actual);
        assertArrayEquals(expected, apduCommand.getBytes());
    }

    @Test
    public void createCase2TestException0() {
        ApduCommand apduCommand = ApduCommand.createCase2(
                0x01, 0x02, 0x03, 0x04,
                0xFF, false
        );

        byte[] arrayLengthNotEnough = new byte[apduCommand.size() - 1];

        try {
            apduCommand.writeTo(arrayLengthNotEnough, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase3Test1() throws IOException {
        byte[] header = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] dataSize = new byte[]{(byte) 0xFF}; // 255
        byte[] data = new byte[255];
        rand.nextBytes(data);

        int expectedSize = 4 + 1 + 255;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand apduCommand = ApduCommand.createCase3(
                0x01, 0x02, 0x03, 0x04,
                data, false
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
        assertArrayEquals(expectedByteArray, apduCommand.getBytes());
    }

    @Test
    public void createCase3TestException0() {
        byte[] data = new byte[255];
        rand.nextBytes(data);

        ApduCommand apduCommand = ApduCommand.createCase3(
                0x01, 0x02, 0x03, 0x04,
                data, false
        );

        byte[] arrayLengthNotEnough = new byte[apduCommand.size() - 1];

        try {
            apduCommand.writeTo(arrayLengthNotEnough, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase3Test2() throws IOException {
        byte[] header = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] dataSize = new byte[]{0x00, 0x01, 0x00}; // 256
        byte[] data = new byte[256];
        rand.nextBytes(data);

        int expectedSize = 4 + 3 + 256;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand apduCommand = ApduCommand.createCase3(
                0x01, 0x02, 0x03, 0x04,
                data, true
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
        assertArrayEquals(expectedByteArray, apduCommand.getBytes());
    }

    @Test
    public void createCase3Test3() throws IOException {
        byte[] header = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] dataSize = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0x00}; // 65,535
        byte[] data = new byte[0x00_00FFFF];
        rand.nextBytes(data);

        int expectedSize = 4 + 3 + 65535;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand apduCommand = ApduCommand.createCase3(
                0x01, 0x02, 0x03, 0x04,
                data, true
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
        assertArrayEquals(expectedByteArray, apduCommand.getBytes());
    }

    @Test
    public void createCase3TestException1() {
        byte[] data = new byte[0x00_00FFFF];
        rand.nextBytes(data);

        try {
            ApduCommand apduCommand = ApduCommand.createCase3(
                    0x01, 0x02, 0x03, 0x04,
                    data, false
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase3TestException2() {
        byte[] data = new byte[0x00_01FFFF];
        rand.nextBytes(data);

        try {
            ApduCommand apduCommand = ApduCommand.createCase3(
                    0x01, 0x02, 0x03, 0x04,
                    data, false
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase4Test1() throws IOException {
        byte[] header = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] dataSize = new byte[]{(byte) 0xFF}; // 255
        byte[] data = new byte[255];
        rand.nextBytes(data);

        byte[] le = new byte[]{0x01};

        int expectedSize = 4 + 1 + 255 + 1;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        expected.write(le);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand apduCommand = ApduCommand.createCase4(
                0x01, 0x02, 0x03, 0x04,
                data, 0x1, false
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
        assertArrayEquals(expectedByteArray, apduCommand.getBytes());
    }

    @Test
    public void createCase4TestException0() {
        byte[] data = new byte[255];
        rand.nextBytes(data);

        ApduCommand apduCommand = ApduCommand.createCase4(
                0x01, 0x02, 0x03, 0x04,
                data, 0x1, false
        );

        byte[] arrayLengthNotEnough = new byte[apduCommand.size() - 1];

        try {
            apduCommand.writeTo(arrayLengthNotEnough, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase4Test2() throws IOException {
        byte[] header = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] dataSize = new byte[]{0x00, 0x01, 0x00}; // 256
        byte[] data = new byte[256];
        rand.nextBytes(data);

        byte[] le = new byte[]{(byte) 0x00, 0x01, 0x00}; // 256

        int expectedSize = 4 + 3 + 256 + 3;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        expected.write(le);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand apduCommand = ApduCommand.createCase4(
                0x01, 0x02, 0x03, 0x04,
                data, 0x0100, true
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
        assertArrayEquals(expectedByteArray, apduCommand.getBytes());
    }

    @Test
    public void createCase4TestException1() throws IOException {
        byte[] data = new byte[256];
        rand.nextBytes(data);

        try {
            ApduCommand apduCommand = ApduCommand.createCase4(
                    0x01, 0x02, 0x03, 0x04,
                    data, 0x0100, false
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase4Test4() throws IOException {
        byte[] header = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] dataSize = new byte[]{0x00, 0x01, 0x00}; // 256
        byte[] data = new byte[256];
        rand.nextBytes(data);

        byte[] le = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0x00}; // 65,535

        int expectedSize = 4 + 3 + 256 + 3;

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(header);
        expected.write(dataSize);
        expected.write(data);
        expected.write(le);
        byte[] expectedByteArray = expected.toByteArray();

        ApduCommand apduCommand = ApduCommand.createCase4(
                0x01, 0x02, 0x03, 0x04,
                data, 0xFFFF, true
        );
        int actualSize = apduCommand.size();
        assertEquals(expectedSize, actualSize);

        byte[] actual = new byte[apduCommand.size()];
        apduCommand.writeTo(actual, 0);

        assertArrayEquals(expectedByteArray, actual);
        assertArrayEquals(expectedByteArray, apduCommand.getBytes());
    }

    @Test
    public void createCase4TestException2() {
        byte[] data = new byte[256];

        try {
            ApduCommand apduCommand = ApduCommand.createCase4(
                    0x01, 0x02, 0x03, 0x04,
                    data, 0x010000, false
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void createCase4TestException3() {
        byte[] data = new byte[256];

        try {
            ApduCommand apduCommand = ApduCommand.createCase4(
                    0x01, 0x02, 0x03, 0x04,
                    data, 0x010000, true
            );
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
