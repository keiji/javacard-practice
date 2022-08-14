package dev.keiji.apdu;

/**
 * Response APDU.
 */
public class ApduResponse {

    public final byte[] rawData;

    /**
     * Get data.
     *
     * @return String of bytes received in the data field of the response
     */
    public byte[] getData() {
        if (rawData.length == 2) {
            return null;
        }

        byte[] result = new byte[rawData.length - 2];
        System.arraycopy(rawData, 0, result, 0, result.length);
        return result;
    }

    /**
     * Get status word 1.
     *
     * @return Command processing status.
     */
    public int getStatusWord1() {
        return Utils.convertByteToInt(rawData[rawData.length - 2]);
    }

    /**
     * Get status word 2.
     *
     * @return Command processing qualifier
     */
    public int getStatusWord2() {
        return Utils.convertByteToInt(rawData[rawData.length - 1]);
    }

    /**
     * Constructor.
     *
     * @param rawData the rawData of APDU response
     */
    public ApduResponse(byte[] rawData) {
        if (rawData == null) {
            throw new IllegalArgumentException("`rawData` must not be null.");
        }
        if (rawData.length < 2) {
            throw new IllegalArgumentException("`rawData` length must be greater or equal 2 bytes.");
        }
        this.rawData = rawData;
    }
}
