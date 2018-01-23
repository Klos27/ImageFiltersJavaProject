//singleton
package ProcessingServer.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessingServerPriority extends Thread{
    public static boolean isServerRunning = false;
    int serverPort;
    ExecutorService executor;
    ServerSocket listenSocket = null;
    public ProcessingServerPriority(int serverPort){
        this.serverPort = serverPort;
        executor = Executors.newCachedThreadPool();
    }
    public void close(){
        executor.shutdown();
        isServerRunning = false;
        if(listenSocket != null)
            try {
                listenSocket.close();
                System.out.println("Server socket closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    @Override
    public void run() {
        try{
            listenSocket = new ServerSocket(serverPort);
            System.out.println("ServerPriority starts listening...");

            isServerRunning = true;
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                executor.execute(new ProcessingServerPriorityConnection(clientSocket));
            }
        }
        catch(IOException e) {
            System.out.println("Listen : "+e.getMessage());
        }
        finally {
            if(listenSocket != null)
                try {
                    listenSocket.close();
                    System.out.println("Priority server socket closed!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}