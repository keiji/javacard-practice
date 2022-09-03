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

public abstract class BaseCommand {

    public final int cla;

    public BaseCommand(int cla) {
        this.cla = cla;
    }

    abstract byte[] getBytes();

    public static abstract class BaseResponse extends ApduResponse {
        private static final int MASK_1ST_BYTE = 0xFF;

        public static int getStatusWord(int sw1, int sw2) {
            return ((sw1 & MASK_1ST_BYTE) << 8) | (sw2 & MASK_1ST_BYTE);
        }

        public int getStatusWord() {
            return getStatusWord(getStatusWord1(), getStatusWord2());
        }

        public BaseResponse(byte[] rawData) {
            super(rawData);
        }
    }
}
