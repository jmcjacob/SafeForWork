package com.company;
import java.io.PrintWriter;
import java.net.*;

/**
 * A class to send messages to the Stocks server.
 * @see java.lang.Runnable
 * @see java.lang.Thread
 * @see #s This is the socket the client communicates with the server through.
 * @see #m Message to send to the server.
 */
public class SendMessages extends Thread
{
    private Socket s;
    private String m;

    /**
     * Constructor for SendMessages including the socket information and message to send.
     * @param _Socket This is the socket the client communicates with the server through.
     * @param _m Message to send to the server.
     */
    public SendMessages(Socket _Socket, String _m) {
        this.s = _Socket;
        this.m = _m;
    }

    /**
     * Sends and outputs the message.
     */
    public void run() {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println(m);
            out.flush();
        }
        catch (Exception e) {
            System.out.println("Receive: " + e);
        }
    }
}
