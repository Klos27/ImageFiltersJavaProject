package SchedulerServer.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ProcessingServerInfo {
    String ipAddress;
    int loadPort;
    int processingPort;
    long load;

    public ProcessingServerInfo(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.loadPort = port;
        this.load = -1; // server unavailable
        this.processingPort = 0;
    }
    public ProcessingServerInfo(String ipAddress, int port, long load){
        this.ipAddress = ipAddress;
        this.loadPort = port;
        this.load = load;
        this.processingPort = 0;
    }
    public void refreshLoad(){
        Socket soc = null;
        try{
            soc = new Socket(ipAddress, loadPort);
            DataInputStream input = new DataInputStream(soc.getInputStream());
//            DataOutputStream output = new DataOutputStream(soc.getOutputStream());
            long newLoad = input.readLong();
            int newServerProcessingPort = input.readInt();
            if(newLoad >= 0){
                load = newLoad;
            }
            if(newServerProcessingPort>0)
                processingPort = newServerProcessingPort;
        } catch (Exception e){
            load = -1;  // server unavailable
        }
        finally {
            if (soc != null)
                try {
                    soc.close();
                } catch (IOException e) {/*close failed*/}
        }
    }
}
