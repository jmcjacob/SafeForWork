package com.company;

public class Main {

    public static void main(String[] args) {
        if (Facade.initiate("192.168.0.5")) {
            Facade.autoCycle(Facade.clientSocket);
        }
        else{
            System.out.println("FAILED");
        }
    }
}