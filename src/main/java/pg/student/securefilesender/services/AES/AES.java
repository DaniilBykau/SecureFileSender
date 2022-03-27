package pg.student.securefilesender.services.AES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

public class AES {
    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    private SecretKey key;
    private byte[] iv;

    // n -> keySize
    public SecretKey generateKeyAes(int n){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(n);
            key = keyGenerator.generateKey();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        this.iv = generateIv();
        return key;
    }

    public byte[] generateIv(){
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    public String encrypt(String textToEncrypt, byte [] iv, SecretKey keyAES){
        byte[] cipherText = null;
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, this.key, ivParameterSpec);
            cipherText = cipher.doFinal(textToEncrypt.getBytes());
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String textToDecrypt, byte [] iv, SecretKey keyAES){
        byte[] plainText = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, this.key, ivParameterSpec);
            plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(textToDecrypt));
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return new String(plainText);
    }

    public void encryptFile( byte [] iv, SecretKey keyAES, File file){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keyAES, ivParameterSpec);
            FileInputStream inputStream = new FileInputStream(file.getPath());
            FileOutputStream outputStream = new FileOutputStream("Daniil_Bykau1.txt");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
            inputStream.close();
            outputStream.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }

    public void decryptFile( byte [] iv, SecretKey keyAES, File file){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keyAES, ivParameterSpec);
            FileInputStream inputStream = new FileInputStream("Daniil_Bykau1.txt");
            FileOutputStream outputStream = new FileOutputStream("Daniil_Bykau2.txt");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer))>0) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
            inputStream.close();
            outputStream.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

    }
}
