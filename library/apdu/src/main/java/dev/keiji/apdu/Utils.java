/*
 * Copyright (C) 2022 ARIYAMA Keiji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.keiji.apdu;

final class Utils {
    private static final int MASK_GREATER_1BYTE = 0xFFFFFF00;
    private static final int MASK_1ST_BYTE = 0x000000FF;
    private static final int MASK_2ND_BYTE = 0x0000FF00;
    private static final int MASK_3RD_BYTE = 0x00FF0000;
    private static final int MASK_4TH_BYTE = 0xFF000000;

    private static final int MAX_LC_OR_LE_VALUE = 0x0000FFFF; // 65535
    private static final int MASK_INVALID_BYTES = MASK_3RD_BYTE | MASK_4TH_BYTE;

    private Utils() {
    }

    static int calcByteArraySizeForLcOrLe(int value) {
        if ((value & MASK_INVALID_BYTES) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals " + MAX_LC_OR_LE_VALUE);
        }

        if ((value & MASK_GREATER_1BYTE) == 0) {
            return 1;
        } else {
            return 3;
        }
    }

    static byte[] integerToByteArrayForLcOrLe(int value) {
        if ((value & MASK_INVALID_BYTES) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals " + MAX_LC_OR_LE_VALUE);
        }

        int resultArraySize = calcByteArraySizeForLcOrLe(value);
        if (resultArraySize == 1) {
            byte[] result = new byte[1];
            result[0] = (byte) value;
            return result;
        } else {
            byte[] result = new byte[3];
            // result[0] = 0x0;
            result[1] = (byte) ((value & MASK_2ND_BYTE) >> 8);
            result[2] = (byte) (value & MASK_1ST_BYTE);
            return result;
        }
    }

    public static int convertByteToInt(byte value) {
        return ((int) value) & MASK_1ST_BYTE;
    }

    public static byte convertIntToByte(int value) {
        if ((value & MASK_GREATER_1BYTE) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals 1 byte.");
        }

        return (byte) (value & MASK_1ST_BYTE);

    }

    public static byte[] readByteArrayForLcOrLe(byte[] byteArray, int offset) {
        if (byteArray == null) {
            throw new IllegalArgumentException("`byteArray` must not be null.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
        }
        if (byteArray.length <= offset) {
            throw new IllegalArgumentException("`byteArray` must be greater or equal " + (byteArray.length + offset));
        }

        byte byte1st = byteArray[offset];
        boolean hasMoreByte = byteArray.length > (offset + 1);
        if (byte1st == 0x00 && hasMoreByte) {
            byte[] bytes = new byte[3];
            bytes[1] = byteArray[offset + 1];
            bytes[2] = byteArray[offset + 2];
            return bytes;
        } else {
            return new byte[]{byte1st};
        }
    }

    public static int convertLcOrLeBytesToInt(byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("`byteArray` must not be null.");
        }
        if (byteArray.length == 0) {
            throw new IllegalArgumentException("`byteArray` must not be empty.");
        }

        if (byteArray.length == 1) {
            return ((int) byteArray[0] & MASK_1ST_BYTE);
        } else if (byteArray.length == 3) {
            return ((int) byteArray[1] & MASK_1ST_BYTE) << 8 | ((int) byteArray[2] & MASK_1ST_BYTE);
        } else {
            throw new IllegalArgumentException("`byteArray` length must be 1 or 3.");
        }
    }
}
