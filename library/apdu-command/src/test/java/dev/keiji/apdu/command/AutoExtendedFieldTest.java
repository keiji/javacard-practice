package dev.keiji.apdu.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class AutoExtendedFieldTest {

    private final Random rand = new Random();

    @Test
    public void testComputeDigitalSignature_Short() {
        byte[] data = new byte[255];
        rand.nextBytes(data);

        ComputeDigitalSignature cmd = new ComputeDigitalSignature(0x00, data);
        byte[] bytes = cmd.getBytes();

        // Standard Case 4: Header (4) + Lc (1) + Data (255) + Le (1) = 261
        Assertions.assertEquals(261, bytes.length);
        Assertions.assertEquals(0xFF, bytes[4] & 0xFF);
    }

    @Test
    public void testComputeDigitalSignature_Long() {
        byte[] data = new byte[256];
        rand.nextBytes(data);

        ComputeDigitalSignature cmd = new ComputeDigitalSignature(0x00, data);
        byte[] bytes = cmd.getBytes();

        // Extended Case 4: Header (4) + Lc (3) + Data (256) + Le (2) = 265
        Assertions.assertEquals(265, bytes.length);
        Assertions.assertEquals(0x00, bytes[4]); // First byte of extended Lc
    }

    @Test
    public void testSelectFile_Short() {
        byte[] data = new byte[255];
        rand.nextBytes(data);

        SelectFile.P1[] p1 = new SelectFile.P1[]{SelectFile.P1.SELECT_MF_DF_EF};
        SelectFile.P2[] p2 = new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD};

        SelectFile cmd = new SelectFile(0x00, p1, p2, data);
        byte[] bytes = cmd.getBytes();

        // Standard Case 3: Header (4) + Lc (1) + Data (255) = 260
        Assertions.assertEquals(260, bytes.length);
        Assertions.assertEquals(0xFF, bytes[4] & 0xFF);
    }

    @Test
    public void testSelectFile_Long() {
        byte[] data = new byte[256];
        rand.nextBytes(data);

        SelectFile.P1[] p1 = new SelectFile.P1[]{SelectFile.P1.SELECT_MF_DF_EF};
        SelectFile.P2[] p2 = new SelectFile.P2[]{SelectFile.P2.FIRST_RECORD};

        SelectFile cmd = new SelectFile(0x00, p1, p2, data);
        byte[] bytes = cmd.getBytes();

        // Extended Case 3: Header (4) + Lc (3) + Data (256) = 263
        Assertions.assertEquals(263, bytes.length);
        Assertions.assertEquals(0x00, bytes[4]); // First byte of extended Lc
    }

    @Test
    public void testGetData_Short() {
        byte[] tag = new byte[]{0x5F, 0x20};
        int ne = 255;

        GetData cmd = new GetData(0x00, tag, ne);
        byte[] bytes = cmd.getBytes();

        // Case 2: Header (4) + Le (1) = 5
        Assertions.assertEquals(5, bytes.length);
        Assertions.assertEquals(0xFF, bytes[4] & 0xFF);
    }

    @Test
    public void testGetData_Long() {
        byte[] tag = new byte[]{0x5F, 0x20};
        int ne = 256;

        GetData cmd = new GetData(0x00, tag, ne);
        byte[] bytes = cmd.getBytes();

        // Case 2 Extended: Header (4) + Le (3) = 7
        // ne=256 -> 00 01 00
        Assertions.assertEquals(7, bytes.length);
        Assertions.assertEquals(0x00, bytes[4]);
        Assertions.assertEquals(0x01, bytes[5]);
        Assertions.assertEquals(0x00, bytes[6]);
    }

    @Test
    public void testReadBinary_Short() {
        int offset = 0;
        int ne = 255;

        ReadBinary cmd = new ReadBinary(0x00, offset, ne);
        byte[] bytes = cmd.getBytes();

        // Case 2: Header (4) + Le (1) = 5
        Assertions.assertEquals(5, bytes.length);
        Assertions.assertEquals(0xFF, bytes[4] & 0xFF);
    }

    @Test
    public void testReadBinary_Long() {
        int offset = 0;
        int ne = 256;

        ReadBinary cmd = new ReadBinary(0x00, offset, ne);
        byte[] bytes = cmd.getBytes();

        // Case 2 Extended: Header (4) + Le (3) = 7
        Assertions.assertEquals(7, bytes.length);
        Assertions.assertEquals(0x00, bytes[4]);
        Assertions.assertEquals(0x01, bytes[5]);
        Assertions.assertEquals(0x00, bytes[6]);
    }
}
