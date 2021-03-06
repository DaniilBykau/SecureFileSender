package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import pg.student.securefilesender.controllers.HelloController;
import pg.student.securefilesender.services.AES.AES;
import pg.student.securefilesender.services.RSA.RSA;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    private ObjectOutputStream outObject = null;
    private ObjectInputStream inObject = null;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private RSA rsa;
    byte [] aesEncryptedAes = new byte[64];
    private SecretKey aesKey;
    private byte[] iv = new byte [16];
    private AES aes;

    private File fileEncrypted;
    private String fileName;
    private String partFileName;
    private String partFileAfterDot;

    private ListView filesReceivedList;
    private ObservableList<File> listOfFilesUploaded = FXCollections.observableArrayList();
    private String securityType;

    public Server(ListView filesReceivedList) {
        this.filesReceivedList = filesReceivedList;
    }

    public void startServer(Text serverStatus) {
        try {
            this.serverSocket = new ServerSocket(5000);
            System.out.println("listening to port:5000");
            serverStatus.setText("Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(Text serverStatus) {
        try {
            this.clientSocket.close();
            this.serverSocket.close();
            serverStatus.setText("Connection closed");
            dataInputStream.close();
            dataOutputStream.close();
            outObject.close();
            inObject.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitConnection(Text serverStatus) {
        try {
            this.clientSocket = serverSocket.accept();
            serverStatus.setText("client connected");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            rsa = new RSA();
            rsa.initRSA();
            sendRSA();
            getEncryptedAes();
            receiveFile();
            if(fileEncrypted!=null){
                dataOutputStream.writeUTF("received");
                dataOutputStream.flush();
            }
            aes = new AES();
            aes.decryptFile(this.iv, this.aesKey, this.fileEncrypted, this.partFileName, this.partFileAfterDot, this.securityType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRSA() {
        try {

            this.outObject = new ObjectOutputStream (clientSocket.getOutputStream());
            this.inObject  = new ObjectInputStream (clientSocket.getInputStream());

            rsa = new RSA();
            rsa.initRSA();
            PublicKey rsaPublicKey = rsa.getPublicKey();
            this.outObject.writeObject(rsaPublicKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEncryptedAes() {
        try {
            this.dataInputStream.read(this.aesEncryptedAes);
            this.dataInputStream.read(this.iv);
            this.fileName = this.dataInputStream.readUTF();
            this.securityType = this.dataInputStream.readUTF();
            this.aesKey = rsa.decrypt(this.aesEncryptedAes, rsa.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveFile() {
        try {
            int bytes = 0;

            String[] partsFileName = this.fileName.split("\\.");
            this.partFileName = partsFileName[0]; // fileName
            this.partFileAfterDot = partsFileName[1]; // np .txt .mp3

            FileOutputStream fileOutputStream = new FileOutputStream(this.partFileName + "En." + this.partFileAfterDot);

            long size = this.dataInputStream.readLong();     // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = this.dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;      // read upto file size
            }
            this.fileEncrypted = new File(this.partFileName + "En." + this.partFileAfterDot);

            listOfFilesUploaded.add(this.fileEncrypted);
            filesReceivedList.setItems(listOfFilesUploaded);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
