package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

public class FilesSupport {

    private ObservableList<File> filesUploaded = FXCollections.observableArrayList();

    public void uploadFile(ActionEvent event, ListView filesUploadedList, String ipAddressName ) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();              //stage

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);          //file

        filesUploaded.add(selectedFile);
        filesUploadedList.setItems(filesUploaded);

        Thread t1 = new Thread(new Client(filesUploadedList, ipAddressName));
        t1.start();
    }

    public void receiveFile(ActionEvent event, ListView filesReceivedList){
        Server server = new Server(filesReceivedList);
        server.startServer();
    }
}
