//singleton
package ProcessingServer.model;

import ProcessingServer.controller.ProcessingServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessingServer extends Thread{
    private static final Object synchronizer = new Object();
    private static final Object numberSynchronizer = new Object();
    int serverPort;
    ExecutorService executor;
    ServerSocket listenSocket = null;
    public static boolean isServerRunning = false;
    private static long serverLoad = 0;
    private static long clientNo = 0;

    public ProcessingServer(int serverPort){
        this.serverPort = serverPort;
        executor = Executors.newCachedThreadPool();
    }
    public void close(){
        executor.shutdown();
        isServerRunning = false;
        if(listenSocket != null)
            try {
                listenSocket.close();
                System.out.println("Processing server socket closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public static void addLoad(long loadValue){
        synchronized(synchronizer){
            serverLoad += loadValue;
        }
    }
    public static void subLoad(long loadValue){
        synchronized (synchronizer){
            serverLoad -= loadValue;
        }
    }
    public static long getLoad(){
        // version 1 - return only how much Bytes this server is processing
//        return serverLoad;
        // version 2 - return cpu load in %
        return ProcessingServerController.getServerLoad();
    }
    public static long getLoadedBytes(){
        return serverLoad;
    }

    public static long getClientNo(){
        synchronized (numberSynchronizer){
            clientNo++;
            return clientNo;
        }
    }

    @Override
    public void run() {
        try{
            listenSocket = new ServerSocket(serverPort);
            System.out.println("Processing server starts listening...");
            isServerRunning = true;
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = listenSocket.accept();
                executor.execute(new ProcessingServerConnection(clientSocket));
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

