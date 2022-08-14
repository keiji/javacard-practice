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

        public Header(byte cla, byte ins, byte p1, byte p2) {
            this.cla = cla;
            this.ins = ins;
            this.p1 = p1;
            this.p2 = p2;
        }

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
        private static final int MAX_LC_LENGTH = 0x00FFFFFF;
        private static final int MAX_LE_LENGTH = 0x00FFFFFF;

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

        public Body(byte[] data, Integer le) {
            if (data == null && le == null) {
                throw new IllegalArgumentException("Either `data` or `le` must not be null.");
            }

            if (data == null) {
                this.data = null;
                this.lc = null;
            } else if (data.length > MAX_LC_LENGTH) {
                throw new IllegalArgumentException("`data` size must be less or equal than " + MAX_LC_LENGTH + " bytes.");
            } else {
                this.data = data;
                this.lc = Utils.integerToByteArrayForLcOrLe(data.length);
            }

            if (le == null) {
                this.le = null;
            } else if (le > MAX_LE_LENGTH) {
                throw new IllegalArgumentException("`le` must be less or equal than " + MAX_LE_LENGTH + " bytes.");
            } else {
                this.le = Utils.integerToByteArrayForLcOrLe(le);
            }
        }

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

    public final Header header;
    public final Body body;

    ApduCommand(Header header, Body body) {
        if (header == null) {
            throw new IllegalArgumentException("`header` must not be null.");
        }

        this.header = header;
        this.body = body;
    }

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
     * @param cla
     * @param ins
     * @param p1
     * @param p2
     * @return
     */
    public static ApduCommand createCase1(byte cla, byte ins, byte p1, byte p2) {
        return new ApduCommand(new Header(cla, ins, p1, p2), null);
    }

    /**
     * Case 2.
     * Command data: No data
     * Expected response data: Data
     *
     * @param cla
     * @param ins
     * @param p1
     * @param p2
     * @param le
     * @return
     */
    public static ApduCommand createCase2(byte cla, byte ins, byte p1, byte p2, int le) {
        return new ApduCommand(new Header(cla, ins, p1, p2), new Body(null, le));
    }

    /**
     * Case 3.
     * Command data: Data
     * Expected response data: No data
     *
     * @param cla
     * @param ins
     * @param p1
     * @param p2
     * @param data
     * @return
     */
    public static ApduCommand createCase3(byte cla, byte ins, byte p1, byte p2, byte[] data) {
        return new ApduCommand(new Header(cla, ins, p1, p2), new Body(data, null));
    }

    /**
     * Case 4.
     * Command data: Data
     * Expected response data: Data
     *
     * @param cla
     * @param ins
     * @param p1
     * @param p2
     * @param data
     * @param le
     * @return
     */
    public static ApduCommand createCase4(byte cla, byte ins, byte p1, byte p2, byte[] data, int le) {
        return new ApduCommand(new Header(cla, ins, p1, p2), new Body(data, le));
    }
}
