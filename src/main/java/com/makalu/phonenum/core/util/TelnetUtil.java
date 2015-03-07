package com.makalu.phonenum.core.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.commons.net.telnet.TelnetClient;

public class TelnetUtil {
	private TelnetClient telnet = new TelnetClient();
    private InputStream in;
    private PrintStream out;
    private String prompt = ">";
    
    public TelnetUtil() { }

    public TelnetUtil(String server, int port, String userName, String password) {
        try {
            // Connect to the specified server
            telnet.connect(server, port);

            // Get input and output stream references
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());

            // Log the user on
            if (userName != null && password != null) {
                readUntil("username:");
                write(userName);
                readUntil("password:");
                write(password);
            }

            // Advance to a prompt
            readUntil(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String sendCommand(String command) {
    	String runCmdOutput;
    	write(command);
    	do{
    		//System.out.println("Start reading output.");
    		runCmdOutput = readUntil(prompt);
    		//System.out.println("Command Output:" + runCmdOutput);
   			if(runCmdOutput.toLowerCase().contains("execute error:there has been a record with the same selector and number")) {
   				return "REPEATED";
   			}
   			if(runCmdOutput.toLowerCase().contains("error")) {
   				return "FAILED";
    		}
        } while(command.equalsIgnoreCase("Y") && !runCmdOutput.toLowerCase().contains(".zip"));
   		return "SUCCESS";
    }
    
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private synchronized String readUntil(String pattern) {
    	char lastChar;
        try {
        	if(pattern.length() > 1) {
        		lastChar = pattern.charAt(pattern.length() - 1);
        	} else {
        		lastChar = '>';
        	}
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) { 
                    if (sb.toString().endsWith(pattern)) {
                    	try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)))) {
                    	    out.println(sb.toString());
                    	}catch (IOException e) {
                    		e.printStackTrace();
                    	}
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
