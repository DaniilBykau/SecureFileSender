module pg.student.securefilesender {
    requires javafx.controls;
    requires javafx.fxml;


    opens pg.student.securefilesender to javafx.fxml;
    exports pg.student.securefilesender;
    exports pg.student.securefilesender.controllers;
    opens pg.student.securefilesender.controllers to javafx.fxml;
}