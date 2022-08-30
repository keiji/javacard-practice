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

import dev.keiji.apdu.ApduCommand;

public class Verify implements BaseCommand {

    public static class P2 {
        public final int value;

        public P2(int value) {
            this.value = value;
        }

        /**
         * No information is given.
         */
        private static final P2 NO_INFORMATION_IS_GIVEN = new P2(0b000_00000);

        /**
         * Global reference data (e.g. card password).
         */
        public static final P2 GLOBAL = new P2(0b000_00000);

        /**
         * Specific reference data (e.g. DF specific password).
         */
        public static final P2 SPECIFIC = new P2(0b100_00000);
    }

    private static final int MAX_5BITS_REFERENCE_DATA_NUMBER_VALUE = 0b000_11111;


    private final ApduCommand apduCommand;

    public Verify(int cla, P2 p2, byte[] data) {
        this(cla, p2, data, null);
    }

    public Verify(int cla, P2 p2, byte[] data, Integer referenceDataNumber) {
        if (data != null && data.length == 0) {
            throw new IllegalArgumentException("data length must not be 0.");
        }

        int p2Value = p2.value;

        if (referenceDataNumber != null) {
            if (referenceDataNumber > MAX_5BITS_REFERENCE_DATA_NUMBER_VALUE) {
                throw new IllegalArgumentException("referenceDataNumber value must not be greater than " + MAX_5BITS_REFERENCE_DATA_NUMBER_VALUE);
            }
            if (referenceDataNumber < 0) {
                throw new IllegalArgumentException("referenceDataNumber value must not be less than 0.");
            }

            p2Value |= referenceDataNumber;
        }

        if (data == null) {
            apduCommand = ApduCommand.createCase1(
                    cla, 0x20,
                    0x00, p2Value
            );
        } else {
            apduCommand = ApduCommand.createCase3(
                    cla, 0x20,
                    0x00, p2Value,
                    data,
                    false
            );
        }
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }
}
