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

public class ReadBinary implements BaseCommand {
    private static final int MASK_1ST_BYTE = 0x000000FF;
    private static final int MASK_2ND_BYTE = 0x0000FF00;

    private final ApduCommand apduCommand;

    public ReadBinary(int cla, int offset, int ne, boolean enableExtendedField) {
        int p1 = (offset & MASK_2ND_BYTE) >>> 8;
        int p2 = (offset & MASK_1ST_BYTE);
        apduCommand = ApduCommand.createCase2(cla, 0xB0, p1, p2, ne, enableExtendedField);
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }
}
