package Server.model;
import java.net.*;
import java.io.*;

public class Connection extends Thread {
    DataInputStream input;
    DataOutputStream output;
    Socket clientSocket;
    private final static String fileOutput = "C:\\Users\\Klos\\Documents\\najsowo.mkv";
    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            input = new DataInputStream( clientSocket.getInputStream());
            output =new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        }
        catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    public void run() {
        try {
            //TODO rebuild this function
            // an echo server
            //  String data = input.readUTF();
            int buffSize = 65536; // max size 65536 bytes [64KB]
            FileWriter out = new FileWriter("test.txt");
            BufferedWriter bufWriter = new BufferedWriter(out);

            FileOutputStream fos = new FileOutputStream(fileOutput);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            //Step 1 read length
            long nb = input.readLong();
            System.out.println("Read Length "+ nb);

            //byte[] digit = new byte[buffSize];
            //Step 2 read byte
            System.out.println("Writing.......");
            //           for(int i = 0; i < nb; i++)
            //               digit[i] = input.readByte();
            int bytesRead;
            byte[] buffer = new byte[buffSize];
            while((bytesRead = input.read(buffer)) != -1){
                bos.write(buffer,0,bytesRead);
            }
            System.out.println("File recived");

            // int bytesRead = input.read(digit, 0, nb);
            //  bos.write(digit, 0, bytesRead);
            bos.close();
            fos.close();

/*
            String st = new String(digit);
            bufWriter.append(st);
            bufWriter.close();
            System.out.println ("receive from : " +
                    clientSocket.getInetAddress() + ":" +
                    clientSocket.getPort() + " message - " + st);
            st = st + "servero";
            //Step 1 send length
            output.writeInt(st.length());
            //Step 2 send length
            output.writeBytes(st); // UTF is a string encoding
            //  output.writeUTF(data);
 */       }
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