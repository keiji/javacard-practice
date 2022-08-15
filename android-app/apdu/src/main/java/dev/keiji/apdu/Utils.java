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

    private Utils() {
    }

    static int calcByteArraySizeForLcOrLe(int value) {
        if ((value & MASK_4TH_BYTE) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals 3 bytes.");
        }
        if (value > MAX_LC_OR_LE_VALUE) {
            throw new IllegalArgumentException("`value` must be less or equals " + MAX_LC_OR_LE_VALUE);
        }

        if ((value & MASK_GREATER_1BYTE) == 0) {
            return 1;
        } else {
            return 3;
        }
    }

    static byte[] integerToByteArrayForLcOrLe(int value) {
        if ((value & MASK_4TH_BYTE) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals 3 bytes.");
        }
        if (value > MAX_LC_OR_LE_VALUE) {
            throw new IllegalArgumentException("`value` must be less or equals " + MAX_LC_OR_LE_VALUE);
        }

        int resultArraySize = calcByteArraySizeForLcOrLe(value);
        if (resultArraySize == 1) {
            byte[] result = new byte[1];
            result[0] = (byte) value;
            return result;
        } else {
            byte[] result = new byte[3];
            result[0] = (byte) (value & MASK_1ST_BYTE);
            result[1] = (byte) ((value & MASK_2ND_BYTE) >> 8);
            result[2] = (byte) ((value & MASK_3RD_BYTE) >> 16);
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
}
