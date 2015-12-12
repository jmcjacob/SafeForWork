package com.company;

public class Main {

    public static void main(String[] args) {
      String ip = "192.168.0.48"; //Default for Lab B Machines

      ip = UserInterface.prompt("Specify the IP address of the server:");

      if (Facade.initiate(ip)) {
        UserInterface.menu();

        }
        else{
            System.out.println("FAILED TO CONNECT TO IP: " + ip);
        }
    }
}
