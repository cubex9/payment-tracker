package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

        tracker.enableEcho();
        tracker.reader(PaymentTrackerApp.resourceInputStream("/examples/bsc.txt"));


        assertEquals("USD 1000\n" +
                "HKD 100\n" +
                "USD -100\n" +
                "RMB 2000\n" +
                "HKD 200", baos.toString());
    }

    @Test
    public void printer() throws Exception {


    }

}