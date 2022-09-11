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

import java.util.Arrays;
import java.util.Objects;

/**
 * Command APDU.
 */
public class ApduCommand {

    /**
     * Mandatory header.
     */
    public static class Header {

        /**
         * Class.
         * Class of instruction
         */
        public final byte cla;

        /**
         * Instruction.
         * Instruction code
         */
        public final byte ins;

        /**
         * Parameter 1.
         * Instruction parameter 1
         */
        public final byte p1;

        /**
         * Parameter 2.
         * Instruction parameter 2
         */
        public final byte p2;

        /**
         * Constructor.
         *
         * @param cla Class of instruction
         * @param ins Instruction code
         * @param p1  Instruction parameter 1
         * @param p2  Instruction parameter 2
         */
        public Header(int cla, int ins, int p1, int p2) {
            this.cla = Utils.convertIntToByte(cla);
            this.ins = Utils.convertIntToByte(ins);
            this.p1 = Utils.convertIntToByte(p1);
            this.p2 = Utils.convertIntToByte(p2);
        }

        /**
         * Number of bytes of the header.
         *
         * @return Returns the number of bytes of the header
         */
        public int size() {
            return 4;
        }

        /**
         * Write the header to the given ByteArray.
         *
         * @param byteArray The ByteArray to which this object should output
         * @param offset    The offset within the array of the first byte to be write
         */
        public void writeTo(byte[] byteArray, int offset) {
            if (offset < 0) {
                throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
            }

            int expectedLength = offset + size();
            if (byteArray.length < expectedLength) {
                throw new IllegalArgumentException("byteArray length must be greater than " + expectedLength);
            }

            byteArray[offset] = cla;
            byteArray[offset + 1] = ins;
            byteArray[offset + 2] = p1;
            byteArray[offset + 3] = p2;
        }

        public static Header readFrom(byte[] byteArray, int offset) {
            return new Header(
                    byteArray[offset] & 0xFF,
                    byteArray[offset + 1] & 0xFF,
                    byteArray[offset + 2] & 0xFF,
                    byteArray[offset + 3] & 0xFF
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Header header = (Header) o;
            return cla == header.cla && ins == header.ins && p1 == header.p1 && p2 == header.p2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(cla, ins, p1, p2);
        }
    }

    /**
     * Conditional body.
     */
    public static class Body {
        private static final int MAX_LC_LENGTH = 0x000000FF;
        private static final int MAX_LE_LENGTH = 0x000000FF;
        private static final int MAX_LC_EXTENDED_LENGTH = 0x0000FFFF;
        private static final int MAX_LE_EXTENDED_LENGTH = 0x0000FFFF;

        /**
         * Number of bytes present in the data field of the command.
         */
        public final byte[] lc;

        /**
         * String of bytes sent in the data field of the command.
         */
        public final byte[] data;

        /**
         * Maximum number of bytes expected in the data field of the response to the command.
         */
        public final byte[] le;

        private Body(byte[] lc, byte[] data, byte[] le) {
            this.lc = lc;
            this.data = data;
            this.le = le;
        }

        /**
         * Constructor.
         *
         * @param data                String of bytes sent in the data field of the command
         * @param le                  Maximum number of bytes expected in the data field of the response to the command
         * @param enableExtendedField Whether the APDU supports extended length
         */
        public Body(byte[] data, Integer le, boolean enableExtendedField) {
            if (data == null && le == null) {
                throw new IllegalArgumentException("Either `data` or `le` must not be null.");
            }

            if (data == null) {
                this.data = null;
                this.lc = null;
            } else if (!enableExtendedField && data.length > MAX_LC_LENGTH) {
                throw new IllegalArgumentException("`data` size must be less or equal than " + MAX_LC_LENGTH + " bytes.");
            } else if (enableExtendedField && data.length > MAX_LC_EXTENDED_LENGTH) {
                throw new IllegalArgumentException("`data` size must be less or equal than " + MAX_LC_EXTENDED_LENGTH + " bytes.");
            } else {
                this.data = data;
                this.lc = Utils.integerToByteArrayForLcOrLe(data.length);
            }

            if (le == null) {
                this.le = null;
            } else if (!enableExtendedField && le > MAX_LE_LENGTH) {
                throw new IllegalArgumentException("`le` must be less or equal than " + MAX_LE_LENGTH + " bytes.");
            } else if (enableExtendedField && le > MAX_LE_EXTENDED_LENGTH) {
                throw new IllegalArgumentException("`le` must be less or equal than " + MAX_LE_EXTENDED_LENGTH + " bytes.");
            } else {
                this.le = Utils.integerToByteArrayForLcOrLe(le);
            }
        }

