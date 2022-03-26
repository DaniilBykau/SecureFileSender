package pg.student.securefilesender.services.connection;

import javafx.collections.ObservableList;

import javafx.scene.control.ListView;
import pg.student.securefilesender.services.AES.AES;
import pg.student.securefilesender.services.RSA.RSA;


import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;


public class Client implements Runnable {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private ListView filesUploadedList;
    ObservableList<File> listOfFilesUploaded;
    String ipAddressName;

    private PublicKey rsaPublicKey;
    private AES aes;
    private RSA rsa;
    private SecretKey AesKey;
    private SecretKey encryptedAesKey;
    private IvParameterSpec iv;

    private File testFile;

    Socket socket;

    public Client(ListView filesUploadedList, String ipAddressName) {
        this.filesUploadedList = filesUploadedList;
        this.ipAddressName = ipAddressName;
    }

    public void sendFile(){
        try {

            this.socket = new Socket(ipAddressName, 5000);

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            ObjectOutputStream  out = new ObjectOutputStream (socket.getOutputStream());
            ObjectInputStream  in = new ObjectInputStream (socket.getInputStream());

            this.rsaPublicKey = (PublicKey) in.readObject();
            encryption(rsaPublicKey);
            out.writeObject(encryptedAesKey);
            out.writeObject(iv);

            this.listOfFilesUploaded = filesUploadedList.getItems();
            this.testFile = listOfFilesUploaded.get(0);
            FileInputStream fileInputStream = new FileInputStream(testFile);
            dataOutputStream.writeLong(testFile.length());
            int bytes = 0;
            byte[] buffer = new byte[4*1024];
            while ((bytes=fileInputStream.read(buffer))!=-1){
                dataOutputStream.write(buffer,0,bytes);
                dataOutputStream.flush();
            }

            fileInputStream.close();
            dataInputStream.close();
            dataInputStream.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    void encryption(PublicKey rsaPublicKey){
        this.aes = new AES();
        this.rsa = new RSA();
        this.aes.generateKeyAes(128);
        this.AesKey = aes.getKey();
        this.iv = aes.getIv();
        try {
            this.encryptedAesKey = rsa.encrypt(this.AesKey, rsaPublicKey);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    @Override
    public void run() {
        sendFile();
    }
}
