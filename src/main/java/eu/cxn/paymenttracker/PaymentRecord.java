package eu.cxn.paymenttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * curency and amount
 */
public class PaymentRecord extends CurrencyCode {

    private static final Pattern P_PATTERN = Pattern.compile("^ *((?<C>" + CurrencyCode.C_PATTERN + ") *|(?<A>-?\\d+) *){2}$");

    private Long amount;

    private ExchangeRateRecord exchange;

    /**
     * @param code
     * @param amount
     */
    public PaymentRecord(String code, Long amount) {

        super(code);
        this.amount = amount;

        isAmountValid();
    }

    public void isAmountValid() {
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
     * if not set, return null
     *
     */
    public ExchangeRateRecord getExchange() {
        return exchange;
    }

    public void setExchange( ExchangeRateRecord exchange ) {
        this.exchange = exchange;
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
            } catch (IllegalArgumentException nfe) {
                return null;
            }
        }

        return null;
    }

    /**
     *
     */
    public String print() {
        String o = String.format("%s %d", getCode(), getAmount());

        if( getExchange() != null ) {
            o += " " + getExchange().print(getAmount());
        }

        return o;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return getCode() + " " + getAmount() ;
    }
}
