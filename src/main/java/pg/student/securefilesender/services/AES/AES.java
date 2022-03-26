package pg.student.securefilesender.services.AES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
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

    public IvParameterSpec getIv() {
        return iv;
    }

    public void setIv(IvParameterSpec iv) {
        this.iv = iv;
    }

    private SecretKey key;
    private IvParameterSpec iv;

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

    public IvParameterSpec generateIv(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public String encrypt(String textToEncrypt){
        byte[] cipherText = null;
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.key, iv);
            cipherText = cipher.doFinal(textToEncrypt.getBytes());
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String textToDecrypt){
        byte[] plainText = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.key, iv);
            plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(textToDecrypt));
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return new String(plainText);
    }

    public void encryptFile(){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
            FileInputStream inputStream = new FileInputStream("Daniil_Bykau.pdf");
            FileOutputStream outputStream = new FileOutputStream("Daniil_Bykau1.pdf");
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
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

    public void decryptFile(){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
            FileInputStream inputStream = new FileInputStream("Daniil_Bykau1.pdf");
            FileOutputStream outputStream = new FileOutputStream("Daniil_Bykau2.pdf");
            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
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
