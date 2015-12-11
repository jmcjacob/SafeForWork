package com.company;

public class Main {

    public static void main(String[] args) {
        if (Facade.initiate("192.168.0.5")) {
            Facade.displayMoney();
            Facade.buy(Facade.clientSocket, "AVIVA", 10);
            Facade.displayMoney();
            Facade.displayOwned();
            Facade.sell(Facade.clientSocket, "AVIVA", 4);
            Facade.displayMoney();
            Facade.displayOwned();
            Facade.exit(Facade.clientSocket);
        }
        else{
            System.out.println("FAILED");
        }
    }
}