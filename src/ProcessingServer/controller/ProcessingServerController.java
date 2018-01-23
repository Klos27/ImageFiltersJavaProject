package ProcessingServer.controller;

//javaFX
import ProcessingServer.model.ProcessingServer;
import ProcessingServer.model.ProcessingServerPriority;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//javaNet

public class ProcessingServerController {
    boolean ifServerIsRunning = false;
    static ProcessingServer server = null;
    static ProcessingServerPriority serverPriority = null;
    public static boolean safeServerClose = false;
    private static int serverPort;
    private static int loadInfoPort;

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
    @FXML
    private TextField loadInfoPortField;
    //Set information to Text Area
    @FXML
    private void setInfoToResultArea(String infoText) {
        resultArea.setText(infoText);
    }
    @FXML
    private void appendInfoToResultArea(String infoText) {
        resultArea.appendText("\n" + infoText);
    }

    public static ProcessingServer getServer(){
        return server;
    }
    public static ProcessingServerPriority getPriorityServer(){
        return serverPriority;
    }

    //start ProcessingServer
    @FXML
    private void startServer(ActionEvent actionEvent) throws ClassNotFoundException {
        resultArea.setVisible(true);
        appendInfoToResultArea("Server Start...");
        try {
            if (!ifServerIsRunning) {
                serverPort = Integer.parseInt(serverPortField.getText());
                loadInfoPort = Integer.parseInt(loadInfoPortField.getText());
                server = new ProcessingServer(serverPort);
                serverPriority = new ProcessingServerPriority(loadInfoPort);
                server.start();
                serverPriority.start();
                ifServerIsRunning = true;
                startServerBtn.setVisible(false);
                closeServerBtn.setVisible(true);
                serverPortField.setVisible(false);
                serverPortLabel.setVisible(false);
                loadInfoLabel.setVisible(false);
                loadInfoPortField.setVisible(false);
                infoLabel.setVisible(false);
                resultArea.setText("Server is running");
            } else {
                appendInfoToResultArea("ProcessingServer is already running");
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
    //close ProcessingServer
    @FXML
    private void closeServer(ActionEvent actionEvent) throws ClassNotFoundException {
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
        if(serverPriority != null && !serverPriority.isInterrupted()){
            serverPriority.close();
            serverPriority.interrupt();
        }
    }
    public static int getServerPort(){
        return serverPort;
    }
    public static int getLoadInfoPort(){
        return loadInfoPort;
    }
}

