package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pg.student.securefilesender.services.AES.AES;
import pg.student.securefilesender.services.RSA.RSA;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class Client implements Runnable {

    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    private ListView filesUploadedList;
    private ObservableList<File> listOfFilesUploaded;
    private String ipAddressName;
    private Text serverStatus;

    private PublicKey rsaPublicKey;
    private AES aes;
    private RSA rsa;
    private SecretKey AesKey;
    private byte [] encryptedAesKey;
    private byte[] iv;

    private File testFile;
    private String fileName;

    private Socket socket;

    private ProgressBar progressBarSend;
    private ProgressBar progressBarEncrypt;

    private ObservableList<File> filesUploaded = FXCollections.observableArrayList();
    private ActionEvent event;

    public Client(ListView filesUploadedList, String ipAddressName, ProgressBar progressBarSend, ProgressBar progressBarEncrypt, Text serverStatus, ActionEvent event) {
        this.filesUploadedList = filesUploadedList;
        this.ipAddressName = ipAddressName;
        this.progressBarSend = progressBarSend;
        this.progressBarEncrypt = progressBarEncrypt;
        this.serverStatus = serverStatus;
        this.event = event;
    }

    public void startSocket(){
        try {
            this.socket = new Socket(ipAddressName, 5000);
            System.out.println("Connected");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendFile(){
        try {


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

            aes.encryptFile(this.iv, this.AesKey, this.testFile, partsFileName[0], partsFileName[1], progressBarEncrypt);
            File fileToSend = new File(partsFileName[0]+ "En." + partsFileName[1]);
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            dataOutputStream.writeLong(fileToSend.length());
            int bytes = 0;
            progressBarSend.setProgress(0);
            byte[] buffer = new byte[4*1024];

            double loopTimes = (double) fileToSend.length() / buffer.length;
            double step = loopTimes != 0 ? (50 / loopTimes) : 100;
            double progress = 0;

            while ((bytes=fileInputStream.read(buffer))!=-1){
                dataOutputStream.write(buffer,0,bytes);
                dataOutputStream.flush();
                progress += step;
                progressBarSend.setProgress(progress);
            }

            fileInputStream.close();
            dataInputStream.close();
            dataInputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendFile();
    }

    public void chooseFile(){
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();              //stage

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);          //file

        filesUploaded.add(selectedFile);
        filesUploadedList.setItems(filesUploaded);
    }
}
