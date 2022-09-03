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

// 29p of https://gnupg.org/ftp/specs/OpenPGP-smart-card-application-2.2.pdf
public class ComputeDigitalSignature extends BaseCommand {
    private static final int INS = 0x2A;
    private static final int P1 = 0x9E;
    private static final int P2 = 0x9A;

    private final ApduCommand apduCommand;

    public ComputeDigitalSignature(int cla, byte[] data, boolean enableExtendedField) {
        super(cla);

        if (data == null) {
            throw new IllegalArgumentException("data must not be null.");
        }
        if (data.length == 0) {
            throw new IllegalArgumentException("data length must not be 0.");
        }

        apduCommand = ApduCommand.createCase4(
                cla, INS,
                P1, P2,
                data,
                0,
                enableExtendedField
        );
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
