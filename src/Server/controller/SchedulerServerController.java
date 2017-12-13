package Server.controller;

//javaFX
import Server.model.SchedulerServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
//javaNet


public class SchedulerServerController {
    boolean ifServerIsRunning = false;
    long numberOfClients = 0;
    SchedulerServer server = new SchedulerServer();

    @FXML
    private TextArea resultArea;
    @FXML
    private Button startServerBtn;
    @FXML
    private Button closeServerBtn;

    //Set information to Text Area
    @FXML
    private void setInfoToResultArea(String infoText) {
        resultArea.setText(infoText);
    }
    @FXML
    private void appendInfoToResultArea(String infoText) {
        resultArea.appendText("\n" + infoText);
    }

    //start ProcessingServer
    @FXML
    private void startServer(ActionEvent actionEvent) throws ClassNotFoundException {
        appendInfoToResultArea("ServerStart...");
        try {
            if (!ifServerIsRunning) {
                server.start();
                ifServerIsRunning = true;
                appendInfoToResultArea("Number of Clients: " + numberOfClients);
                startServerBtn.setVisible(false);
                closeServerBtn.setVisible(true);
            } else {
                appendInfoToResultArea("SchedulerServer is already running");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Starting server failed");
        }
    }
    //close SchedulerServer
    @FXML
    private void closeServer(ActionEvent actionEvent) throws ClassNotFoundException {
        appendInfoToResultArea("ServerClose...");
        try {
            if (ifServerIsRunning) {
                //TODO closing server, how to kill thread?
                server.interrupt();
                //this.start();
                ifServerIsRunning = false;
                //appendInfoToResultArea("Number of Clients: " + numberOfClients);
                startServerBtn.setVisible(true);
                closeServerBtn.setVisible(false);
            } else {
                appendInfoToResultArea("SchedulerServer is already closed");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Closing server failed");
        }
    }
}

