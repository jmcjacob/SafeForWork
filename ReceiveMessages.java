package com.company;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class ReceiveMessages extends Thread
{
    private Socket s;
    public String reply = "";
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
                reply = "Reply: " + response.readLine() + "\n";
            else if(multiLine) {
                do {
                    reply = reply + "Reply: " + response.readLine() + "\n";
                }
                while(response.readLine().substring(0,3).equals("STK"));
            }
        }

        catch (Exception e) {
            System.out.println("Send: " + e);
        }
    }
}
