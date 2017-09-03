package eu.cxn.paymenttracker;

import java.util.Currency;

public class CurrencyCode {

    public static final String C_PATTERN = "[A-Z]{3}";

    private String code;

    public CurrencyCode(String code) {
        this.code = code;

        isCodeValid();
    }

    public String getCode() {
        return code;
    }

    public void isCodeValid()  {
        if( code == null ) {
            throw new IllegalArgumentException("currency code is null");
        }

        try {
            Currency.getInstance(code);
        } catch (IllegalArgumentException iar) {
            throw new IllegalArgumentException("Unknown currency code: " + code);
        }
    }
}
