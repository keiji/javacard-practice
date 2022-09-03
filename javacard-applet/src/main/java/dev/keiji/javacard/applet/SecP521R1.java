package dev.keiji.javacard.applet;

import javacard.security.ECKey;

class SecP521R1 {
    private SecP521R1() {
    }

    // 1.3.132.0.35
    private static final byte[] OID = {
            (byte) 0x2B, (byte) 0x81, (byte) 0x04, (byte) 0x00, (byte) 0x23
    };

    //  2.9.1   Recommended Parameters secp521r1
    // https://www.secg.org/SEC2-Ver-1.0.pdf

    //    p = 01FF
    //        FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF
    //        FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF
    //        FFFFFFFF FFFFFFFF FFFFFFFF = 2521
    private static final byte[] p = {
            (byte) 0x01, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
    };


    //    a = 01FF
    //        FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF
    //        FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF
    //        FFFFFFFF FFFFFFFF FFFFFFFC
    private static final byte[] a = {
            (byte) 0x01, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFC,
    };


    //    b = 0051
    //        953EB961 8E1C9A1F 929A21A0 B68540EE A2DA725B 99B315F3
    //        B8B48991 8EF109E1 56193951 EC7E937B 1652C0BD 3BB1BF07 3573DF88
    //        3D2C34F1 EF451FD4 6B503F00
    private static final byte[] b = {
            (byte) 0x00, (byte) 0x51,

            (byte) 0x95, (byte) 0x3E, (byte) 0xB9, (byte) 0x61,
            (byte) 0x8E, (byte) 0x1C, (byte) 0x9A, (byte) 0x1F,
            (byte) 0x92, (byte) 0x9A, (byte) 0x21, (byte) 0xA0,
            (byte) 0xB6, (byte) 0x85, (byte) 0x40, (byte) 0xEE,
            (byte) 0xA2, (byte) 0xDA, (byte) 0x72, (byte) 0x5B,
            (byte) 0x99, (byte) 0xB3, (byte) 0x15, (byte) 0xF3,

            (byte) 0xB8, (byte) 0xB4, (byte) 0x89, (byte) 0x91,
            (byte) 0x8E, (byte) 0xF1, (byte) 0x09, (byte) 0xE1,
            (byte) 0x56, (byte) 0x19, (byte) 0x39, (byte) 0x51,
            (byte) 0xEC, (byte) 0x7E, (byte) 0x93, (byte) 0x7B,
            (byte) 0x16, (byte) 0x52, (byte) 0xC0, (byte) 0xBD,
            (byte) 0x3B, (byte) 0xB1, (byte) 0xBF, (byte) 0x07,
            (byte) 0x35, (byte) 0x73, (byte) 0xDF, (byte) 0x88,

            (byte) 0x3D, (byte) 0x2C, (byte) 0x34, (byte) 0xF1,
            (byte) 0xEF, (byte) 0x45, (byte) 0x1F, (byte) 0xD4,
            (byte) 0x6B, (byte) 0x50, (byte) 0x3F, (byte) 0x00,
    };



