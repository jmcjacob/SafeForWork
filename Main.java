package com.company;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

// Main class of entry
public class Main {

    // Main entry method.
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Enter Stock Market IP Address: ");
                if (Facade.initiate(scanner.nextLine())) {
                    System.out.println("You have " + Facade.displayMoney() + " in your balance.");
                    while (true) {
                        System.out.println("What would you like to do?");
                        String command = scanner.nextLine();
                        if (command.equals("exit") || command.equals("Exit") || command.equals("EXIT")) {
                            Facade.exit(Facade.clientSocket);
                            System.exit(0);
                        }
                        else if (command.equals("auto") || command.equals("Auto") || command.equals("AUTO")) {
                            while (true) {
                                System.out.println("How many cycles?");
                                String response = scanner.nextLine();
                                if (response.matches("\\d+")) {
                                    Double inital = Facade.money;
                                    Facade.autoCycle(Facade.clientSocket, Integer.valueOf(response));
                                    Facade.setMoney(Facade.clientSocket);
                                    System.out.println("Money made: " + NumberFormat.getCurrencyInstance(new Locale("en", "GB")).format(Facade.money - inital));
                                    break;
                                }
                                    System.out.println("Please enter a valid number of cycles.");
                            }
                        }

                        else if (command.equals("display") || command.equals("Display") || command.equals("DISPLAY")) {
                            while (true) {
                                System.out.println("What do you want to display?");
                                String response = scanner.nextLine();
                                if (response.equals("stocks") || response.equals("Stocks") || response.equals("STOCKS")) {
                                    Facade.display();
                                    break;
                                }
                                else if (response.equals("owned") || response.equals("Owned") || response.equals("OWNED")) {
                                    Facade.displayOwned();
                                    break;
                                }
                                else if (response.equals("money") || response.equals("Money") || response.equals("MONEY")) {
                                    System.out.println(Facade.displayMoney());
                                    break;
                                }
                                else if (response.equals("help") || response.equals("HELP") || response.equals("Help")) {
                                    System.out.println("STOCKS - Displays all current stock information.");
                                    System.out.println("OWNED - Displays all currently owned stocks.");
                                    System.out.println("MONEY - Displays current balance.");
                                }
                                else {
                                    System.out.println("Please enter a valid command or enter \"Help\"");
                                }
                            }
                        }
                        else if (command.equals("buy") || command.equals("Buy") || command.equals("BUY")) {
                            loop:
                            while (true) {
                                System.out.println("What company would you like to buy shares in?");
                                String response = scanner.nextLine();
                                if (response.equals("exit") || response.equals("Exit") || response.equals("EXIT")) { break; }
                                for (int i = 0; i < Facade.stocks.size(); i++) {
                                    if (Facade.stocks.get(i).getName().equals(response)) {
                                        while (true) {
                                            Stock stock = Facade.stocks.get(i);
                                            System.out.println("Company: " + stock.getName() + ", Price: " + stock.getPrice().toString() + ", Change: " + stock.getChange().toString() + ", Owned: " + stock.getOwned().toString());
                                            System.out.println("How many shares would you like to buy?");
                                            response = scanner.nextLine();
                                            if (response.matches("\\d+")) {
                                                if (Facade.buy(Facade.clientSocket, stock.getName(), Integer.valueOf(response))) {
                                                    System.out.println("Brought " + response + " shares in " + stock.getName() + " for " + NumberFormat.getCurrencyInstance(new Locale("en", "GB")).format(stock.getPrice() * Integer.valueOf(response)));
                                                    break loop;
                                                }
                                            }
                                            else if (response.equals("exit") || response.equals("Exit") || response.equals("EXIT")) {
                                                break loop;
                                            }
                                        }
                                    }
                                }
                                System.out.println("Please enter a valid company or type \"EXIT\" to exit.");
                            }
                        }
                        else if (command.equals("sell") || command.equals("Sell") || command.equals("SELL")) {
                            loop:
                            while (true) {
                                if (Facade.displayOwned()) {
                                    System.out.println("Which company's shares would you like to sell?");
                                    String response = scanner.nextLine();
                                    if (response.equals("exit") || response.equals("Exit") || response.equals("EXIT")) {
                                        break;
                                    }
                                    for (int i = 0; i < Facade.stocks.size(); i++) {
                                        if (Facade.stocks.get(i).getName().equals(response) && Facade.stocks.get(i).getOwned() > 0) {
                                            while (true) {
                                                Stock stock = Facade.stocks.get(i);
                                                System.out.println("Company: " + stock.getName() + ", Price: " + stock.getPrice().toString() + ", Change: " + stock.getChange().toString() + ", Owned: " + stock.getOwned().toString());
                                                System.out.println("How many shares would you like to sell?");
                                                response = scanner.nextLine();
                                                if (response.matches("\\d+")) {
                                                    int value = Integer.valueOf(response);
                                                    if (value >= 0 && value <= stock.getOwned()) {
                                                        if (Facade.sell(Facade.clientSocket, stock.getName(), value)) {
                                                            System.out.println("Sold " + response + " shares in " + stock.getName() + " for " + NumberFormat.getCurrencyInstance(new Locale("en", "GB")).format(stock.getPrice() * value));
                                                            break loop;
                                                        }
                                                    }
                                                } else if (response.equals("exit") || response.equals("Exit") || response.equals("EXIT")) {
                                                    break loop;
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    System.out.println("You do not own any stocks at the moment.");
                                    break;
                                }
                                System.out.println("Please enter a valid company or type \"EXIT\" to exit.");
                            }
                        }
                        else if (command.equals("help") || command.equals("Help") || command.equals("HELP")) {
                            System.out.println("AUTO - Will automatically buy and sell stocks to maximise profits.");
                            System.out.println("DISPLAY - Will display stocks, owned stocks or current balance.");
                            System.out.println("BUY - Buy stock on the stock market.");
                            System.out.println("SELL - Sell stock on the stock market.");
                            System.out.println("EXIT - Will Exit the application.");
                        }
                        else {
                            System.out.println("Please enter a valid command or type \"HELP\" for more commands.");
                        }
                    }
                }
                else {
                    System.out.println("Incorrect IP Address.");
                }
            }
        } catch (Exception e) {
            System.out.println("Failed with error: " + e);
        }
    }
}