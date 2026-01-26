/*
 * Copyright (C) 2024 ARIYAMA Keiji
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

/**
 * Get Data command (INS=0xCA).
 * <p>
 * This command is used to retrieve a specific data object from the card,
 * identified by a tag (P1-P2).
 * </p>
 */
public class GetData extends BaseCommand {
    private static final int INS = 0xCA;

    private static final int MASK_1ST_BYTE = 0x000000FF;

    private final ApduCommand apduCommand;

    /**
     * Constructor using tag byte array.
     *
     * @param cla                 Class of instruction.
     * @param tag                 Tag bytes identifying the data object (1 or 2 bytes).
     * @param ne                  Maximum number of bytes expected in the response (Le).
     * @param enableExtendedField Whether to use extended length APDU.
     * @throws IllegalArgumentException If `tag` is null or not 1 or 2 bytes long.
     */
    public GetData(int cla, byte[] tag, int ne, boolean enableExtendedField) {
        super(cla);

        if (tag == null) {
            throw new IllegalArgumentException("`tag` must not be null.");
        }

        if (!(tag.length == 1 || tag.length == 2)) {
            throw new IllegalArgumentException("tag length must be 1 or 2.");
        }

        int p1;
        int p2;
        if (tag.length == 1) {
            p1 = 0x0;
            p2 = tag[0] & MASK_1ST_BYTE;
        } else {
            p1 = tag[0] & MASK_1ST_BYTE;
            p2 = tag[1] & MASK_1ST_BYTE;
        }

        apduCommand = ApduCommand.createCase2(cla, INS, p1, p2, ne, enableExtendedField);
    }

    /**
     * Constructor using explicit P1 and P2.
     *
     * @param cla                 Class of instruction.
     * @param p1                  Parameter 1 (High byte of tag).
     * @param p2                  Parameter 2 (Low byte of tag).
     * @param ne                  Maximum number of bytes expected in the response (Le).
     * @param enableExtendedField Whether to use extended length APDU.
     */
    public GetData(int cla, int p1, int p2, int ne, boolean enableExtendedField) {
        super(cla);

        apduCommand = ApduCommand.createCase2(cla, INS, p1, p2, ne, enableExtendedField);
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }

    /**
     * Response for GetData command.
     */
    public static class Response extends BaseResponse {

        /**
         * Constructor.
         *
         * @param rawData Raw response data.
         */
        public Response(byte[] rawData) {
            super(rawData);
        }
    }
}
