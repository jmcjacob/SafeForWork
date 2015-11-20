package com.company;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Computing on 13/11/2015.
 */
public class Facade {

    static String id = null;

    public static boolean test()
    {
        //HELO
        try
        {
            Socket clientSocket = new Socket("192.168.0.48", 5000);

            SendMessages send = new SendMessages(clientSocket, "HELO");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected() )
            {
                send.run();
                receive.run();
            }
            else
                System.out.println("Not Connected");

            clientSocket.close();

            if (receive.reply.isEmpty()) return false;
            else return true;
        }
        catch (Exception exception)
        {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static String exit() {
        //EXIT
        try {
            Socket clientSocket = new Socket("192.168.0.48", 5000);
            SendMessages send = new SendMessages(clientSocket, "EXIT");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }

            clientSocket.close();

            if (receive.reply.isEmpty()) {
                return null;
            }
            else {
                return receive.reply;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return null;
    }

    public static String register()
    {
        //REGI
        try {
            Socket clientSocket = new Socket("192.168.0.48", 5000);
            SendMessages send = new SendMessages(clientSocket, "REGI");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }

            clientSocket.close();

            if (receive.reply.isEmpty()) {
                return null;
            }
            else {
                id = receive.reply.substring(receive.reply.lastIndexOf(':') + 1);
                System.out.println("Your Registration ID: " + id);
                return receive.reply;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return null;
    }

    public static String buy() {
        //BUY
        return null;
    }

    public static String sell() {
        //SELL
        return null;
    }

    public static String display() {
        //DISP
        return null;
    }
}
