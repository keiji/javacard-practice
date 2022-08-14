package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

}
