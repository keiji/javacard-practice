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
            return new byte[0];
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
