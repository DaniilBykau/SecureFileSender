package pg.student.securefilesender.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import pg.student.securefilesender.models.MainModel;
import pg.student.securefilesender.services.connection.FilesSupport;

public class HelloController  {
    private MainModel model;
    private FilesSupport filesSupport;
    @FXML
    private ListView filesUploadedList;

    @FXML
    private ListView filesReceivedList;

    @FXML
    public void uploadFile(ActionEvent actionEvent) {
        model = new MainModel(filesUploadedList);
        filesSupport = new FilesSupport(model);
        filesSupport.uploadFile(actionEvent, filesUploadedList, filesReceivedList);

    }

    @FXML
    public void receiveFile(ActionEvent actionEvent) {
        model = new MainModel(filesUploadedList);
        filesSupport = new FilesSupport(model);
        filesSupport.receiveFile(actionEvent, filesReceivedList);
    }
}