        /**
         * Number of bytes of the body.
         *
         * @return Returns the number of bytes of the body
         */
        public int size() {
            int size = 0;
            if (lc != null) {
                size += lc.length;
            }
            if (data != null) {
                size += data.length;
            }
            if (le != null) {
                size += le.length;
            }
            return size;
        }

        /**
         * Write the body to the given ByteArray.
         *
         * @param byteArray The ByteArray to which this object should output
         * @param offset    The offset within the array of the first byte to be write
         */
        public void writeTo(byte[] byteArray, int offset) {
            if (offset < 0) {
                throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
            }

            int expectedLength = offset + size();
            if (byteArray.length < expectedLength) {
                throw new IllegalArgumentException("byteArray length must be greater than " + expectedLength);
            }

            int index = offset;
            if (lc != null) {
                System.arraycopy(lc, 0, byteArray, index, lc.length);
                index += lc.length;
            }
            if (data != null) {
                System.arraycopy(data, 0, byteArray, index, data.length);
                index += data.length;
            }
            if (le != null) {
                System.arraycopy(le, 0, byteArray, index, le.length);
                index += le.length;
            }
        }

        public static Body readFrom(byte[] byteArray, int offset) {
            byte[] lcOrLeBytes = Utils.readByteArrayForLcOrLe(byteArray, offset);
            int lcOrLeValue = Utils.convertLcOrLeBytesToInt(lcOrLeBytes);

            offset += lcOrLeBytes.length;

            boolean hasData = offset < byteArray.length;

            if (!hasData) {
                return new Body(
                        null,
                        null,
                        lcOrLeBytes
                );
            }

            byte[] data = new byte[lcOrLeValue];
            System.arraycopy(byteArray, offset, data, 0, lcOrLeValue);

            offset += data.length;

            byte[] leBytes = null;
            if (offset < byteArray.length) {
                leBytes = Utils.readByteArrayForLcOrLe(byteArray, offset);
            }

            return new Body(
                    lcOrLeBytes,
                    data,
                    leBytes
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Body body = (Body) o;
            return Arrays.equals(lc, body.lc) && Arrays.equals(data, body.data) && Arrays.equals(le, body.le);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(lc);
            result = 31 * result + Arrays.hashCode(data);
            result = 31 * result + Arrays.hashCode(le);
            return result;
        }
    }

    /**
     * Header(mandatory).
     */
    public final Header header;

    /**
     * Body(optional).
     */
    public final Body body;

    /**
     * Constructor.
     *
     * @param header Mandatory header
     * @param body   Conditional body
     */
    ApduCommand(Header header, Body body) {
        if (header == null) {
            throw new IllegalArgumentException("`header` must not be null.");
        }

        this.header = header;
        this.body = body;
    }

    /**
     * Number of bytes of the command.
     *
     * @return Returns the number of bytes of the command
     */
    public int size() {
        if (body != null) {
            return header.size() + body.size();
        }

        return header.size();
    }

    /**
     * Returns the byte array that this APDU command.
     *
     * @return The result byte array
     */
    public byte[] getBytes() {
        byte[] byteArray = new byte[size()];
        writeTo(byteArray);
        return byteArray;
    }

