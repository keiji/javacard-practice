package dev.keiji.javacard.applet;

import javacard.security.*;

class KeyData {
    final byte algorithm;
    final KeyPair keyPair;

    KeyData() {
        algorithm = Signature.ALG_ECDSA_SHA;
        keyPair = generateECKeyPair(KeyBuilder.LENGTH_EC_FP_521);

//        algorithm = Signature.ALG_RSA_SHA_256_PKCS1;
//        keyPair = generateRsaKeyPair(KeyBuilder.LENGTH_RSA_1024);
    }

    private KeyPair generateECKeyPair(short length) {
        ECPrivateKey pri = (ECPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_EC_FP_PRIVATE, length, false);
        ECPublicKey pub = (ECPublicKey) KeyBuilder.buildKey(KeyBuilder.TYPE_EC_FP_PUBLIC, length, false);

        SecP521R1.setup(pub);
        SecP521R1.setup(pri);

        KeyPair kp = new KeyPair(pub, pri);
        kp.genKeyPair();

        return kp;
    }

    private KeyPair generateRsaKeyPair(short length) {
        PrivateKey pri = (PrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, length, false);
        PublicKey pub = (PublicKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC, length, false);

        KeyPair kp = new KeyPair(pub, pri);
        kp.genKeyPair();

        return kp;
    }
}
