package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PaymentRecordTest extends AbstractPaymentTrackerTest {

    private PaymentRecord pr;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        pr = new PaymentRecord("USD", 100L);
    }

    @Test
    public void getAmount() throws Exception {

        assertEquals(100L, pr.getAmount());
    }

    @Test
    public void inc() throws Exception {

        pr.inc(new PaymentRecord("USD", 20L));

        assertEquals(120L, pr.getAmount());

        pr.inc(new PaymentRecord("USD", -20L));

        assertEquals(100L, pr.getAmount());

    }

    @Test
    public void exchangePrint() throws Exception {

        PaymentRecord paymentRecord = new PaymentRecord("CZK", 10L);
        paymentRecord.setExchange( new ExchangeRateRecord("CZK", 1.1));

        assertEquals("CZK 10 (USD 11.00)", paymentRecord.print());

    }

    @Test
    public void parse() throws Exception {

        assertEquals("USD 21", PaymentRecord.parse("USD 21").toString());


        assertEquals("USD 21", PaymentRecord.parse("21 USD").toString());
        assertEquals("USD -21", PaymentRecord.parse("USD -21").toString());
        assertEquals("USD -21", PaymentRecord.parse("-21 USD").toString());
        assertEquals("USD 21", PaymentRecord.parse("USD21").toString());
        assertEquals("USD -21", PaymentRecord.parse("USD-21").toString());

        assertEquals("USD 21", PaymentRecord.parse("  USD     21   ").toString());
    }

    @Test
    public void noParse1() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Incorrect format payment entry: USD --21");
        PaymentRecord.parse("USD --21");
    }

    @Test
    public void noParse2() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Incorrect format payment entry: usd 21");
        PaymentRecord.parse("usd 21");
    }


    @Test
    public void noParse3() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Incorrect format payment entry: 21");
        PaymentRecord.parse("21");
    }

    @Test
    public void noParse4() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Incorrect format payment entry: USD");
        PaymentRecord.parse("USD");
    }

    @Test
    public void noParse5() {

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("For input string: \"45454354363563465365465465645654\"");
        PaymentRecord.parse("USD 45454354363563465365465465645654");
    }
}