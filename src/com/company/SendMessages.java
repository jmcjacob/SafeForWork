package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;
/**
 * Created by Computing on 06/11/2015.
 */
public class SendMessages extends Thread
{
    private Socket s;

    public SendMessages(Socket _Socket)
    {
        this.s = _Socket;
    }

    public void run()
    {
        System.out.println("\nSend Messages Thread:");

        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("echo: " + response.readLine());
            //response.close();
        }

        catch (Exception e)
        {
            System.out.println("Send: " + e);
        }
    }
}
