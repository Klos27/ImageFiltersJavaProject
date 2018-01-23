package SchedulerServer.controller;

//javaFX
import SchedulerServer.model.SchedulerServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//javaNet


public class SchedulerServerController {
    static boolean ifServerIsRunning = false;
    static SchedulerServer server = null;
    public static boolean safeServerClose = false;
    private static int serverPort;

    @FXML
    private TextArea resultArea;
    @FXML
    private Button startServerBtn;
    @FXML
    private Button closeServerBtn;
    @FXML
    private Label infoLabel;
    @FXML
    private Label serverPortLabel;
    @FXML
    private Label loadInfoLabel;
    @FXML
    private TextField serverPortField;

    //Set information to Text Area
    @FXML
    private void setInfoToResultArea(String infoText) {
        resultArea.setText(infoText);
    }
    @FXML
    private void appendInfoToResultArea(String infoText) {
        resultArea.appendText("\n" + infoText);
    }

    public static SchedulerServer getServer(){
        return server;
    }
    //start ProcessingServer
    @FXML
    private void startServer(ActionEvent actionEvent) throws ClassNotFoundException {
        resultArea.setVisible(true);
        appendInfoToResultArea("ServerStart...");
        try {
            if (!ifServerIsRunning) {
                serverPort = Integer.parseInt(serverPortField.getText());
                server = new SchedulerServer(serverPort);
                server.start();
                ifServerIsRunning = true;
                startServerBtn.setVisible(false);
                closeServerBtn.setVisible(true);
                serverPortField.setVisible(false);
                serverPortLabel.setVisible(false);
                loadInfoLabel.setVisible(false);
                infoLabel.setVisible(false);
                resultArea.setText("Server is running");
            } else {
                appendInfoToResultArea("SchedulerServer is already running");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Starting server failed");
            appendInfoToResultArea("Please try again or restart app");
            infoLabel.setVisible(true);
            stopServers();
            ifServerIsRunning = false;
        }
    }
    //close SchedulerServer
    @FXML
    private void closeServer(ActionEvent actionEvent){
        appendInfoToResultArea("ServerClose...");
        // safe server Closing
        safeServerClose = true;
        stopServers();
        Platform.exit();
    }
    public static void stopServers(){
        if(server != null && !server.isInterrupted()){
            server.close();
            server.interrupt();
        }
    }
    public static int getServerPort(){
        return serverPort;
    }
}

