package eu.cxn.paymenttracker;

public class CurrencyCode {

    public static final String C_PATTERN = "[A-Z]{3}";

    String code;

    public CurrencyCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static void isValid(String code) throws IllegalArgumentException {
        /* can't use this, expamle for RMB library don't validate old codes,
           only if java.util.currency.data property change
         */
        //Currency.getInstance(code);
    }
}
