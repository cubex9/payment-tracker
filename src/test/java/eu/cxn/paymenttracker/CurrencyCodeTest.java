package eu.cxn.paymenttracker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CurrencyCodeTest extends AbstractPaymentTrackerTest {

    @Test
    public void getCode() throws Exception {

        assertEquals("USD", new CurrencyCode("USD").getCode());
    }

    @Test
    public void currencyProperties() {

        System.out.println( System.getProperty("java.util.currency.data"));
    }
}