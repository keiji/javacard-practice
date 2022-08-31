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

public abstract class BaseCommand {

    public final int cla;

    public BaseCommand(int cla) {
        this.cla = cla;
    }

    abstract byte[] getBytes();

    public static abstract class BaseResponse {
        private static final int MASK_1ST_BYTE = 0xFF;

        public final byte[] rawData;

        public final byte[] data;

        public final byte sw1;
        public final byte sw2;

        public static int getStatusWord(byte sw1, byte sw2) {
            return ((((int) sw1) & MASK_1ST_BYTE) << 8) | (((int) sw2) & MASK_1ST_BYTE);
        }

        public int getStatusWord() {
            return getStatusWord(sw1, sw2);
        }

        public BaseResponse(byte[] rawData) {
            if (rawData == null) {
                throw new IllegalArgumentException("rawData must not be null.");
            }
            if (rawData.length < 2) {
                throw new IllegalArgumentException("rawData length must be greater than 2.");
            }

            this.rawData = rawData;
            this.sw1 = rawData[rawData.length - 2];
            this.sw2 = rawData[rawData.length - 1];

            if (rawData.length == 2) {
                data = null;
            } else {
                data = new byte[rawData.length - 2];
                System.arraycopy(rawData, 0, data, 0, data.length);
            }
        }
    }
}
