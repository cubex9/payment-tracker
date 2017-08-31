package eu.cxn.paymenttracker;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentRecord {

    private static final Pattern PATTERN = Pattern.compile("^\\s*((?<CUR>[A-Z]{3})|(?<AMO>\\-?[0-9]+)|\\s+){3}\\s*$");

    private String currency;

    private Long amount;

    public PaymentRecord(String currency, Long amount) {

        this.currency = currency;
        this.amount = amount;
    }

    public void add(Long inc) {
        amount += inc;
    }


    public String getCurrency() {
        return currency;
    }

    public long getAmount() {
        return amount;
    }

    public static PaymentRecord parse(String line) {
        Matcher m = PATTERN.matcher(line);

        if (m.matches()) {
            if (m.group("CUR") != null && m.group("AMO") != null) {
                return new PaymentRecord(m.group("CUR"), Long.parseLong(m.group("AMO")));
            }
        }

        return null;
    }

    public void print(PrintStream out) {
        out.println(toString());
    }

    @Override
    public String toString() {
        return currency + " " + amount;
    }
}
