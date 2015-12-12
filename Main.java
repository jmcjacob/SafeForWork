package com.company;

public class Main {

    public static void main(String[] args) {

        String ip = "192.168.0.48"; //Default for Lab B Machines

        ip = UserInterface.prompt("Specify the IP address of the server:");

        if (Facade.initiate(ip)) {

           UserInterface.menu();
            //Facade.displayMoney();
            //Facade.buy(Facade.clientSocket, "AVIVA", 10);
            //Facade.displayMoney();
            //Facade.displayOwned();
            //Facade.sell(Facade.clientSocket, "AVIVA", 4);
            //Facade.displayMoney();
            //Facade.displayOwned();
            //Facade.exit(Facade.clientSocket);
        }
        else {
            System.out.println("FAILED TO CONNECT TO IP: " + ip);
        }
    }
}