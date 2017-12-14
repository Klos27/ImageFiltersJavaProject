//singleton
package SchedulerServer.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SchedulerServer extends Thread{
    ProcessingServersList serversList;
    @Override
    public void run() {
        try{
            int serverPort = 55001;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            serversList = new ProcessingServersList();
            serversList.clearList();
            serversList.loadList();

            System.out.println("Server starts listening...");

            while(!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("New client");
                SchedulerServerConnection c = new SchedulerServerConnection(clientSocket);

            }
            //TODO close all connections if you want to close server
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());
        }
    }
}