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
    public void intToLcBytesTest1_Short() {
        byte[] expected = new byte[] {0x0A};
        byte[] actual = Utils.intToLcBytes(0x0A, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLcBytesTest2_Extended() {
        byte[] expected = new byte[] {0x00, 0x01, 0x00};
        byte[] actual = Utils.intToLcBytes(0x100, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLcBytesTest3_ForceExtended() {
        byte[] expected = new byte[] {0x00, 0x00, 0x0A};
        byte[] actual = Utils.intToLcBytes(0x0A, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLeBytesTest1_Short() {
        byte[] expected = new byte[] {0x0A};
        byte[] actual = Utils.intToLeBytes(0x0A, false, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLeBytesTest1_Short_Case4() {
        byte[] expected = new byte[] {0x0A};
        byte[] actual = Utils.intToLeBytes(0x0A, false, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLeBytesTest2_Case2e() {
        byte[] expected = new byte[] {0x00, 0x01, 0x00};
        byte[] actual = Utils.intToLeBytes(0x100, false, false); // Auto extended
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLeBytesTest3_Case4e() {
        byte[] expected = new byte[] {0x01, 0x00};
        byte[] actual = Utils.intToLeBytes(0x100, false, true); // Auto extended
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLeBytesTest4_ForceExtended_Case2e() {
        byte[] expected = new byte[] {0x00, 0x00, 0x0A};
        byte[] actual = Utils.intToLeBytes(0x0A, true, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void intToLeBytesTest5_ForceExtended_Case4e() {
        byte[] expected = new byte[] {0x00, 0x0A};
        byte[] actual = Utils.intToLeBytes(0x0A, true, true);
        assertArrayEquals(expected, actual);
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

    @Test
    public void readByteArrayForLcOrLeTest_Exception5() {
        byte[] byteArray = new byte[] {0x00, 0x01}; // starts with 0x00, implies 3 bytes, but only has 2
        try {
            byte[] actual = Utils.readByteArrayForLcOrLe(byteArray, 0);
            fail();
        } catch(IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`byteArray` length must be greater than 2", exception.getMessage());
        }
    }

    @Test
    public void readByteArrayForLeTest_Exception1() {
        try {
            Utils.readByteArrayForLe(null, 0, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`byteArray` must not be null.", exception.getMessage());
        }
    }

    @Test
    public void readByteArrayForLeTest_Exception2() {
        try {
            Utils.readByteArrayForLe(new byte[1], -1, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`offset` value must be greater or equal 0.", exception.getMessage());
        }
    }

    @Test
    public void convertIntToByteTest_Exception() {
        try {
            Utils.convertIntToByte(256);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`value` must be less or equals 1 byte.", exception.getMessage());
        }
    }

    @Test
    public void intToLcBytesTest_Exception() {
        try {
            Utils.intToLcBytes(65536, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`value` must be less or equals 65535", exception.getMessage());
        }
    }

    @Test
    public void intToLeBytesTest_Exception() {
        try {
            Utils.intToLeBytes(65536, false, false);
            fail();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception);
            assertEquals("`value` must be less or equals 65535", exception.getMessage());
        }
    }
}
