package Server.model;
import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Connection extends Thread {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;
    final int bufferSize = 65536; // max size 65536 bytes [64KB]
    private String clientsFileName;  // name of the file from client
    private String processedFileName;   // name of the file to send to client

    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream( clientSocket.getInputStream());
            output = new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        }
        catch(IOException e) {
            System.out.println("Connection problem: "+e.getMessage());
        }
    }

    public void run() {
        try {
            // Get conversionType
            int conversionType = input.readInt();
            System.out.println("Conversion type: "+ conversionType);

            // Get filename
            clientsFileName = input.readUTF();
            System.out.println("Reciving file: " + clientsFileName);
            clientsFileName = "E:\\" + clientsFileName;
            processedFileName = ImageFilter.getOutputFilePath(clientsFileName);
            //TODO change to store images in folder ex. String fileOutput = "C:\\Users\\Klos\\Documents\\najsowo.mkv"

            // Initialize buffers
            FileOutputStream fos = new FileOutputStream(clientsFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // Get Size of a file
            long fileSize = input.readLong();
            System.out.println("Client's file size: "+ fileSize);

            // Get File
            long fileSizeLeft = fileSize;
            int bytesRead;
            byte[] buffer = new byte[bufferSize];
            while(fileSizeLeft > 0){
                bytesRead = input.read(buffer);
                bos.write(buffer,0,bytesRead);
                fileSizeLeft -= (long) bytesRead;
            }
            System.out.println("File recived");

            // Process File
            if(ImageFilter.convertImage(clientsFileName, conversionType)){
                // Open buffers
                File processedFile = new File(processedFileName);
                buffer = new byte[bufferSize];
                FileInputStream fis = new FileInputStream(processedFile);
                BufferedInputStream bis = new BufferedInputStream(fis);

                // Send Size of a file
                System.out.println("Length of processedFile: " + processedFile.length());
                output.writeLong(processedFile.length());
                int bytesSent;
                // Send File
                while(( bytesSent = bis.read(buffer, 0, bufferSize)) != -1) {
                    output.write(buffer, 0, bytesSent);
                }
                output.flush();

                // Close buffers
                bis.close();
                fis.close();
            }
            else{
                //Send error
                System.out.println("Error Sent to Client");
                output.writeLong(0);
            }

            // Close buffers
            bos.close();
            fos.close();

            // Delete original file and processed File
            Files.deleteIfExists(FileSystems.getDefault().getPath(processedFileName));
            Files.deleteIfExists(FileSystems.getDefault().getPath(clientsFileName));

            // END OF CONNECTION
        }
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage()); }
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());}
        finally {
            try {
                clientSocket.close();
            }
            catch (IOException e){
                // Close failed
            }
        }
    }
}