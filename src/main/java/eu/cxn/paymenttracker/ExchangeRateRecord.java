package eu.cxn.paymenttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExchangeRateRecord extends CurrencyCode {

    /* exchanges base, in future is easy change static into instance property */
    public static final String BASE_CURRENCY = "USD";

    private static final Pattern P_PATTERN = Pattern.compile("^(" + CurrencyCode.C_PATTERN + ") (\\d+(\\.\\d{6})?)$");

    /**
     * inverted exchange rate to BASE_CURRENCY
     */
    private Double invRate;

    public ExchangeRateRecord(String code, Double invRate) {
        super(code);
        this.invRate = invRate;

        isInvRateValid();
    }

    public void isInvRateValid() {

        if (invRate == null || invRate < 0.000001) {
            throw new IllegalArgumentException("Inversion exchange < 0.000001");
        }
    }

    /**
     * print amount with exchange rate
     *
     * @param amount
     */
    public String print(long amount) {
        return String.format("(%s %.2f)", BASE_CURRENCY, amount * invRate);
    }

    /**
     * parse line into exchange record
     *
     * @param line
     * @return
     */
    public static ExchangeRateRecord parse(String line) {
        Matcher m = P_PATTERN.matcher(line);
        if (m.matches()) {
            return new ExchangeRateRecord(m.group(1), Double.parseDouble(m.group(2)));
        }

        throw new IllegalArgumentException("Incorrect format exchange entry: " + line);
    }

    @Override
    public String toString() {
        return String.format("%s %.6f", getCode(), invRate);
    }
}
