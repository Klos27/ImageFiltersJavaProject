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
    final String myInputFilePath = "D:\\input.jpg";  // File's path to send to server
    final String myOutputFilePath = "D:\\output.jpg"; // File recived form server

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
            byte[] buffer = new byte[(int)myFile.length()];

            // Send integer- conversionType
            output.writeInt(conversionType);

            // Send file name
            //output.writeBytes(); // UTF is a string encoding
            output.writeUTF(myFile.getName());
            // Send file size
            System.out.println("Length of myFile: " + myFile.length());
            output.writeLong(myFile.length());

            // Send file
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);


//            System.out.println("fis: " +  fis.available());
//            System.out.println("bis: " + bis.available());
            output.flush();
            int bytesSent;
            while((bytesSent = bis.read(buffer, 0, bufferSize)) != -1) {
                output.write(buffer, 0, bytesSent);
            }
            output.flush();
            // Close buffers
            bis.close();
            fis.close();
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
                    //TODO change to above version, packet size is always 64KB
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
