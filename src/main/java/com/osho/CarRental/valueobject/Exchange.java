package com.osho.CarRental.valueobject;

// Used to take returned response values from Exchange microservice

public class Exchange {

    double amountInSek;
    String fromCurrency;
    String toCurrency;
    double amountInEur;

    public Exchange() {
    }

    public Exchange(double amountInSek, String fromCurrency, String toCurrency, double amountInEur) {
        this.amountInSek = amountInSek;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amountInEur = amountInEur;
    }

    public double getAmountInSek() {
        return amountInSek;
    }

    public void setAmountInSek(double amountInSek) {
        this.amountInSek = amountInSek;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getAmountInEur() {
        return amountInEur;
    }

    public void setAmountInEur(double amountInEur) {
        this.amountInEur = amountInEur;
    }
}
