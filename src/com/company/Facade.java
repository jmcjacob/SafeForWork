package com.company;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Computing on 13/11/2015.
 */
public class Facade {

    public static boolean test()
    {
        //HELO
        try
        {
            Socket clientSocket = new Socket("192.168.0.48", 5000);

            SendMessages send = new SendMessages(clientSocket);
            ReceiveMessages receive = new ReceiveMessages(clientSocket, "HELO");

            if (clientSocket.isConnected() )
            {
                receive.run();
                send.run();
            }
            else
                System.out.println("Not Connected");

            clientSocket.close();

            if (send.reply.isEmpty()) return false;
            else return true;
        }
        catch (Exception exception)
        {
            System.out.println("ERROR: " + exception);
        }
        return true;
    }

    public static boolean exit()
    {
        //EXIT
        return true;
    }

    public static boolean register()
    {
        //REGI
        return true;
    }
}
