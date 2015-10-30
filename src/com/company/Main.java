package com.company;

import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args)
    {
        try
        {
            Socket clientSocket = new Socket("192.168.0.48", 5000);

        }
        catch (Exception exception)
        {
            System.out.println("ERROR: " + exception);
        }
    }
}
