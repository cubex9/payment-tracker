package eu.cxn.paymenttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * curency and amount
 */
public class PaymentRecord extends CurrencyCode {

    private static final Pattern PATTERN = Pattern.compile("^\\s*((?<CUR>[A-Z]{3})|(?<AMO>\\-?[0-9]+)|\\s+){3}\\s*$");


    private Long amount;

    /**
     * @param code
     * @param amount
     */
    public PaymentRecord(String code, Long amount) {

        super(code);
        this.amount = amount;
    }

    /**
     * @param
     */
    public void inc(PaymentRecord r) {
        amount += r.getAmount();
    }

    /**
     * @return
     */
    public long getAmount() {
        return amount;
    }

    /**
     * @param line
     * @return
     */
    public static PaymentRecord parse(String line) {
        Matcher m = PATTERN.matcher(line);

        if (m.matches()) {
            if (m.group("CUR") != null && m.group("AMO") != null) {
                try {
                    return new PaymentRecord(m.group("CUR"), Long.parseLong(m.group("AMO")));
                } catch( NumberFormatException nfe ) {
                    /* return null */
                }
            }
        }

        return null;
    }

    /**
     *
     */
    public String print() {
        return String.format("%d",amount);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return getCode() + " " + getAmount();
    }
}
