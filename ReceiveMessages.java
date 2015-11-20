package com.company;

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
    public String reply;

    public ReceiveMessages(Socket _Socket)
    {
        this.s = _Socket;
    }

    public void run()
    {
        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
            reply = "Reply: " + response.readLine();
        }

        catch (Exception e)
        {
            System.out.println("Send: " + e);
        }
    }
}