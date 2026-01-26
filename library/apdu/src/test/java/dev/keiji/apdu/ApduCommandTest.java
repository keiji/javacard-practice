package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        byte[] dataSize = new byte[]{(byte) 0x00, (byte) 0xFF, (byte) 0xFF}; // 65,535
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

        byte[] le = new byte[]{(byte) 0x00, (byte) 0xFF, (byte) 0xFF}; // 65,535

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

    @Test
    public void testEqualsAndHashCode() {
        ApduCommand cmd1 = ApduCommand.createCase1(0x00, 0xA4, 0x00, 0x00);
        ApduCommand cmd2 = ApduCommand.createCase1(0x00, 0xA4, 0x00, 0x00);
        ApduCommand cmd3 = ApduCommand.createCase1(0x00, 0xA4, 0x04, 0x00);

        assertEquals(cmd1, cmd2);
        assertEquals(cmd1.hashCode(), cmd2.hashCode());

        assertNotEquals(cmd1, cmd3);
        assertNotEquals(cmd1, null);
        assertNotEquals(cmd1, new Object());
    }

    @Test
    public void testHeaderConstructorAndEquals() {
        ApduCommand.Header header1 = new ApduCommand.Header((byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x00);
        ApduCommand.Header header2 = new ApduCommand.Header(0x00, 0xA4, 0x00, 0x00);

        assertEquals(header1, header2);
        assertEquals(header1.hashCode(), header2.hashCode());

        ApduCommand.Header header3 = new ApduCommand.Header(0x00, 0xB0, 0x00, 0x00);
        assertNotEquals(header1, header3);
        assertNotEquals(header1, null);
        assertNotEquals(header1, new Object());
    }

    @Test
    public void testBodyGettersAndEquals() {
        byte[] data = new byte[]{0x01, 0x02};
        ApduCommand.Body body1 = new ApduCommand.Body(data, 0x00, false);
        ApduCommand.Body body2 = new ApduCommand.Body(data, 0x00, false);

        assertEquals(body1, body2);
        assertEquals(body1.hashCode(), body2.hashCode());

        assertNotNull(body1.getLc());
        assertEquals(2, body1.getLc());
        assertEquals(0, body1.getLe()); // le was 0x00

        ApduCommand.Body body3 = new ApduCommand.Body(data, 0x01, false);
        assertNotEquals(body1, body3);
        assertNotEquals(body1, null);
        assertNotEquals(body1, new Object());
    }
}
