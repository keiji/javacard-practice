package dev.keiji.apdu.command;

import dev.keiji.apdu.ApduCommand;

// 29p of https://gnupg.org/ftp/specs/OpenPGP-smart-card-application-2.2.pdf
public class ComputeDigitalSignature implements BaseCommand {

    private final ApduCommand apduCommand;

    public ComputeDigitalSignature(int cla, byte[] data, boolean enableExtendedField) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null.");
        }
        if (data.length == 0) {
            throw new IllegalArgumentException("data length must not be 0.");
        }

        apduCommand = ApduCommand.createCase4(
                cla, 0x2A,
                0x9E, 0x9A,
                data,
                0,
                enableExtendedField
        );
    }

    @Override
    public byte[] getBytes() {
        return apduCommand.getBytes();
    }
}
