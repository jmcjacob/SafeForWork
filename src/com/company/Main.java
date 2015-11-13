package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;

public class Main {

    public static void main(String[] args)
    {
        try
        {
            Socket clientSocket = new Socket("192.168.0.48", 5000);
            ServerSocket socket = new ServerSocket(5000);

            SendMessages sm = new SendMessages(clientSocket);
            ReceiveMessages rm = new ReceiveMessages(clientSocket);

           // PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //BufferedReader response = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            if (clientSocket.isConnected() )
            {
                System.out.println("Connected");
                System.out.println("HeRE");
                //out.println("HELO");
                //System.out.println("echo: " + response.readLine());
                //out.flush();
                //response.close();
               // out.close();
                Thread t1 = new Thread(rm);
                Thread t2 = new Thread (sm);

                t1.start();
                t2.start();
                t1.run();
                t2.run();
            }
            else
                System.out.println("ERROR");

            System.out.println("Ended");
            clientSocket.close();
        }
        catch (Exception exception)
        {
            System.out.println("ERROR: " + exception);
        }
    }
}