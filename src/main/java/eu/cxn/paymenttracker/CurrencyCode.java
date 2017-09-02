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
}
