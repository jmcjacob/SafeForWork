package com.company;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;
/**
 * Created by Computing on 06/11/2015.
 */
public class ReceiveMessages extends Thread
{
    private Socket s;
    public String reply = "";
    public boolean end = true;

    public ReceiveMessages(Socket _Socket)
{
    this.s = _Socket;
}
    public ReceiveMessages(Socket _Socket, Boolean _end)
    {
        this.s = _Socket;
        this.end = _end;
    }

    public void run()
    {
        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if (end)
                reply = "Reply: " + response.readLine() + "\n";
            else {
                do {
                    reply = reply + "Reply: " + response.readLine() + "\n";
                }
                while(response.readLine().substring(0,3).equals("STK"));
            }
        }

        catch (Exception e)
        {
            System.out.println("Send: " + e);
        }
    }
}
