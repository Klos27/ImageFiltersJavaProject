package ProcessingServer.model;

import java.io.*;
import java.net.Socket;

public class ProcessingServerPriorityConnection implements Runnable {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;

    public ProcessingServerPriorityConnection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            output = new DataOutputStream( clientSocket.getOutputStream());
        }
        catch(IOException e) {
            System.out.println("ProcessingServerPriorityConnection problem: "+e.getMessage());
        }
    }

    public void run() {
        try {
                output.writeLong(ProcessingServer.getLoad());
                output.flush();
            // END OF CONNECTION
        }
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage()); }
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());}
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