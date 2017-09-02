package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaymentRecordTest {

    private PaymentRecord pr;

    @Before
    public void setUp() {
        pr = new PaymentRecord("USD", 100L);
    }

    @Test
    public void getAmount() throws Exception {

        assertEquals(100L, pr.getAmount());
    }

    @Test
    public void add() throws Exception {

        pr.inc( new PaymentRecord("USD", 20L ));

        assertEquals( 120L, pr.getAmount());

        pr.inc( new PaymentRecord("USD", -20L ));

        assertEquals( 100L, pr.getAmount());

    }

    @Test
    public void parse() throws Exception {

        assertEquals("USD 21", PaymentRecord.parse("USD 21").toString());
        assertEquals( "USD 21", PaymentRecord.parse("21 USD").toString());
        assertEquals( "USD -21", PaymentRecord.parse("USD -21").toString());
        assertEquals("USD -21", PaymentRecord.parse("-21 USD").toString());
        assertEquals("USD 21", PaymentRecord.parse("USD21").toString());
        assertEquals("USD -21", PaymentRecord.parse("USD-21").toString());

        assertEquals("USD 21", PaymentRecord.parse("  USD     21   ").toString());
    }

    @Test
    public void noParse() {
        assertEquals(null, PaymentRecord.parse("USD --21"));
        assertEquals(null, PaymentRecord.parse("usd 21"));
        assertEquals(null, PaymentRecord.parse("21"));
        assertEquals(null, PaymentRecord.parse("USD"));

        assertEquals(null, PaymentRecord.parse("USD 45454354363563465365465465645654"));
    }
}