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

import java.nio.ByteBuffer;

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
         * Write this object to the given ByteBuffer.
         *
         * @param byteBuffer The ByteBuffer to which this object should output
         */
        public void writeTo(ByteBuffer byteBuffer) {
            byteBuffer.put(cla)
                    .put(ins)
                    .put(p1)
                    .put(p2);
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

        /**
         * Constructor.
         *
         * @param data                String of bytes sent in the data field of the command
         * @param ne                  Maximum number of bytes expected in the data field of the response to the command
         * @param enableExtendedField Whether the APDU supports extended length
         */
        public Body(byte[] data, Integer ne, boolean enableExtendedField) {
            if (data == null && ne == null) {
                throw new IllegalArgumentException("Either `data` or `ne` must not be null.");
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

            if (ne == null) {
                this.le = null;
            } else if (!enableExtendedField && ne > MAX_LE_LENGTH) {
                throw new IllegalArgumentException("`ne` must be less or equal than " + MAX_LE_LENGTH + " bytes.");
            } else if (enableExtendedField && ne > MAX_LE_EXTENDED_LENGTH) {
                throw new IllegalArgumentException("`ne` must be less or equal than " + MAX_LE_EXTENDED_LENGTH + " bytes.");
            } else {
                this.le = Utils.integerToByteArrayForLcOrLe(ne);
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
         * Write this object to the given ByteBuffer.
         *
         * @param byteBuffer The ByteBuffer to which this object should output
         */
        public void writeTo(ByteBuffer byteBuffer) {
            if (lc != null) {
                byteBuffer.put(lc);
            }
            if (data != null) {
                byteBuffer.put(data);
            }
            if (le != null) {
                byteBuffer.put(le);
            }
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
     * Write this object to the given ByteBuffer.
     *
     * @param byteBuffer The ByteBuffer to which this object should output
     */
    public void writeTo(ByteBuffer byteBuffer) {
        header.writeTo(byteBuffer);

        if (body != null) {
            body.writeTo(byteBuffer);
        }
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
     * @param ne                  Number of bytes present in the data field of the command
     * @param enableExtendedField Whether the APDU supports extended length
     * @return ApduCommand object
     */
    public static ApduCommand createCase2(
            int cla, int ins, int p1, int p2,
            int ne, boolean enableExtendedField
    ) {
        return new ApduCommand(
                new Header(cla, ins, p1, p2),
                new Body(null, ne, enableExtendedField)
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
     * @param data                String of bytes sent in the data field of the command.'
     * @param ne                  Maximum number of bytes expected in the data field of the response to the command
     * @param enableExtendedField Whether the APDU supports extended length
     * @return ApduCommand object
     */
    public static ApduCommand createCase4(
            int cla, int ins, int p1, int p2,
            byte[] data, int ne, boolean enableExtendedField
    ) {
        return new ApduCommand(
                new Header(cla, ins, p1, p2),
                new Body(data, ne, enableExtendedField)
        );
    }
}
