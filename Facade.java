package com.company;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Facade class, based on design patterns for simplifying the User Experience.
 * @see #clientSocket
 * @see #id
 * @see #money
 * @see Stock
 */
public class Facade {

    /** String id, stores the returned registration id from the REGI server command.*/
    static String id = null;
    /** Socket clientSocket, stores socket information for the program to access.*/
    static Socket clientSocket;
    /** Local reference of the amount of money the User.*/
    static Double money = 0.0;
    /** Stores a list of the latest stocks, its price and difference.*/
    static List<Stock> stocks = new ArrayList<Stock>();

    /**
     * Method: initiate, takes an IP address as a string and sets up the clients connection to the server.
     * @param ip IP address of the Stocks Server to connect to.
     * @return Boolean value to determine whether initiation was successful.
    */
    public static boolean initiate(String ip) {
        try {
            clientSocket = new Socket(ip, 5000);
            if (Facade.test(clientSocket)) {
                if (register(clientSocket)) {
                    if (get(clientSocket)) {
                        if (setMoney(clientSocket)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Method: test, ensures a connection is established between the client and server by
     * sending "HELO" and reading a response.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @return Boolean value to determine whether test "HELO" command was successful.
     */
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

    /**
     * Method: exit, this method cleanly drops the connection to the server.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @return Boolean value to determine whether the connection was closed successfully.
     */
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

    /**
     * Method: setMoney, this method sets the amount of money locally to the amount the server has allocated.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @return Boolean value to determine whether the money was set correctly.
     */
    public static boolean setMoney(Socket clientSocket) {
        try {
            SendMessages send = new SendMessages(clientSocket, "CASH:"+Facade.id);
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
                String[] values = receive.reply.split(":");
                money = Double.valueOf(values[2]);
                return true;
            }
        }
        catch (Exception exception) {
            System.out.println("ERROR: " + exception);
        }
        return false;
    }

    /**
     * Method: register, this method sends the "REGI" command to the server to retrieve a
     * unique identifier for this clients connection. This identifier is used in other methods such as
     * Buy and Sell as the server requires any transaction to be authorised.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @return Boolean value to determine whether the client has registered successfully.
     * @see #buy(Socket, String, int)
     * @see #sell(Socket, String, int)
     * @see #display()
     * @see #setMoney(Socket)
     * @see #update(Socket)
     */
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

    /**
     * Method: buy, this method will ensure the local stocks list is up to date, there is sufficient funds
     * to buy the specified stocks and then complete the action and store the owned stocks locally.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @param company This represents the company name of the stocks.
     * @param Shares Number of shares involved in the transaction.
     * @return Boolean value to determine whether the purchase has completed successfully.
     * @see #stocks
     * @see #update(Socket)
     */
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
                        stocks.get(index).addOwned(Shares);
                        setMoney(clientSocket);
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

    /**
     * Method: sell, this method will check that the specified stocks and amount are actually owned by
     * the client and then update the current local values of all stocks and proceed with the transaction.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @param company This represents the company name of the stocks.
     * @param Shares Number of shares involved in the transaction.
     * @return Boolean value to determine whether the transaction has completed successfully.
     * @see #stocks
     * @see #update(Socket)
     * @see #money
     * @see #setMoney(Socket)
     */
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
                    if (stocks.get(index).takeOwned(Shares)) {
                        setMoney(clientSocket);
                        return true;
                    }
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

    /**
     * Method: get, this method will initialise the list of stocks with the first values read by the client.
     * after this method, {@link #update(Socket)}, the Update method will handle the continued updated stock
     * information.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @return Boolean value to determine whether the initial stock values have been retrieved.
     * @see #update(Socket)
     * @see #stocks
     */
    public static Boolean get(Socket clientSocket) {
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
                if (receive.replies[i].startsWith("STK")) {
                    String[] values = receive.replies[i].split(":");
                    stocks.add(new Stock(values[1], Double.valueOf(values[2]), Double.valueOf(values[3])));
                }
            }
        }
        catch (Exception exception) {
            System.out.println("ERRORget: " + exception);
            exception.printStackTrace();
        }
        return true;
    }

    /**
     * Method: update, will continually be called throughout the running of the Facade operations to ensure
     * that the client is performing calculations on the most up to date stocks from the server.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @return Boolean value to determine whether the stocks have been updated successfully.
     * @see #stocks
     * @see Stock
     */
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
                if (receive.replies[i].startsWith("STK")) {
                    String[] values = receive.replies[i].split(":");
                    for (int j = 0; j < stocks.size(); j++) {
                        if (stocks.get(j).getName().equals(values[1])) {
                            stocks.set(j, new Stock(values[1], Double.valueOf(values[2]), Double.valueOf(values[3]), stocks.get(j).getOwned()));
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            System.out.println("ERRORupdate: " + exception);
        }
        return false;
    }

    /**
     * Method: display, this method will display the stock information that is stored locally.
     * @return Boolean value to determine whether the stocks have been updated successfully.
     */
    public static boolean display() {
        System.out.println("Available Stocks: ");
        for (int i = 0; i < stocks.size(); i++) {
            System.out.println(stocks.get(i).getName()+":"+stocks.get(i).getPrice()+":"+stocks.get(i).getChange()+":"+stocks.get(i).getOwned());
        }
        return true;
    }

    /**
     * Method: displayMoney, displays the amount of money the client has.
     * @return value of the Money currently owned.
     * @see #money
     */
    public static String displayMoney() {
        return NumberFormat.getCurrencyInstance(new Locale("en", "GB")).format(money);
    }

    /**
     * Method: displayOwned, shows the user what stocks are owned by the client.
     * @return Boolean value to determine whether the owned stock has been displayed.
     */
    public static boolean displayOwned() {
        boolean owned = false;
        System.out.println("Owned Stocks: ");
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getOwned()>0) {
                System.out.println(stocks.get(i).getName() + ":" + stocks.get(i).getPrice() + ":" + stocks.get(i).getChange() + ":" + stocks.get(i).getOwned());
                owned = true;
            }
        }
        return owned;
    }

    /**
     * Method: autoCycle, automates the buy and sell functionality of the facade. This method aims to
     * be continually profitable and then displays the sum of profits to the user.
     * @param clientSocket This is the socket the client communicates with the server through.
     * @param cycles Number of iterations to automate the buy and sell functions.
     * @return Boolean value to determine whether the cycles have completed successfully.
     * @see #buy(Socket, String, int)
     * @see #sell(Socket, String, int) 
     */
    public static boolean autoCycle(Socket clientSocket, int cycles) {
        for (int j = 0; j < cycles; j++) {
            update(clientSocket);
            int index = 0;
            Double HighestChange = 10.0;
            for (int i = 0; i < stocks.size() - 1; i++) {
                if (stocks.get(i).getPrice() < HighestChange && stocks.get(i).getPrice() > 0.0 && stocks.get(i).getChange()>0.0) {
                    HighestChange = stocks.get(i).getPrice();
                    index = i;
                }
            }
            int number = (int) (long) (Facade.money / stocks.get(index).getPrice());
            Facade.buy(clientSocket, stocks.get(index).getName(), number);
            Double price = stocks.get(index).getPrice();
            String name = stocks.get(index).getName();
            boolean thing = true;
            while (thing) {
                try {
                    Thread.sleep(5000);                 //1000 milliseconds is one second.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                Facade.update(clientSocket);
                if (price < stocks.get(index).getPrice()) {
                    Facade.sell(clientSocket, name, number);
                    thing = false;
                }
            }
            System.out.println(String.valueOf("Cycle " +String.valueOf(j+1) + " out of " + String.valueOf(cycles)));
        }
        return true;
    }
}
