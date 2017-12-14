package SchedulerServer.model;

import java.net.*;
import java.io.*;

public class SchedulerServerConnection extends Thread {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;

    public SchedulerServerConnection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream( clientSocket.getInputStream());
            output = new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        }
        catch(IOException e) {
            System.out.println("SchedulerServerConnection problem: "+e.getMessage());
        }
    }

    public void run() {
        try {
        // Get the best server
            ProcessingServerInfo bestServ = ProcessingServersList.getBestServer();
        // Send IP and port to Client
            output.writeUTF(bestServ.ipAddress);
            output.writeInt(bestServ.port);
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