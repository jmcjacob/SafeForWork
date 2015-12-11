package com.company;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

public class ReceiveMessages extends Thread
{
    private Socket s;
    public String reply = "Reply: ";
    public boolean multiLine = false;

    public ReceiveMessages(Socket _Socket)
{
    this.s = _Socket;
}

    public ReceiveMessages(Socket _Socket, Boolean _multiLine) {
        this.s = _Socket;
        this.multiLine = _multiLine;
    }

    public void run() {
        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if (!multiLine)
                reply = reply + response.readLine() + "\n";
            else if(multiLine) {
                int value = 0;
                String thing = "";
                while ((value = response.read()) != -1) {
                    if ((char)value == '\n') {
                        String[] things = thing.split("\n");
                        if (things.length > 1) {
                            if (things[things.length-1].equals("END:EOF")) {
                                reply = Arrays.copyOfRange(things,1,things.length-1).toString();
                                return;
                            }
                        }
                    }
                    if ((char)value == '\r') {}
                    else {
                        thing = thing + (char) value;
                    }
                }
            }
        }

        catch (Exception e) {
            System.out.println("Send: " + e);
        }
    }
}
