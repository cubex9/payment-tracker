package eu.cxn.paymenttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentRecord {

    private String currency;

    private Long amount;

    public PaymentRecord( String currency, Long amount ) {

        this.setCurrency(currency.toUpperCase());
        this.setAmount(amount);
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public static PaymentRecord parse( String line ) {
        Matcher m = Pattern.compile("((?<CUR>[A-Z]{3})|(?<AMO>[0-9]+)| ){3}").matcher(line);

        if( m.matches() ) {
            if( m.group("CUR") != null && m.group("AMO") != null ) {
                return new PaymentRecord(m.group("CUR"), Long.parseLong(m.group("AMO")));
            }
        }

        return null;
    }
}
