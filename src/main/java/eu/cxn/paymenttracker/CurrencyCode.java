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
            throw new IllegalArgumentException("code is null");
        }
        /* can't use this, example for RMB library don't validate old codes,
           only if java.util.currency.data property change
         */
        Currency.getInstance(code);
    }
}
