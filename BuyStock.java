package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;

/**
 * Created by Computing on 27/11/2015.
 */
public class BuyStock extends Thread{
    private Socket s;
    public static String company;
    public static int amount;

    public BuyStock(Socket _Socket)
    {
        this.s = _Socket;
    }

    public void run()
    {
        SendMessages send = new SendMessages(s, ("BUY:" + company + ":" + amount + ":" + Facade.id));
        ReceiveMessages receive = new ReceiveMessages(s);
    }
}
