package com.company;

public class Main {

    public static void main(String[] args) {
        if (Facade.start("192.168.0.3")) {
            System.out.print(Facade.display(Facade.clientSocket));
            Facade.exit(Facade.clientSocket);
        }
        else{
            System.out.println("FAILED");
        }
    }
}