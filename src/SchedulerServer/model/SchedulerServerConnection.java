package SchedulerServer.model;

import java.net.*;
import java.io.*;

public class SchedulerServerConnection implements Runnable {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;

    public SchedulerServerConnection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream( clientSocket.getInputStream());
            output = new DataOutputStream( clientSocket.getOutputStream());
        }
        catch(IOException e) {
            System.out.println("Server Connection problem: "+e.getMessage());
        }
    }

    public void run() {
        try {
            // Get the best server
            System.out.println("Scheduler: get the best processing server");
            ProcessingServerInfo bestServ = ProcessingServersList.getBestServer();

            // Send IP and port to Client
            System.out.println("Send info to client");
            output.writeUTF(bestServ.ipAddress);
            output.writeInt(bestServ.processingPort);
        }
        catch(Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        finally {
            try {
                clientSocket.close();
            }
            catch (IOException e){
                // Close failed
            }
        }
    }
}