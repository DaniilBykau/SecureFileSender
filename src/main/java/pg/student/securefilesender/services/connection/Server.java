package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import pg.student.securefilesender.controllers.HelloController;
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

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private static ObjectOutputStream outObject = null;
    private static ObjectInputStream inObject = null;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    RSA rsa;
    SecretKey aesEncryptedAes;
    SecretKey aesKey;
    private IvParameterSpec iv;

    private ListView filesReceivedList;
    ObservableList<File> listOfFilesUploaded = FXCollections.observableArrayList();

    public Server(ListView filesReceivedList) {
        this.filesReceivedList = filesReceivedList;
    }

    public void startServer(Text serverStatus) {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("listening to port:5000");
            serverStatus.setText("Server started");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void closeConnection(Text serverStatus) {
        try {
            clientSocket.close();
            serverStatus.setText("Connection closed");
            dataInputStream.close();
            dataOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void waitConnection(Text serverStatus) {
        try {
            clientSocket = serverSocket.accept();
            serverStatus.setText("client connected");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            rsa = new RSA();
            rsa.initRSA();
            sendRSA();
            getEncryptedAes();
            receiveFile();

        } catch (Exception e) {
            System.out.println(e.toString());
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
            System.out.println(e.toString());
        }
    }

    private void getEncryptedAes() {
        try {
            this.aesEncryptedAes = (SecretKey) this.inObject.readObject();
            this.iv = (IvParameterSpec) this.inObject.readObject();
            this.aesKey = rsa.decrypt(this.aesEncryptedAes, rsa.getPrivateKey());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void receiveFile() {
        try {
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\santi\\Downloads\\COMLab3\\COMLab3\\new_file.txt");

            long size = dataInputStream.readLong();     // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;      // read upto file size
            }
            File file = new File("new_file.txt");

            listOfFilesUploaded.add(file);
            filesReceivedList.setItems(listOfFilesUploaded);
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
