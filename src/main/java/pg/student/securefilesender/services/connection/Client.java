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
    private ObservableList<File> listOfFilesUploaded;
    private String ipAddressName;

    private PublicKey rsaPublicKey;
    private AES aes;
    private RSA rsa;
    private SecretKey AesKey;
    private byte [] encryptedAesKey;
    private byte[] iv;

    private File testFile;
    private String fileName;

    private Socket socket;

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

            this.listOfFilesUploaded = filesUploadedList.getItems();
            this.testFile = listOfFilesUploaded.get(0);

            this.rsaPublicKey = (PublicKey) in.readObject();            //get Rsa
            encryption(rsaPublicKey);                                   //encrypt Rsa
            dataOutputStream.write(encryptedAesKey);
            dataOutputStream.write(iv);
            dataOutputStream.writeUTF(testFile.getName());
            dataOutputStream.flush();                                   //send AES

            String[] partsFileName = testFile.getName().split("\\.");

            aes.encryptFile(this.iv, this.AesKey, this.testFile, partsFileName[0], partsFileName[1]);
            File fileToSend = new File(partsFileName[0]+ "En." + partsFileName[1]);
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            dataOutputStream.writeLong(fileToSend.length());
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
