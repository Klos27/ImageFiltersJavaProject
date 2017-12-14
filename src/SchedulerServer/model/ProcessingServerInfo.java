package SchedulerServer.model;

public class ProcessingServerInfo {
    String ipAddress;
    int port;
    long load;
    public ProcessingServerInfo(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }
    public ProcessingServerInfo(String ipAddress, int port, long load){
        this.ipAddress = ipAddress;
        this.port = port;
        this.load = load;
    }
    public void refreshLoad(){
        //TODO connect to server and get number of clients
        // catch not connected -> load = -1 (processing server off)
        load = 0;
    }
}
