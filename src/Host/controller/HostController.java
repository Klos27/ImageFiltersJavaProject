package Host.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class HostController {
    @FXML
    private TextArea consoleOutputArea;
    @FXML
    private Button processImageBtn;

    String processingServerIP = "localhost";
    int processingServerPort = 55000;
    Socket soc = null;
    final int bufferSize = 65536;   // max size 65536 bytes [64KB]
    String myInputFilePath = "D:\\input.jpg";  // File's path to send to server
    String myOutputFilePath = "D:\\output.jpg"; // File recived form server
    private boolean connectionIsRunning = false;
    int conversionType = 1;
    /*
    1 == Sepia (default)
    2 == Negative
    3 == MirrorImage
    4 == RedImage
    5 == GreenImage
    6 == BlueImage
    7 == ...
     */

    private void getProcessingServerIP() {
        //TODO get this form Scheduler server
        processingServerIP = "localhost";
        processingServerPort = 55000;
    }

    private void getFilePath() {
        //TODO get this from the textField GUI
        //myInputFilePath = "D:\\input.jpg";
        //myOutputFilePath = "D:\\output.jpg";
    }

    private String getMyFileName() {
        //TODO change this to extract only name + extansion from whole Path
        return "input.jpg";
    }
    @FXML
    private void appendTextToTextArea(String text){
        consoleOutputArea.appendText(text + "\n");
    }
    @FXML
    private void clearTextArea(){
        consoleOutputArea.setText("");
    }
    private long checkFileSize(String pathToFile){
        File myFile = new File(myInputFilePath);
        if(myFile.exists())
            return myFile.length();
        else
            return -1;
    }

    @FXML
    private void processImage() {
        if(connectionIsRunning == false) {
            try {
                Connection connection = new Connection();
                clearTextArea();
                appendTextToTextArea("Start");
                processImageBtn.setVisible(false);
                connection.start();

            } catch (Exception e) {
                appendTextToTextArea("Starting connection failed");
            }
        }
    }

    public void run() {
        // Check if fileSize is smaller than 20MB
        if(checkFileSize(myInputFilePath) < 1 || checkFileSize(myInputFilePath) > 20971520) {
            appendTextToTextArea("Select correct file!");
            appendTextToTextArea("File size: " + checkFileSize(myInputFilePath));
            appendTextToTextArea("File does not exist or file size is greater than 20MB");
            processImageBtn.setVisible(true);
        }
        else {
            appendTextToTextArea("Selected file is correct");
            // Connect to Scheduler Server
            //TODO this
            // Get Processing server ip and port
            //TODO this

            // Connect to Processing Server
            try {
                soc = new Socket(processingServerIP, processingServerPort);

                // Initialize buffers
                DataInputStream input = new DataInputStream(soc.getInputStream());
                DataOutputStream output = new DataOutputStream(soc.getOutputStream());
                File myFile = new File(myInputFilePath);
                byte[] buffer = new byte[(int) myFile.length()];

                // Send integer- conversionType
                output.writeInt(conversionType);

                // Send file name
                output.writeUTF(myFile.getName());
                appendTextToTextArea("File: " + myFile.getPath());

                // Send file size
                System.out.println("Length of myFile: " + myFile.length());
                appendTextToTextArea("Length of myFile: " + myFile.length());
                output.writeLong(myFile.length());

                // Send file
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                appendTextToTextArea("Sending file to server");
                int bytesSent;
                while ((bytesSent = bis.read(buffer, 0, bufferSize)) != -1) {
                    output.write(buffer, 0, bytesSent);
                }
                output.flush();

                // Close buffers
                bis.close();
                fis.close();
                appendTextToTextArea("File has been Sent");

                // Wait for processing file
                System.out.println("Our server is processing your file");

                // Get file size
                long processedFileSize = input.readLong();
                System.out.println("Servers's file size: " + processedFileSize);
                appendTextToTextArea("Servers's file size: " + processedFileSize);
                if (processedFileSize > 0) {
                    // Get processed File
                    FileOutputStream fos = new FileOutputStream(myOutputFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    System.out.println("Reciving file: " + myOutputFilePath);
                    appendTextToTextArea("Reciving file: " + myOutputFilePath);
                    buffer = new byte[bufferSize];
                    int bytesRead;
                    long fileSizeLeft = processedFileSize;
                    while (fileSizeLeft > 0) {
                        bytesRead = input.read(buffer);
                        bos.write(buffer, 0, bytesRead);
                        fileSizeLeft -= (long) bytesRead;
                    }
                    System.out.println("File recived");
                    appendTextToTextArea("File recived");
                    // Close buffers
                    bos.close();
                    fos.close();

                    //END OF CONNECTION
                } else {
                    System.out.println("Server reports error with your file");
                }
                appendTextToTextArea("Done!");
            } catch (UnknownHostException e) {
                System.out.println("Socket: " + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            } finally {
                //13. Close connections
                if (soc != null)
                    try {
                        soc.close();
                    } catch (IOException e) {/*close failed*/}
                processImageBtn.setVisible(true);
            }
        }
    }

    //------- Connection Class
    private class Connection extends Thread {

        public void run() {
            connectionIsRunning = true;
            // Check if fileSize is smaller than 20MB
            if(checkFileSize(myInputFilePath) < 1 || checkFileSize(myInputFilePath) > 20971520) {
                appendTextToTextArea("Select correct file!");
                appendTextToTextArea("File size: " + checkFileSize(myInputFilePath));
                appendTextToTextArea("File does not exist or file size is greater than 20MB");
                processImageBtn.setVisible(true);
            }
            else {
                appendTextToTextArea("Selected file is correct");
                // Connect to Scheduler Server
                //TODO this
                // Get Processing server ip and port
                //TODO this

                // Connect to Processing Server
                try {
                    soc = new Socket(processingServerIP, processingServerPort);

                    // Initialize buffers
                    DataInputStream input = new DataInputStream(soc.getInputStream());
                    DataOutputStream output = new DataOutputStream(soc.getOutputStream());
                    File myFile = new File(myInputFilePath);
                    byte[] buffer = new byte[(int) myFile.length()];

                    // Send integer- conversionType
                    output.writeInt(conversionType);

                    // Send file name
                    output.writeUTF(myFile.getName());
                    appendTextToTextArea("File: " + myFile.getPath());

                    // Send file size
                    System.out.println("Length of myFile: " + myFile.length());
                    appendTextToTextArea("Length of myFile: " + myFile.length());
                    output.writeLong(myFile.length());

                    // Send file
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    appendTextToTextArea("Sending file to server");
                    int bytesSent;
                    while ((bytesSent = bis.read(buffer, 0, bufferSize)) != -1) {
                        output.write(buffer, 0, bytesSent);
                    }
                    output.flush();

                    // Close buffers
                    bis.close();
                    fis.close();
                    appendTextToTextArea("File has been Sent");

                    // Wait for processing file
                    System.out.println("Our server is processing your file");
                    appendTextToTextArea("Our server is processing your file");
                    // Get file size
                    long processedFileSize = input.readLong();
                    System.out.println("Servers's file size: " + processedFileSize);
                    appendTextToTextArea("Servers's file size: " + processedFileSize);
                    if (processedFileSize > 0) {
                        // Get processed File
                        FileOutputStream fos = new FileOutputStream(myOutputFilePath);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        System.out.println("Reciving file: " + myOutputFilePath);
                        appendTextToTextArea("Reciving file: " + myOutputFilePath);
                        buffer = new byte[bufferSize];
                        int bytesRead;
                        long fileSizeLeft = processedFileSize;
                        while (fileSizeLeft > 0) {
                            bytesRead = input.read(buffer);
                            bos.write(buffer, 0, bytesRead);
                            fileSizeLeft -= (long) bytesRead;
                        }
                        System.out.println("File recived");
                        appendTextToTextArea("File recived");
                        // Close buffers
                        bos.close();
                        fos.close();

                        //END OF CONNECTION
                    } else {
                        System.out.println("Server reports error with your file");
                    }
                    appendTextToTextArea("Done!");
                } catch (UnknownHostException e) {
                    System.out.println("Socket: " + e.getMessage());
                    appendTextToTextArea("Server's socket is unavailable");
                } catch (EOFException e) {
                    System.out.println("EOF: " + e.getMessage());
                    appendTextToTextArea("There is a problem with your file");
                } catch (IOException e) {
                    System.out.println("IO: " + e.getMessage());
                    appendTextToTextArea("Server is unavailable");
                } finally {
                    //13. Close connections
                    if (soc != null)
                        try {
                            soc.close();
                        } catch (IOException e) {/*close failed*/}
                    processImageBtn.setVisible(true);
                }
            }
            System.out.println("End of thread connection");
            connectionIsRunning = false;
        }
    }




}
