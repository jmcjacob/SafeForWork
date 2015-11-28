package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;

public class Main {

    public static void main(String[] args) {
        Facade.ip = "192.168.0.3";
        if (Facade.test()) {
            Facade.register();
            System.out.println(Facade.display());
        }
        else{
            System.out.println("FAILED");
        }
    }
}