package dev.keiji.apdu.command;

import dev.keiji.apdu.ApduCommand;

public class SelectFile implements BaseCommand {

    public static class P1 {
        /**
         * Select MF, DF or EF (data field=identifier or empty).
         */
        public static final int SELECT_MF_DF_EF = 0b000000_00;

        /**
         * Select child DF (data field=DF identifier).
         */
        public static final int SELECT_CHILD_DF = 0b000000_01;

        /**
         * Select EF under current DF (data field=EF identifier).
         */
        public static final int SELECT_EF_UNDER_CURRENT_DF = 0b000000_10;

        /**
         * Select parent DF of the current DF (empty data field).
         */
        public static final int SELECT_PARENT_DF_OF_CURRENT_DF = 0b000000_11;

        /**
         * Direct selection by DF name (data field=DF name).
         */
        public static final int DIRECT_SELECTION_BY_DF_NAME = 0b000001_00;

        /**
         * Select from MF (data field=path without the identifier of the MF).
         */
        public static final int SELECT_FROM_MF = 0b00001_000;

        /**
         * Select from current DF (data field=path without the identifier of the current DF).
         */
        public static final int SELECT_FROM_CURRENT_DF = 0b00001_001;
    }

    public static class P2 {
        /**
         * First record.
         */
        public static final int FIRST_RECORD = 0b0000_00_00;

        /**
         * Last record.
         */
        public static final int LAST_RECORD = 0b0000_00_01;

        /**
         * Next record.
         */
        public static final int NEXT_RECORD = 0b0000_00_10;

        /**
         * Previous record.
         */
        public static final int PREVIOUS_RECORD = 0b0000_00_11;

        /**
         * Return FCI, optional template.
         */
        public static final int RETURN_FCI_OPTIONAL_TEMPLATE = 0b000000_00;

        /**
         * Return FCP template.
         */
        public static final int RETURN_FCP_TEMPLATE = 0b000001_00;

        /**
         * Return FMD template.
         */
        public static final int RETURN_FMD_TEMPLATE = 0b000010_00;
    }

    public static SelectFile create(int p1, int p2, byte[] data, boolean enableExtendedField) {
        return new SelectFile(p1, p2, data, enableExtendedField);
    }

    private final ApduCommand apduCommand;

    private SelectFile(int p1, int p2, byte[] data, boolean enableExtendedField) {
        apduCommand = ApduCommand.createCase3(0x00, 0xA4, p1, p2, data, enableExtendedField);
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }
}
