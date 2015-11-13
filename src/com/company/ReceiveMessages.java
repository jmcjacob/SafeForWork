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
    private String m;

    public ReceiveMessages(Socket _Socket, String _m)
    {
        this.s = _Socket;
        this.m = _m;
    }

    public void run()
    {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println(m);

            out.flush();
            //out.close();
        }
        catch (Exception e)
        {
            System.out.println("Receive: " + e);
        }
   }
}
