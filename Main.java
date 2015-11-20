package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.CharBuffer;

public class Main {

    public static void main(String[] args)
    {
        if (Facade.test()) System.out.println("MAIN: Yay, it does stuff.");
        System.out.println(Facade.exit());
        System.out.println(Facade.register());
    }
}