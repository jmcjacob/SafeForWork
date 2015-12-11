package com.company;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Facade {

    static String id = null;
    static Socket clientSocket;
    static Double money = 0.0;
    static List<Stock> stocks = new ArrayList<Stock>();

    public static boolean initiate(String ip) {
        try {
            clientSocket = new Socket(ip, 5000);
            Facade.addMoney((double)clientSocket.getLocalPort());
            if (Facade.test(clientSocket)) {
                if (register(clientSocket)) {
                    if (get(clientSocket)) {
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean addMoney(Double cash) {
        money = money + cash;
        return true;
    }

    public static boolean takeMoney(Double cash) {
        if (money - cash >= 0.0) {
            money = money - cash;
            return true;
        }
        return false;
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
                System.out.println(receive.reply);
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
        Facade.update(clientSocket);
        boolean comp = false;
        int index = 0;
        for (int i = 0; i < stocks.size(); i++){
            if (stocks.get(i).getName().equals(company) && stocks.get(i).getPrice()*Shares<=money) {
                comp = true;
                index = i;
                break;
            }
        }
        if (comp) {
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
                    String[] things = receive.reply.split(":");
                    Double value = Double.valueOf(things[things.length-1]);
                    if (money - value >= 0.0) {
                        takeMoney(value);
                        stocks.get(index).addOwned(Shares);
                        return true;
                    }
                } else {
                    return false;
                }
            } catch (Exception exception) {
                System.out.println("ERROR: " + exception);
            }
        }
        return false;
    }

    public static boolean sell(Socket clientSocket, String company, int Shares) {
        Facade.update(clientSocket);
        boolean comp = false;
        int index = 0;
        for (int i = 0; i < stocks.size(); i++){
            if (stocks.get(i).getName().equals(company) && stocks.get(i).getOwned()>=Shares) {
                comp = true;
                index = i;
                break;
            }
        }
        if (comp) {
            try {
                SendMessages send = new SendMessages(clientSocket, "SELL:" + company + ":" + String.valueOf(Shares) + ":" + Facade.id);
                ReceiveMessages receive = new ReceiveMessages(clientSocket);

                if (clientSocket.isConnected()) {
                    send.run();
                    receive.run();
                } else {
                    System.out.println("Not Connected");
                }

                if (!receive.reply.isEmpty()) {
                    String[] things = receive.reply.split(":");
                    Double value = Double.valueOf(things[things.length-1]);
                    addMoney(value);
                    if (stocks.get(index).takeOwned(Shares))
                        return true;
                    return false;
                } else {
                    return false;
                }
            } catch (Exception exception) {
                System.out.println("ERROR: " + exception);
            }
        }
        return false;
    }

    public static Boolean get(Socket clientSocket) {
        try {
            System.out.println("ID: " + Facade.id);
            SendMessages send = new SendMessages(clientSocket, "DISP:" + Facade.id);
            ReceiveMessages receive = new ReceiveMessages(clientSocket, true);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }
            for (int i = 0; i < receive.replies.length; i++) {
                String[] values = receive.replies[i].split(":");
                stocks.add(new Stock(values[1], Double.valueOf(values[2]), Double.valueOf(values[3])));
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return true;
    }

    public static Boolean update(Socket clientSocket) {
        try {
            SendMessages send = new SendMessages(clientSocket, "DISP:" + Facade.id);
            ReceiveMessages receive = new ReceiveMessages(clientSocket, true);

            if (clientSocket.isConnected()) {
                send.run();
                receive.run();
            } else {
                System.out.println("Not Connected");
            }
            for (int i = 0; i < receive.replies.length; i++) {
                String[] values = receive.replies[i].split(":");
                for (int j = 0; j > stocks.size(); j++) {
                    if (stocks.get(j).getName().equals(values[0])) {
                        stocks.set(j, new Stock(values[0], Double.valueOf(values[1]), Double.valueOf(values[2]), stocks.get(j).getOwned()));
                    }
                }
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    public static boolean display() {
        for (int i = 0; i < stocks.size(); i++) {
            System.out.println(stocks.get(i).getName()+":"+stocks.get(i).getPrice()+":"+stocks.get(i).getChange()+":"+stocks.get(i).getOwned());
        }
        return true;
    }

    public static boolean displayMoney() {
        System.out.println("Current Balance: " + NumberFormat.getCurrencyInstance(new Locale("en", "GB")).format(money));
        return true;
    }

    public static boolean displayOwned() {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getOwned()>0) {
                System.out.println(stocks.get(i).getName() + ":" + stocks.get(i).getPrice() + ":" + stocks.get(i).getChange() + ":" + stocks.get(i).getOwned());
            }
        }
        return true;
    }
}
