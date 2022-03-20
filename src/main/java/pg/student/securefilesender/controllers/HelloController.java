package pg.student.securefilesender.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pg.student.securefilesender.services.connection.FilesSupport;

public class HelloController  {

    private FilesSupport filesSupport;
    @FXML
    private ListView filesUploadedList;

    @FXML
    private ListView filesReceivedList;

    @FXML
    private TextField nameIP;

    @FXML
    public void uploadFile(ActionEvent actionEvent) {

        filesSupport = new FilesSupport();
        String ipAddressName = nameIP.getText().toString();
        filesSupport.uploadFile(actionEvent, filesUploadedList, ipAddressName);
    }

    @FXML
    public void receiveFile(ActionEvent actionEvent) {

        filesSupport = new FilesSupport();
        filesSupport.receiveFile(actionEvent, filesReceivedList);
    }
}