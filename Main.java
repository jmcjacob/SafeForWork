package com.company;

import java.text.NumberFormat;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        if (Facade.initiate("192.168.0.5")) {
            Double money = Facade.money;
            Facade.autoCycle(Facade.clientSocket, 10);
            System.out.println("Money made: " + NumberFormat.getCurrencyInstance(new Locale("en", "GB")).format(Facade.money-money));
        }
        else{
            System.out.println("FAILED");
        }
        Facade.exit(Facade.clientSocket);
    }
}