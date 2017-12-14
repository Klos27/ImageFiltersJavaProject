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
        addServer("localhost", 55000);
    }

    private static void refreshList(){
        for(int i = 0; i < serverslist.size(); i++){
            serverslist.get(i).refreshLoad();
        }
    }

    public static ProcessingServerInfo getBestServer() {
//        refreshList();
        ProcessingServerInfo bestServ = new ProcessingServerInfo("0.0.0.0", 0, Long.MAX_VALUE);
        ProcessingServerInfo tmp;
        for(int i = 0; i < serverslist.size(); i++){
            tmp = serverslist.get(i);
            tmp.refreshLoad();
            if((tmp.load >= 0) && tmp.load <= bestServ.load){
                bestServ = tmp;
            }
        }
        return bestServ;
    }



}
