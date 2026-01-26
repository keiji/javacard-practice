package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class ApduResponseTest {

    @Test
    public void test1() {
        byte[] rawData = new byte[]{0x01, (byte) 0xFF};

        ApduResponse apduResponse = new ApduResponse(rawData);

        assertEquals(0x01, apduResponse.getStatusWord1());
        assertEquals(0xFF, apduResponse.getStatusWord2());

        byte[] bytes = new byte[apduResponse.size()];
        apduResponse.writeTo(bytes, 0);

        assertArrayEquals(rawData, bytes);
    }

    @Test
    public void test2() throws IOException {
        byte[] statusWords = new byte[]{0x01, (byte) 0xFF};
        byte[] data = new byte[256];
        new Random(System.currentTimeMillis()).nextBytes(data);

        ByteArrayOutputStream rawDataOutputStream = new ByteArrayOutputStream();
        rawDataOutputStream.write(data);
        rawDataOutputStream.write(statusWords);

        byte[] rawData = rawDataOutputStream.toByteArray();

        ApduResponse apduResponse = new ApduResponse(rawData);

        assertEquals(0x01, apduResponse.getStatusWord1());
        assertEquals(0xFF, apduResponse.getStatusWord2());
        assertArrayEquals(data, apduResponse.getData());

        byte[] bytes = new byte[apduResponse.size()];
        apduResponse.writeTo(bytes, 0);

        assertArrayEquals(rawData, bytes);
    }

    @Test
    public void test1_exception1() {
        byte[] rawData = new byte[]{0x01, (byte) 0xFF};

        ApduResponse apduResponse = new ApduResponse(rawData);

        byte[] bytes = new byte[apduResponse.size()];

        try {
            apduResponse.writeTo(bytes, -1);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test1_exception2() {
        byte[] rawData = new byte[]{0x01, (byte) 0xFF};

        ApduResponse apduResponse = new ApduResponse(rawData);

        byte[] bytes = new byte[apduResponse.size() - 1];

        try {
            apduResponse.writeTo(bytes, 0);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void testConstructors() {
        ApduResponse response1 = new ApduResponse(0x90, 0x00);
        assertEquals(0x90, response1.getStatusWord1());
        assertEquals(0x00, response1.getStatusWord2());
        assertEquals(0, response1.getData().length);

        byte[] data = new byte[]{0x01, 0x02, 0x03};
        ApduResponse response2 = new ApduResponse(0x90, 0x00, data);
        assertEquals(0x90, response2.getStatusWord1());
        assertEquals(0x00, response2.getStatusWord2());
        assertArrayEquals(data, response2.getData());
    }

    @Test
    public void testGetBytes() {
        byte[] data = new byte[]{0x01, 0x02};
        ApduResponse response = new ApduResponse(0x90, 0x00, data);
        byte[] expected = new byte[]{0x01, 0x02, (byte) 0x90, 0x00};
        assertArrayEquals(expected, response.getBytes());
    }

    @Test
    public void testWriteTo() {
        byte[] data = new byte[]{0x01, 0x02};
        ApduResponse response = new ApduResponse(0x90, 0x00, data);
        byte[] buffer = new byte[4];
        response.writeTo(buffer);
        byte[] expected = new byte[]{0x01, 0x02, (byte) 0x90, 0x00};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testEqualsAndHashCode() {
        ApduResponse response1 = new ApduResponse(0x90, 0x00, new byte[]{0x01});
        ApduResponse response2 = new ApduResponse(0x90, 0x00, new byte[]{0x01});
        ApduResponse response3 = new ApduResponse(0x61, 0x00, new byte[]{0x01});
        ApduResponse response4 = new ApduResponse(0x90, 0x00, new byte[]{0x02});

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        assertNotEquals(response1, response3);
        assertNotEquals(response1, response4);
        assertNotEquals(response1, new Object());
        assertNotEquals(response1, null);
    }

    @Test
    public void testConstructor_Exception_NullData() {
        try {
            new ApduResponse(0x90, 0x00, null);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`data` must not be null.", exception.getMessage());
        }
    }
}