    //    G = 04
    //        00C6858E 06B70404 E9CD9E3E CB662395 B4429C64 8139053F
    //        B521F828 AF606B4D 3DBAA14B 5E77EFE7 5928FE1D C127A2FF A8DE3348
    //        B3C1856A 429BF97E 7E31C2E5 BD660118 39296A78 9A3BC004 5C8A5FB4
    //        2C7D1BD9 98F54449 579B4468 17AFBD17 273E662C 97EE7299 5EF42640
    //        C550B901 3FAD0761 353C7086 A272C240 88BE9476 9FD16650
    private static final byte[] G = {
            (byte) 0x04,

            (byte) 0x00, (byte) 0xC6, (byte) 0x85, (byte) 0x8E,
            (byte) 0x06, (byte) 0xB7, (byte) 0x04, (byte) 0x04,
            (byte) 0xE9, (byte) 0xCD, (byte) 0x9E, (byte) 0x3E,
            (byte) 0xCB, (byte) 0x66, (byte) 0x23, (byte) 0x95,
            (byte) 0xB4, (byte) 0x42, (byte) 0x9C, (byte) 0x64,
            (byte) 0x81, (byte) 0x39, (byte) 0x05, (byte) 0x3F,

            (byte) 0xB5, (byte) 0x21, (byte) 0xF8, (byte) 0x28,
            (byte) 0xAF, (byte) 0x60, (byte) 0x6B, (byte) 0x4D,
            (byte) 0x3D, (byte) 0xBA, (byte) 0xA1, (byte) 0x4B,
            (byte) 0x5E, (byte) 0x77, (byte) 0xEF, (byte) 0xE7,
            (byte) 0x59, (byte) 0x28, (byte) 0xFE, (byte) 0x1D,
            (byte) 0xC1, (byte) 0x27, (byte) 0xA2, (byte) 0xFF,
            (byte) 0xA8, (byte) 0xDE, (byte) 0x33, (byte) 0x48,

            (byte) 0xB3, (byte) 0xC1, (byte) 0x85, (byte) 0x6A,
            (byte) 0x42, (byte) 0x9B, (byte) 0xF9, (byte) 0x7E,
            (byte) 0x7E, (byte) 0x31, (byte) 0xC2, (byte) 0xE5,
            (byte) 0xBD, (byte) 0x66, (byte) 0x01, (byte) 0x18,
            (byte) 0x39, (byte) 0x29, (byte) 0x6A, (byte) 0x78,
            (byte) 0x9A, (byte) 0x3B, (byte) 0xC0, (byte) 0x04,
            (byte) 0x5C, (byte) 0x8A, (byte) 0x5F, (byte) 0xB4,

            (byte) 0x2C, (byte) 0x7D, (byte) 0x1B, (byte) 0xD9,
            (byte) 0x98, (byte) 0xF5, (byte) 0x44, (byte) 0x49,
            (byte) 0x57, (byte) 0x9B, (byte) 0x44, (byte) 0x68,
            (byte) 0x17, (byte) 0xAF, (byte) 0xBD, (byte) 0x17,
            (byte) 0x27, (byte) 0x3E, (byte) 0x66, (byte) 0x2C,
            (byte) 0x97, (byte) 0xEE, (byte) 0x72, (byte) 0x99,
            (byte) 0x5E, (byte) 0xF4, (byte) 0x26, (byte) 0x40,

            (byte) 0xC5, (byte) 0x50, (byte) 0xB9, (byte) 0x01,
            (byte) 0x3F, (byte) 0xAD, (byte) 0x07, (byte) 0x61,
            (byte) 0x35, (byte) 0x3C, (byte) 0x70, (byte) 0x86,
            (byte) 0xA2, (byte) 0x72, (byte) 0xC2, (byte) 0x40,
            (byte) 0x88, (byte) 0xBE, (byte) 0x94, (byte) 0x76,
            (byte) 0x9F, (byte) 0xD1, (byte) 0x66, (byte) 0x50,
    };

    //    n = 01FF
    //        FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF
    //        FFFFFFFF FFFFFFFA 51868783 BF2F966B 7FCC0148 F709A5D0 3BB5C9B8
    //        899C47AE BB6FB71E 91386409
    private static final byte[] n = {
            (byte) 0x01, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,

            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFA,
            (byte) 0x51, (byte) 0x86, (byte) 0x87, (byte) 0x83,
            (byte) 0xBF, (byte) 0x2F, (byte) 0x96, (byte) 0x6B,
            (byte) 0x7F, (byte) 0xCC, (byte) 0x01, (byte) 0x48,
            (byte) 0xF7, (byte) 0x09, (byte) 0xA5, (byte) 0xD0,
            (byte) 0x3B, (byte) 0xB5, (byte) 0xC9, (byte) 0xB8,

            (byte) 0x89, (byte) 0x9C, (byte) 0x47, (byte) 0xAE,
            (byte) 0xBB, (byte) 0x6F, (byte) 0xB7, (byte) 0x1E,
            (byte) 0x91, (byte) 0x38, (byte) 0x64, (byte) 0x09
    };

    // h = 01
    private static final short k = 0x01;

    static void setup(ECKey key) {
        key.setFieldFP(p, (short) 0, (short) p.length);
        key.setA(a, (short) 0, (short) a.length);
        key.setB(b, (short) 0, (short) b.length);
        key.setG(G, (short) 0, (short) G.length);
        key.setR(n, (short) 0, (short) n.length);
        key.setK(k);
    }
}
