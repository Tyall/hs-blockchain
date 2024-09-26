package blockchain.utils;

import java.security.*;

public class SignatureUtils {

    public static KeyPair getKeyPair() {
        KeyPair keyPair = null;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            keyPair = keyGen.generateKeyPair();
        } catch (Exception e) {
            System.out.println("Exception occurred when generating KeyPair with RSA Algorithm. Message: " + e.getMessage());
        }

        return keyPair;
    }

    public static byte[] sign(String data, PrivateKey key) {
        byte[] signature = null;

        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(key);
            sig.update(data.getBytes());
            signature = sig.sign();
        } catch (Exception e) {
            System.out.println("Exception occurred when creating signature with SHA1withRSA Algorithm. Message: " + e.getMessage());
        }
        return signature;
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey key) {
        boolean verificationStatus = false;

        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(key);
            sig.update(data);
            verificationStatus = sig.verify(signature);
        } catch (Exception e) {
            System.out.println("Exception occurred when verifying signature with SHA1withRSA Algorithm. Message: " + e.getMessage());
        }

        return verificationStatus;
    }

    private SignatureUtils() {
        throw new IllegalArgumentException("Cannot instantiate utility class");
    }
}