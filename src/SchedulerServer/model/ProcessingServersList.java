package SchedulerServer.model;

import java.util.ArrayList;

// singleton
public class ProcessingServersList {
   static ArrayList<ProcessingServerInfo> serverslist;

   public void clearList(){
       serverslist.clear();
   }

   public ProcessingServersList(){
       serverslist = new ArrayList<ProcessingServerInfo>();
   }
    private void addServer(String ipAddress, int port){
       serverslist.add(new ProcessingServerInfo(ipAddress,port));
    }
    public void loadList(){
        //TODO load list from file
        addServer("localhost", 50000);
    }

    private static void refreshList(){
        while(serverslist.iterator().hasNext()){
            serverslist.iterator().next().refreshLoad();
        }
    }

    public static ProcessingServerInfo getBestServer() throws Exception {
//        refreshList();
        ProcessingServerInfo bestServ = new ProcessingServerInfo("0.0.0.0", 0, Long.MAX_VALUE);
        ProcessingServerInfo tmp;
        while(serverslist.iterator().hasNext()){
            tmp = serverslist.iterator().next();
            tmp.refreshLoad();
            if((tmp.load >= 0) && tmp.load <= bestServ.load){
                bestServ = tmp;
            }
        }
        return bestServ;


    }



}
