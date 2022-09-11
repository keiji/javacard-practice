package dev.keiji.apdu;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void calcByteArraySizeForLcOrLeTest1() {
        int size = Utils.calcByteArraySizeForLcOrLe(0x00_000000);
        Assertions.assertEquals(1, size);
    }

    @Test
    public void calcByteArraySizeForLcOrLeTest2() {
        int size = Utils.calcByteArraySizeForLcOrLe(0x00_0000FF);
        Assertions.assertEquals(1, size);
    }

    @Test
    public void calcByteArraySizeForLcOrLeTest3() {
        int size = Utils.calcByteArraySizeForLcOrLe(0x00_0001FF);
        Assertions.assertEquals(3, size);
    }

    @Test
    public void calcByteArraySizeForLcOrLeTest4() {
        int size = Utils.calcByteArraySizeForLcOrLe(0x00_00FFFF);
        Assertions.assertEquals(3, size);
    }

    @Test
    public void calcByteArraySizeForLcOrLeTest5() {
        try {
            int size = Utils.calcByteArraySizeForLcOrLe(0x00_01FFFF);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void calcByteArraySizeForLcOrLeTest6() {
        try {
            int size = Utils.calcByteArraySizeForLcOrLe(0x01_000000);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void calcByteArraySizeForLcOrLeTest7() {
        try {
            int size = Utils.calcByteArraySizeForLcOrLe(0xFF_FFFFFF);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void integerToByteArrayForLcOrLeTest1() {
        byte[] result = Utils.integerToByteArrayForLcOrLe(0x00_000000);
        assertEquals(1, result.length);
        assertArrayEquals(new byte[]{0x00}, result);
    }

    @Test
    public void integerToByteArrayForLcOrLeTest2() {
        byte[] expected = new byte[]{(byte) 0xFF};
        byte[] result = Utils.integerToByteArrayForLcOrLe(0x00_0000FF);
        assertEquals(1, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void integerToByteArrayForLcOrLeTest3() {
        byte[] expected = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0xFF};
        byte[] result = Utils.integerToByteArrayForLcOrLe(0x00_0001FF);
        assertEquals(3, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void integerToByteArrayForLcOrLeTest4() {
        byte[] expected = new byte[]{(byte) 0x00, (byte) 0xFF, (byte) 0xFF};
        byte[] result = Utils.integerToByteArrayForLcOrLe(0x00_00FFFF);
        assertEquals(3, result.length);
        assertArrayEquals(expected, result);
    }

    @Test
    public void integerToByteArrayForLcOrLeTest5() {
        try {
            byte[] result = Utils.integerToByteArrayForLcOrLe(0x00_01FFFF);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void integerToByteArrayForLcOrLeTest6() {
        try {
            byte[] result = Utils.integerToByteArrayForLcOrLe(0x01_FFFFFF);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void integerToByteArrayForLcOrLeTest7() {
        try {
            byte[] result = Utils.integerToByteArrayForLcOrLe(0xFF_FFFFFF);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void convertByteToIntTest1() {
        int expected = 0;
        byte value = 0;
        int actual = Utils.convertByteToInt(value);
        assertEquals(expected, actual);
    }

    @Test
    public void convertByteToIntTest2() {
        int expected = 1;
        byte value = 1;
        int actual = Utils.convertByteToInt(value);
        assertEquals(expected, actual);
    }

    @Test
    public void convertByteToIntTest3() {
        int expected = 0xFF;
        byte value = (byte) 0xFF;

        int actual1 = value;
        assertNotEquals(expected, actual1);

        int actual2 = Utils.convertByteToInt(value);
        assertEquals(expected, actual2);
    }

    @Test
    public void readByteArrayForLcOrLeTest1() {
        byte[] byteArray = new byte[] {(byte) 0xFF};
        int offset = 0;

        byte[] expected = new byte[] {(byte) 0xFF};

        byte[] actual = Utils.readByteArrayForLcOrLe(byteArray, offset);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void readByteArrayForLcOrLeTest1_Exception1() {
        try {
            byte[] actual = Utils.readByteArrayForLcOrLe(null, 0);
            fail();
        } catch(IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readByteArrayForLcOrLeTest1_Exception2() {
        byte[] emptyData = new byte[0];
        try {
            byte[] actual = Utils.readByteArrayForLcOrLe(emptyData, 0);
            fail();
        } catch(IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readByteArrayForLcOrLeTest1_Exception3() {
        try {
            byte[] actual = Utils.readByteArrayForLcOrLe(new byte[1], -1);
            fail();
        } catch(IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readByteArrayForLcOrLeTest1_Exception4() {
        try {
            byte[] actual = Utils.readByteArrayForLcOrLe(new byte[1], 1);
            fail();
        } catch(IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void readByteArrayForLcOrLeTest2() {
        byte[] byteArray = new byte[] {0x00, 0x01, (byte) 0xFF};
        int offset = 0;

        byte[] expected = new byte[] {0x00, 0x01, (byte) 0xFF};

        byte[] actual = Utils.readByteArrayForLcOrLe(byteArray, offset);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void convertLcOrLeBytesToIntTest1() {
        int expected = 0xFF;
        byte[] byteArray = new byte[]{(byte) 0xFF};

        int actual = Utils.convertLcOrLeBytesToInt(byteArray);
        assertEquals(expected, actual);
    }

    @Test
    public void convertLcOrLeBytesToIntTest2() {
        int expected = 0xFFFF;
        byte[] byteArray = new byte[]{0x00, (byte) 0xFF, (byte) 0xFF};

        int actual = Utils.convertLcOrLeBytesToInt(byteArray);
        assertEquals(expected, actual);
    }

    @Test
    public void convertLcOrLeBytesToIntTest3() {
        int expected = 0x1234;
        byte[] byteArray = new byte[]{0x00, (byte) 0x12, (byte) 0x34};

        int actual = Utils.convertLcOrLeBytesToInt(byteArray);
        assertEquals(expected, actual);
    }

    @Test
    public void convertLcOrLeBytesToIntTest_Exception1() {
        byte[] byteArray = new byte[]{(byte) 0xFF, (byte) 0xFF};

        try {
            int actual = Utils.convertLcOrLeBytesToInt(byteArray);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }

    @Test
    public void convertLcOrLeBytesToIntTest_Exception2() {
        try {
            int actual = Utils.convertLcOrLeBytesToInt(null);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
        }
    }
}
