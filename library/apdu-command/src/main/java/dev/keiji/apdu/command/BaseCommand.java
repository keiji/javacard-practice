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

package dev.keiji.apdu.command;

import dev.keiji.apdu.ApduResponse;

/**
 * Base abstract class for APDU commands.
 * <p>
 * Provides the basic structure and common functionality for APDU commands,
 * including the Class (CLA) byte.
 * </p>
 */
public abstract class BaseCommand {

    /**
     * The Class (CLA) byte of the APDU command.
     */
    public final int cla;

    /**
     * Maximum data length for standard APDU (255 bytes).
     */
    public static final int MAX_DATA_LENGTH_STANDARD = 255;

    /**
     * Constructor.
     *
     * @param cla The Class (CLA) byte.
     */
    public BaseCommand(int cla) {
        this.cla = cla;
    }

    /**
     * Gets the byte array representation of the APDU command.
     *
     * @return The APDU command as a byte array.
     */
    public abstract byte[] getBytes();

    /**
     * Base abstract class for APDU responses.
     * <p>
     * Extends {@link ApduResponse} to provide helper methods for handling Status Words (SW).
     * </p>
     */
    public static abstract class BaseResponse extends ApduResponse {
        private static final int MASK_1ST_BYTE = 0xFF;

        /**
         * Combines SW1 and SW2 into a single integer status word.
         *
         * @param sw1 Status Word 1.
         * @param sw2 Status Word 2.
         * @return The combined status word ({@code SW1 << 8 | SW2}).
         */
        public static int getStatusWord(int sw1, int sw2) {
            return ((sw1 & MASK_1ST_BYTE) << 8) | (sw2 & MASK_1ST_BYTE);
        }

        /**
         * Gets the combined status word (SW) from this response.
         *
         * @return The combined status word ({@code SW1 << 8 | SW2}).
         */
        public int getStatusWord() {
            return getStatusWord(getStatusWord1(), getStatusWord2());
        }

        /**
         * Constructor.
         *
         * @param rawData The raw byte array of the APDU response.
         * @throws IllegalArgumentException If rawData is null or invalid (handled by super).
         */
        public BaseResponse(byte[] rawData) {
            super(rawData);
        }
    }
}
