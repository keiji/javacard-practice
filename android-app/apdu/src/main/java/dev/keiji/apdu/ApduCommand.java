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

        public void write(ByteBuffer byteBuffer) {
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
            } else {
                this.data = data;
                this.lc = integerToByteArrayForLcOrLe(data.length);
            }

            if (le == null) {
                this.le = null;
            } else {
                this.le = integerToByteArrayForLcOrLe(le);
            }
        }

        public void write(ByteBuffer byteBuffer) {
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

    public void write(ByteBuffer byteBuffer) {
        header.write(byteBuffer);

        if (body != null) {
            body.write(byteBuffer);
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

    private static final int FILTER_GREATER_1BYTE = 0xFFFFFF00;
    private static final int FILTER_1ST_BYTE = 0x000000FF;
    private static final int FILTER_2ND_BYTE = 0x0000FF00;
    private static final int FILTER_3RD_BYTE = 0x00FF0000;
    private static final int FILTER_4TH_BYTE = 0xFF000000;

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
