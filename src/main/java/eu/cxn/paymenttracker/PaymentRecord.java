package eu.cxn.paymenttracker;

public class PaymentRecord<T> {

    private String currency;

    private T amount;

    public PaymentRecord( String currency, T amount ) {

        this.setCurrency(currency);
        this.setAmount(amount);
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public T getAmount() {
        return amount;
    }

    public void setAmount(T amount) {
        this.amount = amount;
    }
}
