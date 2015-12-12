package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

/**
 * A class to read messages outputted by the Stocks server.
 * @see java.lang.Runnable
 * @see java.lang.Thread
 * @see #s This is the socket the client communicates with the server through.
 * @see #reply Stores a single line reply
 * @see #replies stores multi-lined replies
 */
public class ReceiveMessages extends Thread
{
    private Socket s;
    public String reply = "";
    public String[] replies;
    public boolean multiLine = false;

    /**
     * Constructor for ReceiveMessages with socket information.
     * @param _Socket This is the socket the client communicates with the server through.
     */
    public ReceiveMessages(Socket _Socket)
    {
        this.s = _Socket;
    }

    /**
     * Constructor for ReceiveMessages with socket information and multiple line check.
     * @param _Socket This is the socket the client communicates with the server through.
     * @param _multiLine determines whether the reply was a single line or not.
     */
    public ReceiveMessages(Socket _Socket, Boolean _multiLine) {
        this.s = _Socket;
        this.multiLine = _multiLine;
    }

    /**
     * This method will attempt to read the response from the server and then save the relevant information
     * to a string array {@link #replies} or a string {@link #reply}.
     */
    public void run() {
        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if (!multiLine) {
                String thing = response.readLine();
                if (thing.isEmpty()) {
                    reply = reply + response.readLine();
                }
                else{
                    reply = reply + thing;
                }
            }
            else if(multiLine) {
                int value = 0;
                String stock = "";
                while ((value = response.read()) != -1) {
                    if ((char)value == '\n') {
                        String[] stocks = stock.split("\n");
                        if (stocks.length > 1) {
                            if (stocks[stocks.length-1].equals("END:EOF")) {
                                replies = Arrays.copyOfRange(stocks,1,stocks.length-2);
                                return;
                            }
                        }
                    }
                    if ((char)value == '\r') {}
                    else {
                        stock = stock + (char) value;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Send: " + e);
        }
    }
}
