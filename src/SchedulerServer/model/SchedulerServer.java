//singleton
package SchedulerServer.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SchedulerServer extends Thread{
    ProcessingServersList serversList;
    private static final Object synchronizer = new Object();
    int serverPort;
    ExecutorService executor;
    ServerSocket listenSocket = null;
    public static boolean isServerRunning = false;
    private static long serverLoad = 0;

    public SchedulerServer(int serverPort){
        this.serverPort = serverPort;
        executor = Executors.newCachedThreadPool();
        serversList = new ProcessingServersList();
    }
    public void close(){
        executor.shutdown();
        isServerRunning = false;
        if(listenSocket != null)
            try {
                listenSocket.close();
                System.out.println("Scheduler server socket closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public static void addLoad(){
        synchronized(synchronizer){
            serverLoad++;
        }
    }
    public static void subLoad(){
        synchronized (synchronizer){
            serverLoad--;
        }
    }
    public static long getLoad(){
        return serverLoad;
    }

    @Override
    public void run() {
        try{
            listenSocket = new ServerSocket(serverPort);

            System.out.println("Server starts listening...");
            isServerRunning = true;

            while(!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                executor.execute(new SchedulerServerConnection(clientSocket));
            }
        }
        catch(IOException e) {
            System.out.println("Listen : "+e.getMessage());
        }
        finally {
            if(listenSocket != null)
                try {
                    listenSocket.close();
                    System.out.println("Processing server socket closed!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}