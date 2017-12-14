//singleton
package ProcessingServer.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessingServerPriority extends Thread{
    @Override
    public void run() {
        try{
            int serverPort = 55002;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            System.out.println("ServerPriority starts listening...");

            while(!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                ProcessingServerPriorityConnection c = new ProcessingServerPriorityConnection(clientSocket);
            }
            //TODO close all connections if you want to close server
        }
        catch(IOException e) {
            System.out.println("Listen :"+e.getMessage());
        }
    }
}