package dev.keiji.apdu;

class Utils {
    private static final int FILTER_GREATER_1BYTE = 0xFFFFFF00;
    private static final int FILTER_1ST_BYTE = 0x000000FF;
    private static final int FILTER_2ND_BYTE = 0x0000FF00;
    private static final int FILTER_3RD_BYTE = 0x00FF0000;
    private static final int FILTER_4TH_BYTE = 0xFF000000;

    private Utils() {
    }

    static int calcByteArraySizeForLcOrLe(int value) {
        if ((value & FILTER_4TH_BYTE) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals 3 bytes.");
        }

        if ((value & FILTER_GREATER_1BYTE) == 0) {
            return 1;
        } else {
            return 3;
        }
    }

    static byte[] integerToByteArrayForLcOrLe(int value) {
        if ((value & FILTER_4TH_BYTE) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals 3 bytes.");
        }

        int resultArraySize = calcByteArraySizeForLcOrLe(value);
        if (resultArraySize == 1) {
            byte[] result = new byte[1];
            result[0] = (byte) value;
            return result;
        } else {
            byte[] result = new byte[3];
            result[0] = (byte) (value & FILTER_1ST_BYTE);
            result[1] = (byte) ((value & FILTER_2ND_BYTE) >> 8);
            result[2] = (byte) ((value & FILTER_3RD_BYTE) >> 16);
            return result;
        }
    }
}
