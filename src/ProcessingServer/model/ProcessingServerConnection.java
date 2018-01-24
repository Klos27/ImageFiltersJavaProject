package ProcessingServer.model;
import java.net.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class ProcessingServerConnection implements Runnable {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;
    final int bufferSize = 65536; // max size 65536 bytes [64KB]
    private String clientsFileName;  // name of the file from client
    private String processedFileName;   // name of the file to send to client
    static long fileNo = 0;
    private boolean loadAdded = false;
    private long fileSize = 0;

    public ProcessingServerConnection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream( clientSocket.getInputStream());
            output = new DataOutputStream( clientSocket.getOutputStream());
//            this.start();
        }
        catch(IOException e) {
            System.out.println("ProcessingServerConnection problem: "+e.getMessage());
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
            //TODO change to store images in folder "Processing", not in main folder

            // Change file name to clientNo
            String ext = ImageFilter.getFileExtension(clientsFileName);
            if(ext.isEmpty())
                clientsFileName = String.valueOf(ProcessingServer.getClientNo());
            else
                clientsFileName = String.valueOf(ProcessingServer.getClientNo()) + "." + ext;

            clientsFileName = ".\\" + clientsFileName;
            processedFileName = ImageFilter.getOutputFilePath(clientsFileName);

            // Initialize buffers
            FileOutputStream fos = new FileOutputStream(clientsFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            // Get Size of a file
            fileSize = input.readLong();
            System.out.println("Client's file size: "+ fileSize);
            ProcessingServer.addLoad(fileSize);
            loadAdded = true;

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
//            Files.deleteIfExists(FileSystems.getDefault().getPath(processedFileName));
//            Files.deleteIfExists(FileSystems.getDefault().getPath(clientsFileName));

            // END OF CONNECTION
        }
        catch(EOFException e) {
            System.out.println("EOF: "+e.getMessage()); }
        catch(IOException e) {
            System.out.println("IO: "+e.getMessage());}
        catch(java.lang.OutOfMemoryError e){
            System.out.println("Out of memory: "+e.getMessage());
        }
        finally {
            if(loadAdded) {
                ProcessingServer.subLoad(fileSize);
                loadAdded = false;
            }
            // Delete original file and processed File
            try {
                Files.deleteIfExists(FileSystems.getDefault().getPath(processedFileName));
            } catch (IOException e) {
//                e.printStackTrace();
            }
            try {
                Files.deleteIfExists(FileSystems.getDefault().getPath(clientsFileName));
            } catch (IOException e) {
//                e.printStackTrace();
            }
            try {
                clientSocket.close();
            }
            catch (IOException e){
                // Close failed
            }
        }
    }
}