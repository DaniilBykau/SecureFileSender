package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

public class Support {

    Server server;
    Client client;

    public void connectToServer(ActionEvent event, ListView filesUploadedList, String ipAddressName, ProgressBar progressBarSend, ProgressBar progressBarEncrypt, Text serverStatus, Button buttonChooseFile) {
        try {
            this.client = new Client(filesUploadedList, ipAddressName, progressBarSend, progressBarEncrypt, serverStatus, event);
            this.client.startSocket();
            buttonChooseFile.setVisible(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startConnection(ActionEvent event, ListView filesReceivedList, Text serverStatus, Button buttonCloseConnection, Button buttonWaitConnection){
        this.server = new Server(filesReceivedList);
        this.server.startServer(serverStatus);
        buttonCloseConnection.setVisible(true);
        buttonWaitConnection.setVisible(true);
    }

    public void closeConnection(ActionEvent event, Text serverStatus){
        this.server.closeConnection(serverStatus);
    }

    public void waitConnection(ActionEvent event, Text serverStatus){
        this.server.waitConnection(serverStatus);
    }

    public void uploadFile(ActionEvent actionEvent, Text serverStatus) {
        Thread t1 = new Thread(this.client);
        t1.start();
    }

    public void chooseFile(ActionEvent actionEvent, Text serverStatus, Button buttonUploadFile) {
        try {
            this.client.chooseFile();
            buttonUploadFile.setVisible(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
