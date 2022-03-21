package pg.student.securefilesender.services.connection;

import javafx.collections.ObservableList;

import javafx.scene.control.ListView;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;


public class Client implements Runnable {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private ListView filesUploadedList;
    ObservableList<File> listOfFilesUploaded;
    String ipAddressName;

    Socket socket;

    public Client(ListView filesUploadedList, String ipAddressName) {
        this.filesUploadedList = filesUploadedList;
        this.ipAddressName = ipAddressName;
    }

    public void sendFile(){
        try {

            socket = new Socket(ipAddressName, 5000);

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
