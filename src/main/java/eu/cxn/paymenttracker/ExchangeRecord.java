package eu.cxn.paymenttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExchangeRecord extends CurrencyCode {

    private static final Pattern P_PATTERN = Pattern.compile("^("+ CurrencyCode.C_PATTERN +") (\\d+(\\.\\d+)?)$");

    Double invRate;

    public ExchangeRecord(String code, Double invRate) {
        super(code);

        if( invRate < 0.000001 ) {
            throw new IllegalArgumentException("Inversion exchange < 0.000001");
        }
        this.invRate = invRate;
    }

    /**
     * print amount with exchange rate
     *
     * @param amount
     */
    public String print(long amount) {
        return String.format("%.2f", amount * invRate);
    }

    /**
     * parse line into exchange record
     *
     * @param line
     * @return
     */
    public static ExchangeRecord parse( String line ) {
        Matcher m = P_PATTERN.matcher(line);
        if( m.matches()) {
            try {
                return new ExchangeRecord(m.group(1), Double.parseDouble(m.group(2)));
            } catch( IllegalArgumentException ise ) {
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return String.format("%s %.6f", getCode(), invRate);
    }
}
