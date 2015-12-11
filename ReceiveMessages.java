package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

public class ReceiveMessages extends Thread
{
    private Socket s;
    public String reply = "";
    public String[] replies;
    public boolean multiLine = false;

    public ReceiveMessages(Socket _Socket)
{
    this.s = _Socket;
}

    public ReceiveMessages(Socket _Socket, Boolean _multiLine) {
        this.s = _Socket;
        this.multiLine = _multiLine;
    }

    public void run() {
        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if (!multiLine)
                reply = reply + response.readLine() + "\n";
            else if(multiLine) {
                int value = 0;
                String stock = "";
                while ((value = response.read()) != -1) {
                    if ((char)value == '\n') {
                        String[] stocks = stock.split("\n");
                        if (stocks.length > 1) {
                            if (stocks[stocks.length-1].equals("END:EOF")) {
                                replies = Arrays.copyOfRange(stocks,1,stocks.length-1);
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
