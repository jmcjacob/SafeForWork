package com.company;

public class Stock {
    String Name;
    Double Price;
    Double Change;
    Integer Owned = 0;

    public Stock(String _Name, Double _Price, Double _Change) {
        Name = _Name;
        Price = _Price;
        Change = _Change;
    }

    public Stock(String _Name, Double _Price, Double _Change, int _Owned) {
        Name = _Name;
        Price = _Price;
        Change = _Change;
        Owned = _Owned;
    }

    public String getName() {return Name;}

    public Double getPrice() {return Price;}

    public Double getChange() {return Change;}

    public Integer getOwned() {return Owned;}

    public Boolean addOwned(Integer amount) {
        Owned = Owned + amount;
        return true;
    }

    public Boolean takeOwned(Integer amount) {
        if (Owned - amount >= 0) {
            Owned = Owned - amount;
        }
        return false;
    }

    public void update(Double price, Double change) {
        Price = price;
        Change = change;
    }
}
