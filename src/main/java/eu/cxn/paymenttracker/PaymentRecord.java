package eu.cxn.paymenttracker;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * curency and amount
 */
public class PaymentRecord {

    private static final Pattern PATTERN = Pattern.compile("^\\s*((?<CUR>[A-Z]{3})|(?<AMO>\\-?[0-9]+)|\\s+){3}\\s*$");

    private String currency;

    private Long amount;

    /**
     *
     * @param currency
     * @param amount
     */
    public PaymentRecord(String currency, Long amount) {

        this.currency = currency;
        this.amount = amount;
    }

    /**
     *
     * @param inc
     */
    public void add(Long inc) {
        amount += inc;
    }

    /**
     *
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return
     */
    public long getAmount() {
        return amount;
    }

    /**
     *
     * @param line
     * @return
     */
    public static PaymentRecord parse(String line) {
        Matcher m = PATTERN.matcher(line);

        if (m.matches()) {
            if (m.group("CUR") != null && m.group("AMO") != null) {
                return new PaymentRecord(m.group("CUR"), Long.parseLong(m.group("AMO")));
            }
        }

        return null;
    }

    /**
     *
     * @param out
     */
    public void print(PrintStream out) {
        out.println(toString());
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return currency + " " + amount;
    }
}
