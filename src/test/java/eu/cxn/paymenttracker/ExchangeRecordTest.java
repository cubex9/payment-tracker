package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExchangeRecordTest {

    private ExchangeRecord er;

    @Before
    public void setUp() {
        er = new ExchangeRecord("CZK", 1.5 );
    }

    @Test
    public void print() throws Exception {
        assertEquals("1.50", er.print(1L));
    }

    @Test
    public void parse() throws Exception {

        assertEquals("CZK 21.100000", ExchangeRecord.parse("CZK 21.1").toString());
        assertEquals("CZK 21.000000", ExchangeRecord.parse("CZK 21").toString());
    }

    @Test
    public void noParse() {
        assertEquals(null, ExchangeRecord.parse("CZD -21.1"));
        assertEquals(null, ExchangeRecord.parse("CZK 0.0000009"));
        assertEquals(null, ExchangeRecord.parse("CZK .0123"));
    }

}