    public static ApduCommand readFrom(byte[] byteArray, int offset) {
        if (byteArray == null) {
            throw new IllegalArgumentException("`byteArray` must not be null.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
        }
        if (byteArray.length < offset + 4 /* Header length */) {
            throw new IllegalArgumentException("`byteArray` length must be greater or equal " + (offset + 4));
        }

        Header header = Header.readFrom(byteArray, offset);
        offset += 4;

        Body body = null;
        if (byteArray.length > offset) {
            body = Body.readFrom(byteArray, offset);
        }

        return new ApduCommand(header, body);
    }

    /**
     * Write this APDU command to the given ByteArray.
     *
     * @param byteArray The ByteArray to which this object should output
     */
    public void writeTo(byte[] byteArray) {
        writeTo(byteArray, 0);
    }

    /**
     * Write this APDU command to the given ByteArray.
     *
     * @param byteArray The ByteArray to which this object should output
     * @param offset    The offset within the array of the first byte to be write
     */
    public void writeTo(byte[] byteArray, int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("`offset` value must be greater or equal 0.");
        }

        int expectedLength = offset + size();
        if (byteArray.length < expectedLength) {
            throw new IllegalArgumentException("byteArray length must be greater than " + expectedLength + " bytes.");
        }

        int index = offset;
        header.writeTo(byteArray, index);
        index += header.size();

        if (body != null) {
            body.writeTo(byteArray, index);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApduCommand that = (ApduCommand) o;
        return header.equals(that.header) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body);
    }

    /**
     * Case 1.
     * Command data: No data
     * Expected response data: No data
     *
     * @param cla Class of instruction
     * @param ins Instruction code
     * @param p1  Instruction parameter 1
     * @param p2  Instruction parameter 2
     * @return ApduCommand object
     */
    public static ApduCommand createCase1(int cla, int ins, int p1, int p2) {
        return new ApduCommand(new Header(cla, ins, p1, p2), null);
    }

    /**
     * Case 2.
     * Command data: No data
     * Expected response data: Data
     *
     * @param cla                 Class of instruction
     * @param ins                 Instruction code
     * @param p1                  Instruction parameter 1
     * @param p2                  Instruction parameter 2
     * @param le                  Number of bytes present in the data field of the command
     * @param enableExtendedField Whether the APDU supports extended length
     * @return ApduCommand object
     */
    public static ApduCommand createCase2(
            int cla, int ins, int p1, int p2,
            int le, boolean enableExtendedField
    ) {
        return new ApduCommand(
                new Header(cla, ins, p1, p2),
                new Body(null, le, enableExtendedField)
        );
    }

    /**
     * Case 3.
     * Command data: Data
     * Expected response data: No data
     *
     * @param cla                 Class of instruction
     * @param ins                 Instruction code
     * @param p1                  Instruction parameter 1
     * @param p2                  Instruction parameter 2
     * @param data                String of bytes sent in the data field of the command
     * @param enableExtendedField Whether the APDU supports extended length
     * @return ApduCommand object
     */
    public static ApduCommand createCase3(
            int cla, int ins, int p1, int p2,
            byte[] data, boolean enableExtendedField
    ) {
        return new ApduCommand(
                new Header(cla, ins, p1, p2),
                new Body(data, null, enableExtendedField)
        );
    }

    /**
     * Case 4.
     * Command data: Data
     * Expected response data: Data
     *
     * @param cla                 Class of instruction
     * @param ins                 Instruction code
     * @param p1                  Instruction parameter 1
     * @param p2                  Instruction parameter 2
     * @param data                String of bytes sent in the data field of the command
     * @param le                  Maximum number of bytes expected in the data field of the response to the command
     * @param enableExtendedField Whether the APDU supports extended length
     * @return ApduCommand object
     */
    public static ApduCommand createCase4(
            int cla, int ins, int p1, int p2,
            byte[] data, int le, boolean enableExtendedField
    ) {
        return new ApduCommand(
                new Header(cla, ins, p1, p2),
                new Body(data, le, enableExtendedField)
        );
    }
}
