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

/**
 * Utility class for APDU data manipulation.
 * <p>
 * This class provides methods to convert between integers and byte arrays,
 * particularly for handling Lc (Length of Command) and Le (Length of Expected response) fields
 * which have specific encoding rules (short vs extended length).
 * </p>
 */
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

    /**
     * Calculates the size of the byte array required to represent the given Lc or Le value.
     * <p>
     * If the value is less than or equal to 255, it requires 1 byte.
     * If the value is greater than 255, it requires 3 bytes (0x00 followed by 2 bytes of length).
     * </p>
     *
     * @param value The integer value of Lc or Le.
     * @return The size of the byte array (1 or 3).
     * @throws IllegalArgumentException If the value is greater than 65535.
     */
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

    /**
     * Converts an integer value to a byte array for Lc or Le fields.
     *
     * @param value The integer value to convert.
     * @return A byte array representing the value.
     * @throws IllegalArgumentException If the value is greater than 65535.
     * @deprecated Use {@link #intToLcBytes(int, boolean)} or {@link #intToLeBytes(int, boolean, boolean)} instead.
     */
    @Deprecated
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

    /**
     * Converts an integer value to a byte array for Lc field.
     *
     * @param value         The integer value to convert.
     * @param forceExtended Whether to force extended length encoding (3 bytes) even if value <= 255.
     * @return A byte array representing the value.
     */
    static byte[] intToLcBytes(int value, boolean forceExtended) {
        if ((value & MASK_INVALID_BYTES) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals " + MAX_LC_OR_LE_VALUE);
        }

        if (forceExtended || (value & MASK_GREATER_1BYTE) != 0) {
            // Extended Lc is always 3 bytes: 00 XX XX
            byte[] result = new byte[3];
            // result[0] = 0x0;
            result[1] = (byte) ((value & MASK_2ND_BYTE) >> 8);
            result[2] = (byte) (value & MASK_1ST_BYTE);
            return result;
        } else {
            // Short Lc: 1 byte
            byte[] result = new byte[1];
            result[0] = (byte) value;
            return result;
        }
    }

    /**
     * Converts an integer value to a byte array for Le field.
     *
     * @param value         The integer value to convert.
     * @param forceExtended Whether to force extended length encoding.
     * @param hasData       Whether the APDU command has data (determines Case 4 vs Case 2 extended format).
     * @return A byte array representing the value.
     */
    static byte[] intToLeBytes(int value, boolean forceExtended, boolean hasData) {
        if ((value & MASK_INVALID_BYTES) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals " + MAX_LC_OR_LE_VALUE);
        }

        boolean isExtended = forceExtended || (value & MASK_GREATER_1BYTE) != 0;

        if (isExtended) {
            if (hasData) {
                // Case 4e: Le is 2 bytes (XX XX) - No 00 prefix
                byte[] result = new byte[2];
                result[0] = (byte) ((value & MASK_2ND_BYTE) >> 8);
                result[1] = (byte) (value & MASK_1ST_BYTE);
                return result;
            } else {
                // Case 2e: Le is 3 bytes (00 XX XX)
                byte[] result = new byte[3];
                // result[0] = 0x0;
                result[1] = (byte) ((value & MASK_2ND_BYTE) >> 8);
                result[2] = (byte) (value & MASK_1ST_BYTE);
                return result;
            }
        } else {
            // Short Le: 1 byte
            byte[] result = new byte[1];
            result[0] = (byte) value;
            return result;
        }
    }

    /**
     * Reads Le field bytes from the byte array.
     *
     * @param byteArray    The source byte array.
     * @param offset       The offset to start reading from.
     * @param isLcExtended Whether the Lc field was extended (determines Le format in Case 4).
     * @return A byte array containing the Le bytes.
     */
    static byte[] readByteArrayForLe(byte[] byteArray, int offset, boolean isLcExtended) {
        if (byteArray == null) {
            throw new IllegalArgumentException("`byteArray` must not be null.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
        }

        if (isLcExtended) {
            // Case 4e: Le is 2 bytes
            if (byteArray.length < offset + 2) {
                throw new IllegalArgumentException("`byteArray` length must be greater or equal " + (offset + 2));
            }
            byte[] le = new byte[2];
            le[0] = byteArray[offset];
            le[1] = byteArray[offset + 1];
            return le;
        } else {
            // Case 4s: Le is 1 byte
            if (byteArray.length <= offset) {
                throw new IllegalArgumentException("`byteArray` length must be greater than " + offset);
            }
            return new byte[]{byteArray[offset]};
        }
    }

    /**
     * Converts a byte to an unsigned integer.
     *
     * @param value The byte value.
     * @return The unsigned integer representation.
     */
    public static int convertByteToInt(byte value) {
        return ((int) value) & MASK_1ST_BYTE;
    }

    /**
     * Converts an integer to a byte, ensuring it fits in one byte.
     *
     * @param value The integer value.
     * @return The byte representation.
     * @throws IllegalArgumentException If the value is greater than 255 (0xFF).
     */
    public static byte convertIntToByte(int value) {
        if ((value & MASK_GREATER_1BYTE) != 0) {
            throw new IllegalArgumentException("`value` must be less or equals 1 byte.");
        }

        return (byte) (value & MASK_1ST_BYTE);

    }

    /**
     * Reads a byte array from a larger array that represents an Lc or Le field.
     *
     * @param byteArray The source byte array.
     * @param offset    The offset to start reading from.
     * @return A byte array containing the Lc or Le bytes.
     * @throws IllegalArgumentException If `byteArray` is null, `offset` is negative, or `byteArray` is too short.
     */
    public static byte[] readByteArrayForLcOrLe(byte[] byteArray, int offset) {
        if (byteArray == null) {
            throw new IllegalArgumentException("`byteArray` must not be null.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
        }
        if (byteArray.length <= offset) {
            throw new IllegalArgumentException("`byteArray` length must be greater than " + offset);
        }

        byte byte1st = byteArray[offset];
        boolean hasMoreByte = byteArray.length > (offset + 1);
        if (byte1st == 0x00 && hasMoreByte) {
            if (byteArray.length <= (offset + 2)) {
                throw new IllegalArgumentException("`byteArray` length must be greater than " + (offset + 2));
            }

            byte[] bytes = new byte[3];
            bytes[1] = byteArray[offset + 1];
            bytes[2] = byteArray[offset + 2];
            return bytes;
        } else {
            return new byte[]{byte1st};
        }
    }

    /**
     * Converts a byte array representing Lc or Le into an integer.
     *
     * @param byteArray The byte array (length 1 or 3).
     * @return The integer value.
     * @throws IllegalArgumentException If `byteArray` is null, empty, or has an invalid length (not 1 or 3).
     */
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
