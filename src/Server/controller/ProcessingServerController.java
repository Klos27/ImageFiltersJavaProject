package Server.controller;

//javaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
//javaNet
import java.net.*;
import java.io.*;
import Server.model.Connection;

public class ProcessingServerController extends Thread {
    static boolean ifServerIsRunning = false;
    static long numberOfClients = 0;

    @FXML
    private TextArea resultArea;

    //Set information to Text Area
    @FXML
    private void setInfoToResultArea(String infoText) {
        resultArea.setText(infoText);
    }
    @FXML
    private void appendInfoToResultArea(String infoText) {
        resultArea.appendText("\n" + infoText);
    }

    //start Server
    @FXML
    private void startServer(ActionEvent actionEvent) throws ClassNotFoundException {
        appendInfoToResultArea("ServerStart...");
        try {
            if (!ifServerIsRunning) {
                this.start();
                ifServerIsRunning = true;
                appendInfoToResultArea("Number of Clients: " + numberOfClients);
            } else {
                appendInfoToResultArea("Server is already running");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Starting server failed");
        }
    }
    //close Server
    @FXML
    private void closeServer(ActionEvent actionEvent) throws ClassNotFoundException {
        appendInfoToResultArea("ServerClose...");
        try {
            if (ifServerIsRunning) {
                //TODO closing server, how to kill thread?

                //this.start();
                ifServerIsRunning = false;
                //appendInfoToResultArea("Number of Clients: " + numberOfClients);
            } else {
                appendInfoToResultArea("Server is already closed");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Closing server failed");
        }
    }
    public void run() {
        try{
            // TODO change to another port
            int serverPort = 6880;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.out.println("server start listening... ... ...");

            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
                numberOfClients++;
                appendInfoToResultArea("Number of Clients: " + numberOfClients);
            }
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());}
    }
}

