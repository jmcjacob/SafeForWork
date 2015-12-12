package com.company;

import java.io.DataInputStream;
import java.io.IOException;

public class UserInterface {

    public static String menu(){
        String item1 = "Registered ID", item2 = "Display stock values",
                item3 = "Display balance", item4 = "Display owned stocks",
                item5 = "Buy stocks", item6 = "Sell stocks", item7 = "Exit";

        String company;
        Integer shares;
        String option;

        do { option =  prompt("What would you like to do?" + "\n1. " + item1 + "\t2. " + item2 +
                    "\t3. " + item3 + "\n4. " + item4 + "\t5. " + item5 + "\t6. " + item6 +
                    "\n7. " + item7);

            switch (option) {
                case "1": // Registered ID
                    System.out.println(Facade.id);
                    break;
                case "2": // Display stock values
                    Facade.display();
                    break;
                case "3": // Display balance
                    Facade.displayMoney();
                    break;
                case "4": // Display owned stocks
                    Facade.displayOwned();
                    break;
                case "5": // Buy stocks
                    company = prompt("What company stocks would you like to buy?");
                    shares = 0;

                    try {
                         shares = Integer.valueOf(prompt("How many?"));
                    } catch (Exception e) {e.printStackTrace();}

                    Facade.buy(Facade.clientSocket, company, shares);
                    break;
                case "6": // Sell stocks

                    company = prompt("What company stocks would you like to sell?");
                    shares = 0;

                    Facade.displayOwned();

                    try {
                        shares = Integer.valueOf(prompt("How many?"));
                    } catch (Exception e) {e.printStackTrace();}

                    Facade.sell(Facade.clientSocket, company, shares);
                    break;
                case "7": // Exit
                    Facade.exit(Facade.clientSocket);
                    break;
            }
        } while (option != "7");
        return "";
    }

    public static String prompt(String prompt){
        DataInputStream in = new DataInputStream(System.in);
        String response = "";

        System.out.println(prompt);

        try {
            response = in.readLine();
        }
        catch (IOException e) {e.printStackTrace();}

        return response;
    }
}
