package Host;

import java.net.*;
import java.io.*;

public class MainHost {
    //TODO ADD GUI instead of main
    public static void main (String args[]) {// arguments supply message and hostname of destination
        Socket s = null;
        //final String fileInput = "C:\\Users\\Klos\\Documents\\najs.mkv";
        final String fileInput = "D:\\najs.mkv";
        try {
            // TODO change to another port
            int serverPort = 55000;
            int buffSize = 65536;   // max size 65536 bytes [64KB]
            String ip = "localhost";
//            String data = "Hello, How are you? ";

            s = new Socket(ip, serverPort);
//            DataInputStream input = new DataInputStream(s.getInputStream());
            DataOutputStream output = new DataOutputStream(s.getOutputStream());

            File myFile = new File(fileInput);
            byte[] mybytearray = new byte[(int) myFile.length()];

            //Step 1 send length
            System.out.println("Length" + myFile.length());
            output.writeLong(myFile.length());
            //Step 2 send length
            System.out.println("Writing.......");
            //  output.writeBytes(data); // UTF is a string encoding

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
            long percent = 0;
            long pack = 0;
            long fileSize = myFile.length();
            long numOfPacks = fileSize / (100 * buffSize);


            while( bis.read(mybytearray, 0, buffSize) != -1) {
                output.write(mybytearray, 0, buffSize);
                pack++;
                if(pack % numOfPacks == 0){
                    System.out.println("Postęp : " + percent + "%");
                    percent++;
                }
            }
            System.out.println("Postęp : " + percent + "%");
            output.flush();
/*
            //Step 1 send length
            System.out.println("Length" + data.length());
            output.writeInt(data.length());
            //Step 2 send length
            System.out.println("Writing.......");
            output.writeBytes(data); // UTF is a string encoding
*/


      /*      //Step 1 read length
            int nb = input.readInt();
            byte[] digit = new byte[nb];
            //Step 2 read byte
            for (int i = 0; i < nb; i++)
                digit[i] = input.readByte();

            String st = new String(digit);
            System.out.println("Received: " + st);*/
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {/*close failed*/}
        }

    }
}
