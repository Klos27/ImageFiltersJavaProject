package Server.model;
import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchedulerServerConnection extends Thread {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;
    final int bufferSize = 65536; // max size 65536 bytes [64KB]
    private String clientsFileName;  // name of the file from client
    private String processedFileName;   // name of the file to send to client
    static long fileNo = 0;

    public SchedulerServerConnection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream( clientSocket.getInputStream());
            output = new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        }
        catch(IOException e) {
            System.out.println("ProcessingServerConnection problem: "+e.getMessage());
        }
    }

    public void run() {
        try {
        //TODO Connect to clients
        // Get Processing servers IP and Port from file
        // Connect to Processing servers and get their number of clients
        // Chose better server
        // Send IP and port to Client
        }
        catch(Exception e) {
            System.out.println("Error: "+e.getMessage());}
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