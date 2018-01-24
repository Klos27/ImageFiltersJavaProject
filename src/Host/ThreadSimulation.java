package Host;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ThreadSimulation extends Thread{
    boolean connectionIsRunning = false;
    String myInputFilePath = "4.png";  // File's path to send to server
    String myOutputFilePath; // File recived form server
    int conversionType;
    int i = 0;
    private String schedulerServerIP;
    private int schedulerServerPort;
    private Socket soc = null;
    private String processingServerIP;
    private int processingServerPort;
    final int bufferSize = 65536;

    public ThreadSimulation(int i) {
        this.i = i;
    }

    private void getConversionType() {
        Random random = new Random();
        conversionType = random.nextInt(8)+1;
        conversionType = 2;

    }

    private void setOutputFilePath() {

        myOutputFilePath = String.format("TestImageOutput%04d.png",i);
        System.out.println(myOutputFilePath);
    }

    private void getSchedulerServerIP() {
        schedulerServerIP = "0.0.0.0";
        schedulerServerPort = 55001;
    }

    private void getProcessingServerIP() throws IOException {
        try {
            processingServerIP = "1.1.1.1";
            processingServerPort = 1;
            Socket schedulerSoc = new Socket(schedulerServerIP, schedulerServerPort);

            DataInputStream input = new DataInputStream(schedulerSoc.getInputStream());

            processingServerIP = input.readUTF();
            processingServerPort = input.readInt();
            System.out.println("processingServerIP: " + processingServerIP);
            System.out.println("processingServerPort: " +processingServerPort);

        } catch(IOException e){
            throw new IOException("Scheduler server unavailible");
        }
        finally{
            if (soc != null)
                try {
                    soc.close();
                } catch (IOException e) {/*close failed*/}
            if((processingServerPort == 0) || processingServerIP.equals("0.0.0.0")){
                throw new IOException("Processing server unavailible");
            }
        }
    }

        @Override
        public void run() {
            connectionIsRunning = true;
            setOutputFilePath();
            getSchedulerServerIP();
            try{
           //     File tmp = new File(myOutputFilePath);
            }
            catch(java.lang.NullPointerException e){
//                System.out.print("Select input file!");
            }


            try {
                System.out.print("Selected file is correct");
                // Connect to Scheduler Server
//                    getSchedulerServerIP();   // done before entering into this function (check processImage())
                // Get Processing server ip and port
                getProcessingServerIP();
                // Load Conversion Type
                getConversionType();

                // Connect to Processing Server
                soc = new Socket(processingServerIP, processingServerPort);

                // Initialize buffers
                DataInputStream input = new DataInputStream(soc.getInputStream());
                DataOutputStream output = new DataOutputStream(soc.getOutputStream());
                File myFile = new File(myInputFilePath);
//                myFile.getParentFile().mkdirs(); // Will create parent directories if not exists
                myFile.createNewFile();

                byte[] buffer = new byte[bufferSize];

                // Send integer- conversionType
                output.writeInt(conversionType);

                // Send file name
                output.writeUTF(myFile.getName());
                System.out.println("File: " + myFile.getPath());

                // Send file size
                System.out.println("Length of myFile: " + myFile.length());
                System.out.println("Length of myFile: " + myFile.length());
                output.writeLong(myFile.length());

                // Send file
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                System.out.println("Sending file to server");
                int bytesSent;
                while ((bytesSent = bis.read(buffer, 0, bufferSize)) != -1) {
                    output.write(buffer, 0, bytesSent);
                }
                output.flush();

                // Close buffers
                bis.close();
                fis.close();
                System.out.println("File has been Sent");

                // Wait for processing file
                System.out.println("Our server is processing your file");
                // Get file size
                long processedFileSize = input.readLong();
                System.out.println("Servers's file size: " + processedFileSize);
                if (processedFileSize > 0) {
                    // Get processed File
                    FileOutputStream fos = new FileOutputStream(myOutputFilePath, false);

                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    System.out.println("Reciving file: " + myOutputFilePath);
                    buffer = new byte[bufferSize];
                    int bytesRead;
                    long fileSizeLeft = processedFileSize;
                    while (fileSizeLeft > 0) {
                        bytesRead = input.read(buffer);
                        bos.write(buffer, 0, bytesRead);
                        fileSizeLeft -= (long) bytesRead;
                    }
                    System.out.println("File recived");
                    // Close buffers
                    bos.close();
                    fos.close();

                    //END OF CONNECTION
                    System.out.println("Done!");
                } else {
                    System.out.println("Server reports error with your file");

                }
            }
            catch(java.lang.NullPointerException e){
                System.out.println("You didn't chose output file");
            } catch (UnknownHostException e) {
                System.out.println("Socket: " + e.getMessage());
                System.out.println("Server's socket is unavailable");
            } catch (EOFException e) {
                System.out.println("EOF: " + e.getMessage());
                System.out.println("There is a problem with your file");
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
                if(e.getMessage().equals("Connection refused: connect"))
                    System.out.println("Server is unavailable");
                else
                    System.out.println(e.getMessage());
            } finally {
                //13. Close connections
                if (soc != null)
                    try {
                        soc.close();
                    } catch (IOException e) {/*close failed*/}
            }

            System.out.println("End of thread connection");
            connectionIsRunning = false;
        }

}
