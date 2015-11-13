package com.company;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;

/**
 * Created by Computing on 13/11/2015.
 */
public class Register extends Thread
{
    private Socket s;

    public Register(Socket _Socket)
    {
        this.s = _Socket;
    }

    public void run()
    {
        System.out.println("Register Thread:");
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("REGI");

            //Need to exit the connection by sending "EXIT" to notify the server.

            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            System.out.println("Register: " + e);
        }
    }
}