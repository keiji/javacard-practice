package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetDataTest {
    @Test
    public void test1() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xCA, 0x00, 0x01, 0x00,
        };

        GetData getData = new GetData(
                0x00,
                new byte[]{
                        0x01,
                },
                0x00,
                false
        );
        byte[] actual = getData.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test2() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xCA, 0x01, 0x02, (byte) 0xFF,
        };

        GetData getData = new GetData(
                0x00,
                new byte[]{
                        0x01, 0x02,
                },
                255,
                false
        );
        byte[] actual = getData.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test3() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xCA, 0x00, 0x01,
                (byte) 0x00, (byte) 0x01, (byte) 0x00,
        };

        GetData getData = new GetData(
                0x00,
                new byte[]{
                        0x01
                },
                256,
                true
        );
        byte[] actual = getData.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test4() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xCA, 0x01, 0x02,
                (byte) 0x00, (byte) 0xFF, (byte) 0xFF,
        };

        GetData getData = new GetData(
                0x00,
                new byte[]{
                        0x01, 0x02,
                },
                0xFFFF,
                true
        );
        byte[] actual = getData.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_exception0() {
        try {
            GetData getData = new GetData(
                    0x00,
                    new byte[2],
                    256,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test_exception1() {
        try {
            GetData getData = new GetData(
                    0x00,
                    new byte[0],
                    0x00,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test_exception2() {
        try {
            GetData getData = new GetData(
                    0x00,
                    new byte[]{
                            0x01, 0x02, 0x03,
                    },
                    0x00,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
