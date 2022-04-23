package pg.student.securefilesender.services.connection;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;


public class Support {

    Server server;
    Client client;
    String securityType;

    public void connectToServer(ActionEvent event, ListView filesUploadedList, String ipAddressName, ProgressBar progressBarSend,
                                ProgressBar progressBarEncrypt, Text serverStatus, Button buttonChooseFile, RadioButton cbcRadioButton, RadioButton ofbRadioButton) {
        try {
            ToggleGroup groupSecurity = new ToggleGroup();
            cbcRadioButton.setToggleGroup(groupSecurity);
            ofbRadioButton.setToggleGroup(groupSecurity);
            if(ofbRadioButton.isSelected()){
                this.securityType = "OFB";
            }
            if(ofbRadioButton.isSelected()){
                this.securityType = "CBC";
            }
            if (this.securityType != null) {
                this.client = new Client(filesUploadedList, ipAddressName, progressBarSend, progressBarEncrypt, serverStatus, event, this.securityType);
                this.client.startSocket();
                buttonChooseFile.setVisible(true);
            } else {
                System.out.println("choose ofb or cbc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startConnection(ActionEvent event, ListView filesReceivedList, Text serverStatus, Button buttonCloseConnection,
                                Button buttonWaitConnection) {
        this.server = new Server(filesReceivedList);
        this.server.startServer(serverStatus);
        buttonCloseConnection.setVisible(true);
        buttonWaitConnection.setVisible(true);
    }

    public void closeConnection(ActionEvent event, Text serverStatus) {
        this.server.closeConnection(serverStatus);
    }

    public void waitConnection(ActionEvent event, Text serverStatus) {
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
            serverStatus.setText("File chosen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
