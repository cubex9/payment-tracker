package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExchangeRateRecordTest extends AbstractPaymentTrackerTest {

    private ExchangeRateRecord er;

    @Before
    public void setUp() {
        er = new ExchangeRateRecord("CZK", 1.5 );
    }

    @Test
    public void print() throws Exception {
        assertEquals("(USD 1.50)", er.print(1L));
    }

    @Test
    public void parse() throws Exception {

        assertEquals("CZK 21.100000", ExchangeRateRecord.parse("CZK 21.1").toString());
        assertEquals("CZK 21.000000", ExchangeRateRecord.parse("CZK 21").toString());
    }

    @Test
    public void noParse() {
        assertEquals(null, ExchangeRateRecord.parse("CZD -21.1"));
        assertEquals(null, ExchangeRateRecord.parse("CZK 0.0000009"));
        assertEquals(null, ExchangeRateRecord.parse("CZK .0123"));
    }

}