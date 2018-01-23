package ProcessingServer.controller;

import ProcessingServer.model.ProcessingServer;
import ProcessingServer.model.ProcessingServerPriority;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;


public class ProcessingServerController {
    static boolean ifServerIsRunning = false;
    static ProcessingServer server = null;
    static ProcessingServerPriority serverPriority = null;
    public static boolean safeServerClose = false;
    private static int serverPort;
    private static int loadInfoPort;

    @FXML
    private TextArea resultArea;
    @FXML
    private Button startServerBtn;
    @FXML
    private Button closeServerBtn;
    @FXML
    private Label infoLabel;
    @FXML
    private Label serverPortLabel;
    @FXML
    private Label loadInfoLabel;
    @FXML
    private TextField serverPortField;
    @FXML
    private TextField loadInfoPortField;
    //-------LOAD INFO
    static LoadUpdater loadUpdaterHolder = null;
    static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
            OperatingSystemMXBean.class);
    @FXML
    private Label serverLoadLabel;
    @FXML
    private Label ramFreeLabel;
    @FXML
    private Label cpuSystemLabel;
    @FXML
    private Label cpuServerLabel;
    @FXML
    private Label ramUsageLabel;
    @FXML
    private Label ramTotalLabel;
    @FXML
    private Label ramForAppLabel;
    @FXML
    private ProgressBar cpuSystemBar;
    @FXML
    private ProgressBar cpuServerBar;
    @FXML
    private ProgressBar ramUsageBar;

    //-------END OF LOAD INFO
    //Set information to Text Area
    @FXML
    private void setInfoToResultArea(String infoText) {
        resultArea.setText(infoText);
    }
    @FXML
    private void appendInfoToResultArea(String infoText) {
        resultArea.appendText("\n" + infoText);
    }

    public static ProcessingServer getServer(){
        return server;
    }
    public static ProcessingServerPriority getPriorityServer(){
        return serverPriority;
    }

    //start ProcessingServer
    @FXML
    private void startServer(ActionEvent actionEvent) throws ClassNotFoundException {
        resultArea.setVisible(true);
        appendInfoToResultArea("Server Start...");
        try {
            if (!ifServerIsRunning) {
                serverPort = Integer.parseInt(serverPortField.getText());
                loadInfoPort = Integer.parseInt(loadInfoPortField.getText());
                server = new ProcessingServer(serverPort);
                serverPriority = new ProcessingServerPriority(loadInfoPort);
                server.start();
                serverPriority.start();
                ifServerIsRunning = true;
                startServerBtn.setVisible(false);
                closeServerBtn.setVisible(true);
                serverPortField.setVisible(false);
                serverPortLabel.setVisible(false);
                loadInfoLabel.setVisible(false);
                loadInfoPortField.setVisible(false);
                infoLabel.setVisible(false);
                resultArea.setText("Server is running");
                loadUpdaterHolder = new LoadUpdater();
                loadUpdaterHolder.start();
            } else {
                appendInfoToResultArea("ProcessingServer is already running");
            }
        }
        catch(Exception e){
            appendInfoToResultArea("Starting server failed");
            appendInfoToResultArea("Please try again or restart app");
            infoLabel.setVisible(true);
            stopServers();
            ifServerIsRunning = false;
        }
    }
    //close ProcessingServer
    @FXML
    private void closeServer(ActionEvent actionEvent) {
        appendInfoToResultArea("ServerClose...");
        // safe server Closing
        safeServerClose = true;
        stopServers();
        Platform.exit();
    }
    public static void stopServers(){
        if(server != null && !server.isInterrupted()){
            server.close();
            server.interrupt();
        }
        if(serverPriority != null && !serverPriority.isInterrupted()){
            serverPriority.close();
            serverPriority.interrupt();
        }
        if(loadUpdaterHolder != null && !loadUpdaterHolder.isInterrupted()){
            loadUpdaterHolder.interrupt();
        }
    }
    public static int getServerPort(){
        return serverPort;
    }
    public static int getLoadInfoPort(){
        return loadInfoPort;
    }

    public void updateLoad(){
        // update all load info
        serverLoadLabel.setText(String.valueOf(server.getLoad() / 1048576) + " MB");
        ramFreeLabel.setText(String.valueOf(osBean.getFreePhysicalMemorySize() / 1048576) + " MB");
        ramForAppLabel.setText(String.valueOf(osBean.getCommittedVirtualMemorySize() / 1048576) + " MB");
        cpuSystemBar.setProgress(osBean.getSystemCpuLoad());
        cpuServerBar.setProgress(osBean.getProcessCpuLoad());
        ramUsageBar.setProgress((osBean.getTotalPhysicalMemorySize()- osBean.getFreePhysicalMemorySize())/(double)osBean.getTotalPhysicalMemorySize());
        cpuSystemLabel.setText(String.format("%.2f", osBean.getSystemCpuLoad() * 100) + "%");
        cpuServerLabel.setText(String.format("%.2f", osBean.getProcessCpuLoad() * 100) + "%");
        ramUsageLabel.setText(String.format("%.2f", ((osBean.getTotalPhysicalMemorySize()- osBean.getFreePhysicalMemorySize())/(double)osBean.getTotalPhysicalMemorySize()) * 100) + "%");
        ramTotalLabel.setText(String.valueOf(osBean.getTotalPhysicalMemorySize() / 1048576) + " MB");
    }

    class LoadUpdater extends Thread{
        public void run(){
            while(!Thread.currentThread().isInterrupted()){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateLoad();
                    }
                });
                try{
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    System.exit(0);
                }
            }
        }
    }
}

