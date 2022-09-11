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
 * Response APDU.
 */
public class ApduResponse {

    private final byte[] data;

    /**
     * Get data.
     *
     * @return String of bytes received in the data field of the response
     */
    public byte[] getData() {
        return data;
    }

    private final int statusWord1;

    /**
     * Get status word 1.
     *
     * @return Command processing status
     */
    public int getStatusWord1() {
        return statusWord1;
    }

    private final int statusWord2;

    /**
     * Get status word 2.
     *
     * @return Command processing qualifier
     */
    public int getStatusWord2() {
        return statusWord2;
    }

    /**
     * Number of bytes of the response.
     *
     * @return Returns the number of bytes of the response
     */
    public int size() {
        return data.length + 2;
    }

    /**
     * Write the response to the given ByteArray.
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

        if (data.length > 0) {
            System.arraycopy(data, 0, byteArray, offset, data.length);
            offset += data.length;
        }

        byteArray[offset] = Utils.convertIntToByte(statusWord1);
        byteArray[offset + 1] = Utils.convertIntToByte(statusWord2);
    }

    /**
     * Constructor.
     *
     * @param statusWord1 Command processing status
     * @param statusWord2 Command processing qualifier
     * @param data        String of bytes received in the data field of the response
     */
    public ApduResponse(int statusWord1, int statusWord2, byte[] data) {
        this.statusWord1 = statusWord1;
        this.statusWord2 = statusWord2;
        this.data = data;
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

        statusWord1 = Utils.convertByteToInt(rawData[rawData.length - 2]);
        statusWord2 = Utils.convertByteToInt(rawData[rawData.length - 1]);

        if (rawData.length == 2) {
            data = new byte[0];
        } else {
            data = new byte[rawData.length - 2];
            System.arraycopy(rawData, 0, data, 0, data.length);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApduResponse that = (ApduResponse) o;
        return statusWord1 == that.statusWord1 && statusWord2 == that.statusWord2 && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(statusWord1, statusWord2);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
