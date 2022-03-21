package pg.student.securefilesender.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pg.student.securefilesender.services.connection.Support;

public class HelloController  {

    public Text serverStatus;

    private Support filesSupport = new Support();

    @FXML
    private ListView filesUploadedList;

    @FXML
    private ListView filesReceivedList;

    @FXML
    private TextField nameIP;

    @FXML
    public void uploadFile(ActionEvent actionEvent) {
        String ipAddressName = nameIP.getText().toString();
        filesSupport.uploadFile(actionEvent, filesUploadedList, ipAddressName);
    }

    @FXML
    public void startConnection(ActionEvent actionEvent) {
        filesSupport.startConnection(actionEvent, filesReceivedList, serverStatus);
    }

    public void closeConnection(ActionEvent actionEvent) {
        filesSupport.closeConnection(actionEvent, serverStatus);
    }

    public void waitConnection(ActionEvent actionEvent) {
        filesSupport.waitConnection(actionEvent, serverStatus);
    }
}