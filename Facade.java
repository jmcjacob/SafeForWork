package com.company;
import java.net.Socket;

public class Facade {

    public static String ip = null;
    static String id = null;

    public static boolean test() {
        try
        {
            Socket clientSocket = new Socket(Facade.ip, 5000);
            SendMessages send = new SendMessages(clientSocket, "HELO");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected() ) {
                send.run();
                receive.run();
            }
            else
                System.out.println("Not Connected");

            clientSocket.close();

            if (receive.reply.isEmpty()) return false;
            else return true;
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static String exit() {
        try {
            Socket clientSocket = new Socket(Facade.ip, 5000);
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

    public static void register() {
        //REGI
        try {
            Socket clientSocket = new Socket(Facade.ip, 5000);
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
                return;
            }
            else {
                Facade.id = receive.reply.substring(receive.reply.lastIndexOf(':') + 1);
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return;
    }

    public static String buy() {
        try {
            Socket clientSocket = new Socket("192.168.0.48", 5000);
            BuyStock buy = new BuyStock(clientSocket);

            if (clientSocket.isConnected()) {
                buy.run();
            } else {
                System.out.println("Not Connected");
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return null;
    }

    public static String sell() {
        //SELL
        return null;
    }

    public static String display() {
        try {
            Socket clientSocket = new Socket(Facade.ip, 5000);
            SendMessages send = new SendMessages(clientSocket, "DISP:" + Facade.id);
            ReceiveMessages receive = new ReceiveMessages(clientSocket, true);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }

            clientSocket.close();
            return receive.reply;
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return null;
    }
}
