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

            SendMessages send = new SendMessages(clientSocket);
            ReceiveMessages receive = new ReceiveMessages(clientSocket);
            Register register = new Register(clientSocket);

           // PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //BufferedReader response = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            if (clientSocket.isConnected() )
            {
                System.out.println("Connected");
                //out.println("HELO");
                //System.out.println("echo: " + response.readLine());
                //out.flush();
                //response.close();
               // out.close();
                Thread t1 = new Thread(send);
                Thread t2 = new Thread(receive);
                Thread t3 = new Thread(register);

                t1.start(); t2.start(); t3.start();
                //t3.run();
                //t1.run();
                //t2.run();
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