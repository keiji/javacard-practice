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

public class SelectFile extends BaseCommand {

    public static class P1 {
        public final int value;

        public P1(int value) {
            this.value = value;
        }

        /**
         * Select MF, DF or EF (data field=identifier or empty).
         */
        public static final P1 SELECT_MF_DF_EF = new P1(0b000000_00);

        /**
         * Select child DF (data field=DF identifier).
         */
        public static final P1 SELECT_CHILD_DF = new P1(0b000000_01);

        /**
         * Select EF under current DF (data field=EF identifier).
         */
        public static final P1 SELECT_EF_UNDER_CURRENT_DF = new P1(0b000000_10);

        /**
         * Select parent DF of the current DF (empty data field).
         */
        public static final P1 SELECT_PARENT_DF_OF_CURRENT_DF = new P1(0b000000_11);

        /**
         * Direct selection by DF name (data field=DF name).
         */
        public static final P1 DIRECT_SELECTION_BY_DF_NAME = new P1(0b000001_00);

        /**
         * Select from MF (data field=path without the identifier of the MF).
         */
        public static final P1 SELECT_FROM_MF = new P1(0b00001_000);

        /**
         * Select from current DF (data field=path without the identifier of the current DF).
         */
        public static final P1 SELECT_FROM_CURRENT_DF = new P1(0b00001_001);

    }

    public static class P2 {
        public final int value;

        public P2(int value) {
            this.value = value;
        }

        /**
         * First record.
         */
        public static final P2 FIRST_RECORD = new P2(0b0000_00_00);

        /**
         * Last record.
         */
        public static final P2 LAST_RECORD = new P2(0b0000_00_01);

        /**
         * Next record.
         */
        public static final P2 NEXT_RECORD = new P2(0b0000_00_10);

        /**
         * Previous record.
         */
        public static final P2 PREVIOUS_RECORD = new P2(0b0000_00_11);

        /**
         * Return FCI, optional template.
         */
        public static final P2 RETURN_FCI_OPTIONAL_TEMPLATE = new P2(0b000000_00);

        /**
         * Return FCP template.
         */
        public static final P2 RETURN_FCP_TEMPLATE = new P2(0b000001_00);

        /**
         * Return FMD template.
         */
        public static final P2 RETURN_FMD_TEMPLATE = new P2(0b000010_00);
    }

    private final ApduCommand apduCommand;

    public SelectFile(int cla, P1[] p1Array, P2[] p2Array, byte[] data, boolean enableExtendedField) {
        super(cla);

        int p1 = 0x00;
        for (P1 p : p1Array) {
            p1 |= p.value;
        }
        int p2 = 0x00;
        for (P2 p : p2Array) {
            p2 |= p.value;
        }
        apduCommand = ApduCommand.createCase3(cla, 0xA4, p1, p2, data, enableExtendedField);
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }

    public class Response extends BaseResponse {

        public Response(byte[] rawData) {
            super(rawData);
        }
    }
}
