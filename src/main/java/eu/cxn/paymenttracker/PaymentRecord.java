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
        /* in this time, all long values is valid */
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
     */
    public ExchangeRateRecord getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeRateRecord exchange) {
        if( exchange != null && getCode().equals(ExchangeRateRecord.BASE_CURRENCY)) {
            throw new IllegalArgumentException("Impossible set exchange rate into :" + ExchangeRateRecord.BASE_CURRENCY);
        }

        this.exchange = exchange;
    }

    /**
     * @param line
     * @return
     */
    public static PaymentRecord parse(String line) {
        Matcher m = P_PATTERN.matcher(line);

        if (m.matches() && m.group("C") != null && m.group("A") != null) {
            return new PaymentRecord(m.group("C"), Long.parseLong(m.group("A")));
        }

        throw new IllegalArgumentException("Incorrect format payment entry: " + line);
    }

    /**
     *
     */
    public String print() {
        String o = String.format("%s %d", getCode(), getAmount());

        if (getExchange() != null) {
            o += " " + getExchange().print(getAmount());
        }

        return o;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return getCode() + " " + getAmount();
    }
}
