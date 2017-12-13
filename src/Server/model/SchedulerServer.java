//singleton
package Server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SchedulerServer extends Thread{
    long numberOfClients = 0;
    @Override
    public void run() {
        try{
            //TODO Get port from config file?
            int serverPort = 55001;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.out.println("Server starts listening...");

            while(!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                SchedulerServerConnection c = new SchedulerServerConnection(clientSocket);
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