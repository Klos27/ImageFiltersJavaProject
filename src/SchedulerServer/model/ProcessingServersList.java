package SchedulerServer.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

// singleton
public class ProcessingServersList {
   static ArrayList<ProcessingServerInfo> serverslist;
   static Path fileIn = FileSystems.getDefault().getPath("processingServersList", "processingServers.txt");

   public void clearList(){
       serverslist.clear();
   }

   public ProcessingServersList(){
       serverslist = new ArrayList<ProcessingServerInfo>();
       loadList();
   }
    private void addServer(String ipAddress, int port){
       serverslist.add(new ProcessingServerInfo(ipAddress,port));
    }

    public void loadList(){
        clearList();
        if(!fileIn.toFile().exists()){
            File theDir = new File(fileIn.getParent().toString());
            // if the directory does not exist, create it
            if (!theDir.exists()) {
                System.out.println("Creating directory: " + theDir.getName());
                try {
                    theDir.mkdir();
                } catch (SecurityException e) {
                    System.out.println("You do not have enough rights to create this folder");
                }
            }
            try (BufferedWriter writer = Files.newBufferedWriter(fileIn, StandardCharsets.UTF_8)) {
                System.out.println("Creating file: " + fileIn.toString());
                String writeToFile = "# lines which starts with # are comments\n" +
                        "# but remember you cannot put any space before #\n" +
                        "# place there any number of processing servers\n" +
                        "# They are listed as IP_address;Port  where IP_addres could be string or numbers\n" +
                        "#And Port has to be loadInfoPort, not processingPort!\n" +
                        "#Servers could be off, it doesn't matter\n" +
                        "# Default server is localhost;55002 and localhost;55003\n" +
                        "localhost;55002\n" +
                        "localhost;55003";
                writer.write(writeToFile, 0, writeToFile.length());
            } catch (java.io.IOException e) {
                System.err.format("IOException: %s%n", e);
            }
        }

        // Read servers list from file
        try (BufferedReader reader = Files.newBufferedReader(fileIn, StandardCharsets.UTF_8)) {
            String line = null;
            String servIP = null;
            String servPort = null;
            int servPortInt = 0;
            while ((line = reader.readLine()) != null) {
                if (!(line == "")) {
                    try {
                        // # is a comment
                        if(!line.startsWith("#") && line.lastIndexOf(";") > 0) {
                            System.out.println(line); // only for tests
                            servIP = line.substring(0,line.lastIndexOf(";"));
                            servPort = line.substring(line.lastIndexOf(";") + 1);
                            System.out.println("IP: " + servIP + "  Port: " + servPort);
                            servPortInt = Integer.parseInt(servPort);
                            if(servPortInt > 0 && servPortInt < 65535)
                                addServer(servIP, servPortInt);
                            else
                                System.out.println("Server port out of range");
                        }
                    } catch (Exception e) {
                        System.out.println("Incorrect Line");
                    }
                }
            }
            if(serverslist.isEmpty()){
                addServer("0.0.0.0", 0);
            }
            reader.close();
        }
        catch(java.nio.file.NoSuchFileException e){
            System.err.format("IOException: %s%n", e);
        }
        catch (java.io.IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    private static void refreshList(){
        for(int i = 0; i < serverslist.size(); i++){
            serverslist.get(i).refreshLoad();
        }
    }

    public static ProcessingServerInfo getBestServer() {
        //refreshList();    // better do it in loop below
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
