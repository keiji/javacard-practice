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

public class GetChallenge extends BaseCommand {
    private static final int INS = 0xB4;
    private static final int P1 = 0x00;
    private static final int P2 = 0x00;

    private final ApduCommand apduCommand;

    public GetChallenge(int cla, int requestLength) {
        super(cla);

        if (requestLength <= 0) {
            throw new IllegalArgumentException("requestLength length must be greater than 0.");
        }

        apduCommand = ApduCommand.createCase2(
                cla, INS,
                P1, P2,
                requestLength, false
        );
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }

    public static class Response extends BaseResponse {

        public Response(byte[] rawData) {
            super(rawData);
        }
    }
}
