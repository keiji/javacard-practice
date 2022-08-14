package dev.keiji.apdu;

/**
 * Response APDU.
 */
public class ApduResponse {

    public final byte[] rawData;

    /**
     * Get data.
     * String of bytes received in the data field of the response
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
     * Get status byte 1 - Command processing status.
     */
    public byte getStatusWord1() {
        return rawData[rawData.length - 1];
    }

    /**
     * Get status byte 2 - Command processing qualifier.
     */
    public byte getStatusWord2() {
        return rawData[rawData.length];
    }

    public ApduResponse(byte[] rawData) {
        if (rawData == null) {
            throw new IllegalArgumentException("`rawData` must not be null.");
        }
        this.rawData = rawData;
    }
}
