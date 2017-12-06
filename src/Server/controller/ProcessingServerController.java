package Server.controller;

//javaFX
import Server.model.ProcessingServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
//javaNet
import java.net.*;
import java.io.*;
import Server.model.Connection;

public class ProcessingServerController {
    boolean ifServerIsRunning = false;
    long numberOfClients = 0;
    ProcessingServer server = new ProcessingServer();

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
                appendInfoToResultArea("ProcessingServer is already running");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Starting server failed");
        }
    }
    //close ProcessingServer
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
                appendInfoToResultArea("ProcessingServer is already closed");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Closing server failed");
        }
    }

//    public void run() {
//        try{
//            int serverPort = 6880;
//            ServerSocket listenSocket = new ServerSocket(serverPort);
//
//            System.out.println("server start listening... ... ...");
//
//            while(true) {
//                Socket clientSocket = listenSocket.accept();
//                Connection c = new Connection(clientSocket);
//                numberOfClients++;
//                appendInfoToResultArea("Number of Clients: " + numberOfClients);
//            }
//        }
//        catch(IOException e) {
//            System.out.println("Listen :"+e.getMessage());}
//    }
}

