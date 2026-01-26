package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.keiji.apdu.ApduResponse;

public class BaseResponseTest {
    private static class DummyResponse extends ApduResponse {
        public DummyResponse(byte[] rawData) {
            super(rawData);
        }
    }

    @Test
    public void test1() {
        byte[] responseByte = new byte[]{0x01, 0x02};

        DummyResponse response = new DummyResponse(responseByte);
        Assertions.assertEquals(0x01, response.getStatusWord1());
        Assertions.assertEquals(0x02, response.getStatusWord2());
    }

    @Test
    public void test1_exception0() {
        try {
            DummyResponse response = new DummyResponse(null);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test1_exception1() {
        byte[] responseByte = new byte[]{0x01};

        try {
            DummyResponse response = new DummyResponse(responseByte);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test2() {
        byte[] responseByte = new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
                (byte) 0xFE, (byte) 0xFF
        };

        DummyResponse response = new DummyResponse(responseByte);
        Assertions.assertArrayEquals(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05}, response.getData());
        Assertions.assertEquals(0xFE, response.getStatusWord1());
        Assertions.assertEquals(0xFF, response.getStatusWord2());
    }
}
