package pg.student.securefilesender.models;

import javafx.scene.control.ListView;

public class MainModel {


    private ListView filesUploadedList;

    public MainModel(ListView filesUploadedList) {
        this.filesUploadedList = filesUploadedList;
    }

    public ListView getFilesUploadedList() {
        return filesUploadedList;
    }
}
