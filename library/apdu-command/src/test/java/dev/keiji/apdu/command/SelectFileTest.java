package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SelectFileTest {
    @Test
    public void test1() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xA4, 0x04, 0x04,
                0x07, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07
        };

        SelectFile selectFile = new SelectFile(
                0x00,
                new SelectFile.P1[]{SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME},
                new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD, SelectFile.P2.RETURN_FCP_TEMPLATE},
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07},
                false
        );
        byte[] actual = selectFile.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test2() {
        byte[] expected = new byte[]{
                0x00, (byte) 0xA4, 0x04, 0x04,
                0x07, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07
        };

        SelectFile selectFile = new SelectFile(
                0x00,
                // duplicated P1 option
                new SelectFile.P1[]{SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME, SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME},
                // duplicated P2 option
                new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD, SelectFile.P2.FIRST_RECORD, SelectFile.P2.RETURN_FCP_TEMPLATE},
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07},
                false
        );
        byte[] actual = selectFile.getBytes();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test3() throws IOException {
        byte[] hugeData = new byte[255 + 1];

        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write(new byte[]{0x00, (byte) 0xA4, 0x04, 0x04});
        expected.write(new byte[]{0x00, 0x01, 0x00}); // 255 + 1
        expected.write(hugeData);

        byte[] expectedByteArray = expected.toByteArray();

        SelectFile selectFile = new SelectFile(
                0x00,
                new SelectFile.P1[]{SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME},
                new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD, SelectFile.P2.RETURN_FCP_TEMPLATE},
                hugeData,
                true
        );
        byte[] actual = selectFile.getBytes();

        Assertions.assertArrayEquals(expectedByteArray, actual);
    }

    @Test
    public void test_exception0() {
        byte[] data = new byte[1];

        try {
            SelectFile selectFile = new SelectFile(
                    0x100,
                    new SelectFile.P1[]{SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME},
                    new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD, SelectFile.P2.RETURN_FCP_TEMPLATE},
                    data,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test_exception1() {
        byte[] hugeData = new byte[255 + 1];

        try {
            SelectFile selectFile = new SelectFile(
                    0x00,
                    new SelectFile.P1[]{SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME},
                    new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD, SelectFile.P2.RETURN_FCP_TEMPLATE},
                    hugeData,
                    false
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void test_exception2() {
        byte[] hugeData = new byte[0x10000];

        try {
            SelectFile selectFile = new SelectFile(
                    0x00,
                    new SelectFile.P1[]{SelectFile.P1.DIRECT_SELECTION_BY_DF_NAME},
                    new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD, SelectFile.P2.RETURN_FCP_TEMPLATE},
                    hugeData,
                    true
            );
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
