package eu.cxn.paymenttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * curency and amount
 */
public class PaymentRecord extends CurrencyCode {

    private static final Pattern P_PATTERN = Pattern.compile("^ *((?<C>" + CurrencyCode.C_PATTERN + ") *|(?<A>-?\\d+) *){2}$");

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
        Matcher m = P_PATTERN.matcher(line);

        if (m.matches() && m.group("C") != null && m.group("A") != null) {
            try {
                return new PaymentRecord(m.group("C"), Long.parseLong(m.group("A")));
            } catch (NumberFormatException nfe) {
                return null;
            }
        }

        return null;
    }

    /**
     *
     */
    public String print() {
        return String.format("%d", amount);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return getCode() + " " + getAmount();
    }
}
