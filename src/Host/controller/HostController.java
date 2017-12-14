package Host.controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HostController {
    @FXML
    private TextArea consoleOutputArea;
    @FXML
    private Button processImageBtn;
    @FXML
    private Button inputFileChoiceButton;
    @FXML
    private Button outputFileChoiceButton;
    @FXML
    private TextField fileInputTextField;
    @FXML
    private TextField fileOutputTextField;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

//    public Stage stage;
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }

    String processingServerIP;
    int processingServerPort ;
    String schedulerServerIP;
    int schedulerServerPort;
    Socket soc = null;
    final int bufferSize = 65536;   // max size 65536 bytes [64KB]
    String myInputFilePath;  // File's path to send to server
    String myOutputFilePath; // File recived form server
    static boolean connectionIsRunning = false;
    int conversionType;

    /*
    1 == Sepia (default)
    2 == Negative
    3 == MirrorImage
    4 == GrayScale
    5 == BlackAndWhite
    6 == RedImage
    7 == GreenImage
    8 == BlueImage
     */
    public void initialize() {
        filterChoiceBox.getItems().removeAll(filterChoiceBox.getItems());
        filterChoiceBox.getItems().addAll("Sepia", "Negative", "GrayScale", "Black&White", "Mirror", "RedImage", "GreenImage", "BlueImage" );

        filterChoiceBox.getSelectionModel().select("Sepia");
    }
    @FXML
    void setInputFilePath(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open input file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"));


        Stage stage = (Stage) inputFileChoiceButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
         if (file != null) {
             myInputFilePath = file.toString();
         }
        fileInputTextField.setText(myInputFilePath);

    }

    @FXML
    void setOutputFilePath(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select output file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP",  "*.bmp"),
                new FileChooser.ExtensionFilter("GIF",  "*.gif"));

        Stage stage = (Stage) outputFileChoiceButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            myOutputFilePath = file.toString();
        }
        fileOutputTextField.setText(myOutputFilePath);

    }

    private void getSchedulerServerIP() {
        //TODO [Marcin] get this from file
        schedulerServerIP = "localhost";
        schedulerServerPort = 55001;
    }

    private void getProcessingServerIP() {
        //TODO [Marcin] get this from Scheduler server
        processingServerIP = "localhost";
        processingServerPort = 55000;
    }

    private void getConversionType() {
        switch(filterChoiceBox.getValue()) {
            case "Sepia":  conversionType = 1;
                break;
            case "Negative":  conversionType = 2;
                break;
            case "Mirror":  conversionType = 3;
                break;
            case "GrayScale":  conversionType = 4;
                break;
            case "Black&White":  conversionType = 5;
                break;
            case "RedImage":  conversionType = 6;
                break;
            case "GreenImage":  conversionType = 7;
                break;
            case "BlueImage":  conversionType = 8;
                break;
            default: conversionType = 1;
                break;
        }
        System.out.println(conversionType);
    }
    @FXML
    private void appendTextToTextArea(String text){
        consoleOutputArea.appendText(text + "\n");
    }
    @FXML
    private void clearTextArea(){
        consoleOutputArea.setText("");
    }
    private long checkFileSize(String pathToFile) throws java.lang.NullPointerException{
        File myFile = new File(pathToFile);
        if(myFile.exists())
            return myFile.length();
        else
            return -1;
    }

    @FXML
    private void processImage() {
        if(connectionIsRunning == false) {
            try {
                //getFilePath();
                clearTextArea();
                appendTextToTextArea("Start");
                processImageBtn.setDisable(true);

                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        Connection connection = new Connection();
                        connection.start();
                    }
                });

            } catch (Exception e) {
                appendTextToTextArea("Starting connection failed");
            }
        }
    }

    //------- ProcessingServerConnection Class
    private class Connection extends Thread {

        public void run() {
            connectionIsRunning = true;
            // Check if fileSize is smaller than 20MB
            long checkFileS = 0;
            boolean outFileCorrect = false;
            try{
                checkFileS = checkFileSize(myInputFilePath);
                File tmp = new File(myOutputFilePath);
                outFileCorrect = true;
            }
            catch(java.lang.NullPointerException e){
//                appendTextToTextArea("Select input file!");
            }
            if(checkFileS < 1 || checkFileS > 20971520 || !outFileCorrect) {
                appendTextToTextArea("Select correct files!");
                if(checkFileS < 1)
                    appendTextToTextArea("Input File does not exist");
                else if(checkFileS > 20971520){
                    appendTextToTextArea("File size: " + checkFileS / 1024 + " KB");
                    appendTextToTextArea("File size is greater than 20MB");
                    appendTextToTextArea("Please select file smaller than 20MB");
                }
                else{
                    appendTextToTextArea("Output file path is not correct!");
                }
            }
            else {
                appendTextToTextArea("Selected file is correct");
                // Connect to Scheduler Server
                //TODO [Marcin] CONNECT TO SCHEDULER SERVER
                getSchedulerServerIP();

                // Get Processing server ip and port
                //TODO [Marcin] Get Processing server ip and port
                getProcessingServerIP();
                getConversionType();
                // Connect to Processing Server
                try {
                    soc = new Socket(processingServerIP, processingServerPort);

                    // Initialize buffers
                    DataInputStream input = new DataInputStream(soc.getInputStream());
                    DataOutputStream output = new DataOutputStream(soc.getOutputStream());
                    File myFile = new File(myInputFilePath);
                    byte[] buffer = new byte[bufferSize];

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
                        appendTextToTextArea("Done!");
                    } else {
                        System.out.println("Server reports error with your file");
                        appendTextToTextArea("Server reports error with your file");
                        appendTextToTextArea("Please try again");
                    }
                }
                catch(java.lang.NullPointerException e){
                    appendTextToTextArea("You didn't chose output file");
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
                }
            }
            System.out.println("End of thread connection");
            connectionIsRunning = false;
            processImageBtn.setDisable(false);
        }
    }




}
