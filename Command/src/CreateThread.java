/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class CreateThread implements Runnable {

    private String command;
    private TestProcessBuilder test = new TestProcessBuilder();
    
    public CreateThread(String command) {
        this.command = command;
    }

    public void run() {
        List<String> input = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(command);
        while (tokenizer.hasMoreTokens()) {
            input.add(tokenizer.nextToken());
        }
        ProcessBuilder pb = new ProcessBuilder(input);
        // ProcessBuilder creates a process corresponding to the input command
        // now start the process
        BufferedReader br = null;
        try {
            Process proc = pb.start();
            // obtain the input and output streams
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            // read what the process returned
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (java.io.IOException ioe) {
            System.err.println("Error");
            System.err.println(ioe);
            test.err.add(command);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(CreateThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
