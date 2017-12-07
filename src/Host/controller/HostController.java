package Host.controller;

import javafx.fxml.FXML;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class HostController {
    String processingServerIP = "localhost";
    int processingServerPort = 55000;
    Socket soc = null;
    final int bufferSize = 65536;   // max size 65536 bytes [64KB]
    String myInputFilePath;  // File's path to send to server
    String myOutputFilePath; // File recived form server

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
        //TODO get this from the textFiled GUI
        myInputFilePath = "input.jpg";
        myOutputFilePath = "output.jpg";
    }

    private String getMyFileName() {
        //TODO change this to extract only name + extansion from whole Path
        return "input.jpg";
    }

    @FXML
    private void processImage() {
        // Check if fileSize is smaller than 20MB!!!
        //TODO this
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
            output.writeBytes(myFile.getName()); // UTF is a string encoding

            // Send file size
            System.out.println("Length of myFile: " + myFile.length());
            output.writeLong(myFile.length());

            // Send file
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            while( bis.read(buffer, 0, bufferSize) != -1) {
                output.write(buffer, 0, bufferSize);
            }
            output.flush();

            // Wait for processing file
            System.out.println("Our server is processing your file");

            // Get file size
            long processedFileSize = input.readLong();
            System.out.println("Servers's file size: "+ processedFileSize);
            if(processedFileSize > 0) {
                // Get processed File
                FileOutputStream fos = new FileOutputStream(myOutputFilePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                System.out.println("Reciving file: " + myOutputFilePath);
                buffer = new byte[bufferSize];
                int bytesRead;
                while((bytesRead = input.read(buffer)) != -1){
                    bos.write(buffer,0,bytesRead);
                }
                System.out.println("File recived");

                // Close buffers
                bos.close();
                fos.close();

                //END OF CONNECTION
            }
            else{
                System.out.println("Server reports error with your file");
            }

            // Close buffers
            bis.close();
            fis.close();
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        finally {
            //13. Close connections
            if (soc != null)
                try {
                    soc.close();
                } catch (IOException e) {/*close failed*/}
        }
    }
}
