//singleton
package ProcessingServer.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessingServer extends Thread{
    private static long numberOfClients = 0;
    public static void addClient(){
        //TODO synchronize this
        numberOfClients++;
    }
    public static void removeClient(){
        //TODO synchronize this
        if(numberOfClients > 0)
            numberOfClients--;
        else
            numberOfClients = 0;
    }
    public static long getNumOfClients(){
        return numberOfClients;
    }

    @Override
    public void run() {
        try{
            int serverPort = 55000;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.out.println("Server starts listening...");

            while(!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                ProcessingServerConnection c = new ProcessingServerConnection(clientSocket);
                addClient();
            }
            //TODO close all connections if you want to close server
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());
        }
    }
}

