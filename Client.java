/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Martin
 */
public class Client {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        Socket clientConnection = new Socket("localhost", 6789);

        DataOutputStream out = new DataOutputStream(clientConnection.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
        InputStream is = clientConnection.getInputStream();
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        for (;;) {
            String command = userInput.readLine();
            String[] commandList = command.split(" ");

            if ("EXIT".equals(command)) {
                out.writeBytes(command + "\r\n\r\n");
                break;
            }
            if ("PWD".equals(command)) {
                out.writeBytes(command + "\r\n\r\n");

                String header = in.readLine();
                System.out.println(header);

                int counter = 0;
                String misc = null;
                while (counter < 1) {
                    misc = in.readLine();
                    System.out.println(misc);
                    if (misc.isEmpty()) {
                        counter++;
                    }
                }

                System.out.println(in.readLine());
            }
            if ("LIST DIR".equals(command)) {
                out.writeBytes(command + "\r\n\r\n");
                int length = in.read();

                String header = in.readLine();
                System.out.println(header);

                int counter = 0;
                String misc = null;
                while (counter < 1) {
                    misc = in.readLine();
                    System.out.println(misc);
                    if (misc.isEmpty()) {
                        counter++;
                    }
                }

                for (int i = 1; i <= length; i++) {
                    System.out.println(in.readLine());
                }
            }
            if ("GET".equals(commandList[0])) {
                out.writeBytes(command + "\r\n\r\n");

                String header = in.readLine();
                String[] headerList = header.split(" ");

                if ("404".equals(headerList[1])) {
                    System.out.println(header);
                    System.out.println(in.readLine());
                } else {
                    System.out.println(header);
                    int counter = 0;
                    String misc = null;
                    long length = 0;
                    while (counter < 1) {
                        misc = in.readLine();
                        if (misc.contains("Length")) {
                            String[] list = misc.split(" ");
                            length = Long.parseLong(list[1]);
                        }
                        System.out.println(misc);
                        if (misc.isEmpty()) {
                            counter++;
                        }
                    }
                    File file = new File(".");
                    String fileName = commandList[1];

                    String fileToDownload = file.getCanonicalPath() + "/" + fileName;
                    file = new File(fileToDownload);

                    int bufferSize = clientConnection.getReceiveBufferSize();

                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);

                    byte[] bytes = new byte[bufferSize];

                    int count = 0;
                    int readBytes = 0;
                    while (readBytes < length) {
                        count = is.read(bytes);
                        System.out.println("Packet size: [ " + count + " bytes ]");
                        bos.write(bytes, 0, count);
                        readBytes += count;
                    }

                    System.out.println("Download complete");
                    bos.flush();

                    fos.close();
                    bos.close();
                }
            }
        }
        userInput.close();
        out.close();
        in.close();
        is.close();
        clientConnection.close();
    }
}
