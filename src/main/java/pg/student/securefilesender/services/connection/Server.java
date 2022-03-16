package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import pg.student.securefilesender.controllers.HelloController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private ListView filesReceivedList;
    ObservableList<File> listOfFilesUploaded = FXCollections.observableArrayList();

    public Server(ListView filesReceivedList) {
        this.filesReceivedList = filesReceivedList;
    }

    public void startServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("listening to port:5000");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected.");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            Thread.sleep(5000);
            receiveFile();

            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }


    public void receiveFile(){
        try {
            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream("new_file.txt");

            long size = dataInputStream.readLong();     // read file size
            byte[] buffer = new byte[4*1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer,0,bytes);
                size -= bytes;      // read upto file size
            }
            File file = new File("new_file.txt");

            listOfFilesUploaded.add(file);
            filesReceivedList.setItems(listOfFilesUploaded);
            fileOutputStream.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    @Override
    public void run() {
        startServer();
    }

}
