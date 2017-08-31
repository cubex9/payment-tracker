package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class PaymentTrackerTest {

    private ByteArrayOutputStream baos;

    private PrintStream output;

    private PaymentTracker tracker;

    @Before
    public void setUp() {
        baos = new ByteArrayOutputStream();
        output = new PrintStream(baos);
        tracker = new PaymentTracker(output);
    }

    @Test
    public void setPrintStream() throws Exception {
    }

    @Test
    public void reader() throws Exception {

        InputStream in = new ByteArrayInputStream("USD 21\nUSD 22\nUSD 23".getBytes(StandardCharsets.UTF_8));

        tracker.enableEcho();
        tracker.reader(in);


        assertEquals("USD 21\nUSD 22\nUSD 23\n", baos.toString());
    }

    @Test
    public void printer() throws Exception {


    }

}