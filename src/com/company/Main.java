package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Main {

    public static void main(String[] args)
    {
        try
        {
            Socket clientSocket = new Socket("192.168.0.48", 5000);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

            if (clientSocket.isConnected())
            {
                System.out.println("Connected");
                out.println("Test");
                out.flush();
                out.close();
            }
            clientSocket.close();
        }
        catch (Exception exception)
        {
            System.out.println("ERROR: " + exception);
        }
    }
}
