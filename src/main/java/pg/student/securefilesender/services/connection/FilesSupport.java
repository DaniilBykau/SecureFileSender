package pg.student.securefilesender.services.connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pg.student.securefilesender.models.MainModel;

import java.io.File;

public class FilesSupport {
    private MainModel model;
    private Client client;
    private Server server;

    private ObservableList<File> filesUploaded = FXCollections.observableArrayList();
    private ObservableList<File> filesReceived = FXCollections.observableArrayList();

    public ObservableList<File> getFilesUploaded() {
        return filesUploaded;
    }

    public FilesSupport(MainModel model) {
        this.model = model;
    }

    public void uploadFile(ActionEvent event, ListView filesUploadedList, ListView filesReceivedList) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();              //stage

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);          //file

        model = new MainModel(filesUploadedList);
        filesUploaded.add(selectedFile);
        filesUploadedList.setItems(filesUploaded);

        Thread t1 = new Thread(new Client(filesUploadedList));
        t1.start();

//        Server server = new Server(filesReceivedList);
//        server.startServer();
    }

    public void receiveFile(ActionEvent event, ListView filesReceivedList){
        Server server = new Server(filesReceivedList);
        server.startServer();
    }
}
