package com.company;
import java.net.Socket;

public class Facade {

    static String id = null;
    static Socket clientSocket;

    public static boolean start(String ip) {
        try {
            clientSocket = new Socket(ip, 5000);
            if (Facade.test(clientSocket)) {
                if (register(clientSocket)) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean test(Socket clientSocket) {
        try {
            SendMessages send = new SendMessages(clientSocket, "HELO");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected() ) {
                send.run();
                receive.run();
            }
            else
                System.out.println("Not Connected");

            if (receive.reply.isEmpty()) return false;
            else return true;
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static boolean exit(Socket clientSocket) {
        try {
            SendMessages send = new SendMessages(clientSocket, "EXIT");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }

            if (receive.reply.isEmpty()) {
                return false;
            }
            else {
                clientSocket.close();
                return true;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static boolean register(Socket clientSocket) {
        //REGI
        try {
            SendMessages send = new SendMessages(clientSocket, "REGI");
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected()) {
                send.start();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }

            if (receive.reply.isEmpty()) {
                return false;
            }
            else {
                Facade.id = receive.reply.substring(receive.reply.lastIndexOf(':') + 1);
                Facade.id = Facade.id.replace("\n", "");
                return true;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static boolean buy(Socket clientSocket, String company, int Shares) {
        try {
            SendMessages send = new SendMessages(clientSocket, "BUY:" + company + ":" + String.valueOf(Shares) + ":" + Facade.id);
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Conneted");
            }

            if (!receive.reply.isEmpty()) {
                System.out.println(receive.reply);
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static Boolean sell(Socket clientSocket, String company, int Shares) {
        try {
            SendMessages send = new SendMessages(clientSocket, "SELL:" + company + ":" + String.valueOf(Shares) + ":" + Facade.id);
            ReceiveMessages receive = new ReceiveMessages(clientSocket);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }

            if (receive.reply.startsWith("ACK:SELL")) {
                System.out.println(receive.reply);
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return null;
    }

    public static String display(Socket clientSocket) {
        try {
            SendMessages send = new SendMessages(clientSocket, "DISP:" + Facade.id);
            ReceiveMessages receive = new ReceiveMessages(clientSocket, true);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }
            return receive.reply;
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return null;
    }
}
