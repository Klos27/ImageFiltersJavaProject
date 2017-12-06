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
    private String fileNameOutput;  // name of the file from client
    private String fileNameInput;   // name of the file to send to client

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
            //1. Read length of a String (file name)
                //no need

            //2. Get filename
            fileNameOutput = input.readUTF();
            fileNameInput = ImageFilter.getOutputFilePath(fileNameOutput);
            //TODO change to store images in folder ex. String fileOutput = "C:\\Users\\Klos\\Documents\\najsowo.mkv"

            //3. Initialize buffers
            int buffSize = 65536; // max size 65536 bytes [64KB]
            FileOutputStream fos = new FileOutputStream(fileNameOutput);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            //4. Get Size of a file
            long fileSize = input.readLong();
            System.out.println("Client's file size: "+ fileSize);

            //5. Get File
            System.out.println("Reciving file: " + fileNameOutput);
            int bytesRead;
            byte[] buffer = new byte[buffSize];
            while((bytesRead = input.read(buffer)) != -1){
                bos.write(buffer,0,bytesRead);
            }
            System.out.println("File recived");

            //6. Process File
            if(!ImageFilter.sepia(fileNameOutput)){
                //7. Send Size of a file
                    //TODO SEND SIZE
                //8. Send File
                    //TODO SEND FILE
                //9. Delete original file and processed File
                Files.deleteIfExists(FileSystems.getDefault().getPath(fileNameInput));
                Files.deleteIfExists(FileSystems.getDefault().getPath(fileNameOutput));

            }
            else{
                //TODO send ERROR
            }

            //10. Close buffers
            bos.close();
            fos.close();
            //END OF CONNECTION
        }
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage()); }
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());}
        finally {
            try {
                clientSocket.close();
            }
            catch (IOException e){/*close failed*/}
        }
    }
}