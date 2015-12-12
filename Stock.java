package com.company;

/**
 * Stock class to store the information retrieved from the server and keep track of owned stocks.
 * @see #Name The name of the company.
 * @see #Price The price of the stock.
 * @see #Change The change compared to the last price of the stock.
 * @see #Owned How many of these stocks are owned by the client.
 */
public class Stock {
    String Name;
    Double Price;
    Double Change;
    Integer Owned = 0;

    /**
     * Constructor for unowned stocks.
     * @param _Name The name of the company.
     * @param _Price The price of the stock.
     * @param _Change The change compared to the last price of the stock.
     */
    public Stock(String _Name, Double _Price, Double _Change) {
        Name = _Name;
        Price = _Price;
        Change = _Change;
    }

    /**
     * Constructor for owned stocks.
     * @param _Name The name of the company.
     * @param _Price The price of the stock.
     * @param _Change The change compared to the last price of the stock.
     * @param _Owned How many of these stocks are owned by the client.
     */
    public Stock(String _Name, Double _Price, Double _Change, int _Owned) {
        Name = _Name;
        Price = _Price;
        Change = _Change;
        Owned = _Owned;
    }

    /**
     * getter for Name
     * @return Name of stock.
     */
    public String getName() {return Name;}

    /**
     * getter for Price
     * @return Price of stock.
     */
    public Double getPrice() {return Price;}

    /**
     * getter for price Change
     * @return Change of stock.
     */
    public Double getChange() {return Change;}

    /**
     * getter for Owned stocks
     * @return number of stocks Owned
     */
    public Integer getOwned() {return Owned;}

    /**
     * On purchase of stocks, they will be added to owned stocks.
     * @param amount number of stocks to add to owned stocks.
     * @return Boolean value to mark completion
     */
    public Boolean addOwned(Integer amount) {
        Owned = Owned + amount;
        return true;
    }

    /**
     * When selling stocks, they will be removed from owned stocks.
     * @param amount number of stocks to remove from owned stocks.
     * @return Boolean value to mark completion
     */
    public Boolean takeOwned(Integer amount) {
        if (Owned - amount >= 0) {
            Owned = Owned - amount;
        }
        return false;
    }

    /**
     * When reading in new stock values, the prices change and this is recorded in the stock list.
     * @param price The price of the stock.
     * @param change The change compared to the last price of the stock.
     */
    public void update(Double price, Double change) {
        Price = price;
        Change = change;
    }
}
