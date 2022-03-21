package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

public class Support {

    private ObservableList<File> filesUploaded = FXCollections.observableArrayList();
    Server server;

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

    public void startConnection(ActionEvent event, ListView filesReceivedList, Text serverStatus){
        server = new Server(filesReceivedList);
        server.startServer(serverStatus);
    }

    public void closeConnection(ActionEvent event, Text serverStatus){
        server.closeConnection(serverStatus);
    }

    public void waitConnection(ActionEvent event, Text serverStatus){
        server.waitConnection(serverStatus);
    }
}
