package pg.student.securefilesender.services.RSA;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSA {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public void initRSA() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(512);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String encrypt(String textToEncrypt) throws Exception {
        byte[] textToEncryptBytes = textToEncrypt.getBytes();
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.publicKey);

        byte[] encryptedMessageBytes = encryptCipher.doFinal(textToEncryptBytes);

        return encode(encryptedMessageBytes);
    }

    public String decrypt(String textToDecrypt) throws Exception {
        byte[] encryptedBytes = decode(textToDecrypt);
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedBytes);

        return new String(decryptedMessageBytes, "UTF8");
    }

    public byte[] encrypt(SecretKey aesKeyToEncrypt, PublicKey rsaPublicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, rsaPublicKey);
        return cipher.wrap(aesKeyToEncrypt);

    }

    public SecretKey decrypt(byte [] aesKeyToDecrypt, PrivateKey rsaPrivateKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, rsaPrivateKey);
        return (SecretKey) cipher.unwrap(aesKeyToDecrypt, "AES", Cipher.SECRET_KEY);
    }


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

}
