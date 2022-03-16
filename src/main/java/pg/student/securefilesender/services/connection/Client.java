package pg.student.securefilesender.services.connection;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import pg.student.securefilesender.HelloApplication;
import pg.student.securefilesender.controllers.HelloController;
import pg.student.securefilesender.models.MainModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private ListView filesUploadedList;
    ObservableList<File> listOfFilesUploaded;

    public Client(ListView filesUploadedList) {
        this.filesUploadedList = filesUploadedList;
    }

    public void sendFile(){
        try {

            Socket socket = new Socket("localhost", 5000);

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            listOfFilesUploaded = filesUploadedList.getItems();
            //test
            File testFile = listOfFilesUploaded.get(0);
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



    @Override
    public void run() {
        sendFile();
    }
}
