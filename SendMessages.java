package com.company;
import java.io.PrintWriter;
import java.net.*;

public class SendMessages extends Thread
{
    private Socket s;
    private String m;

    public SendMessages(Socket _Socket, String _m) {
        this.s = _Socket;
        this.m = _m;
    }

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
