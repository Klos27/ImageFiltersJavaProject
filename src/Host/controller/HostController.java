package Host.controller;

import javafx.fxml.FXML;

public class HostController {
    String processingServerIP = "localhost";
    String processingServerPort = "55000";
    String myFilePath;  // File's path to send to server


    private void getProcessingServerIP() {
        //TODO get this form Scheduler server
        processingServerIP = "localhost";
        processingServerPort = "55000";
    }

    private void getFilePath() {
        //TODO get this from the textFiled GUI
        myFilePath = "input.jpg";
    }

    private String getMyFileName() {
        //TODO change this to extract only name + extansion from whole Path
        return "input.jpg";
    }

    @FXML
    private void processImage() {
        //1. Connect to Scheduler Server
        //2. Get Processing server ip and port
        //3. Connect to Processing Server
        //4. Send file name
        //5. Send file size
        //6. Send file
        //7. Wait for processing file
        //8. Get file size
        //9. Get processed File
        //10. Close connections

    }
}
