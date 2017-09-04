package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ExchangeRateRecordTest extends AbstractPaymentTrackerTest {

    private ExchangeRateRecord er;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        er = new ExchangeRateRecord("CZK", 1.5);
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
    public void noParse1() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Incorrect format exchange entry: CZD -21.1");

        ExchangeRateRecord.parse("CZD -21.1");
    }

    @Test
    public void noParse2() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Inversion exchange < 0.000001");

        ExchangeRateRecord.parse("CZK 0.0000009");
    }

    @Test
    public void noParse3() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Incorrect format exchange entry: CZK .0123");

        ExchangeRateRecord.parse("CZK .0123");

    }

}