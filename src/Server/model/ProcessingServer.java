//singleton
package Server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessingServer extends Thread{
    long numberOfClients = 0;
    @Override
    public void run() {
        try{
            int serverPort = 55000;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.out.println("Server starts listening...");

            while(!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
                numberOfClients++;
               // appendInfoToResultArea("Number of Clients: " + numberOfClients);
            }
            //TODO close all connections if you want to close server
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());
        }
    }
}

