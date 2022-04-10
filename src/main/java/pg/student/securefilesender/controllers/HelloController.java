package pg.student.securefilesender.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import pg.student.securefilesender.services.connection.Support;

public class HelloController  {

    public Text serverStatus;
    public ProgressBar progressBarSend;
    public ProgressBar progressBarEncrypt;
    public Button buttonCloseConnection;
    public Button buttonWaitConnection;
    public Button buttonUploadFile;
    public Button buttonChooseFile;

    private Support filesSupport = new Support();

    @FXML
    private ListView filesUploadedList;

    @FXML
    private ListView filesReceivedList;

    @FXML
    private TextField nameIP;

    @FXML
    public void uploadFile(ActionEvent actionEvent) {
        filesSupport.uploadFile(actionEvent, serverStatus);
    }

    @FXML
    public void startConnection(ActionEvent actionEvent) {          //server
        filesSupport.startConnection(actionEvent, filesReceivedList, serverStatus, buttonWaitConnection, buttonCloseConnection);

    }

    public void closeConnection(ActionEvent actionEvent) {
        filesSupport.closeConnection(actionEvent, serverStatus);
    }

    public void waitConnection(ActionEvent actionEvent) {
        filesSupport.waitConnection(actionEvent, serverStatus);
    }

    public void Connect(ActionEvent actionEvent) {                  //client
        String ipAddressName = nameIP.getText().toString();
        filesSupport.connectToServer(actionEvent, filesUploadedList, ipAddressName, progressBarSend, progressBarEncrypt, serverStatus, buttonChooseFile);
    }

    public void chooseFile(ActionEvent actionEvent) {
        filesSupport.chooseFile(actionEvent, serverStatus, buttonUploadFile);
    }
}