package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        tracker = new PaymentTracker(output, PaymentTrackerApp.EXCHANGE_BASE_CODE);
    }

    @Test
    public void setPrintStream() throws Exception {
    }

    @Test
    public void readAndPrint() throws Exception {

        tracker.enableEcho();
        tracker.reader(PaymentTrackerApp.resourceInputStream("/examples/bsc.txt"));


        assertEquals("USD 1000\n" +
                "HKD 100\n" +
                "USD -100\n" +
                "RMB 2000\n" +
                "HKD 200\n", baos.toString().replaceAll("\r",""));
    }

    @Test(expected = IllegalStateException.class)
    public void exchangeReaderIlegal() throws Exception {

        tracker = new PaymentTracker(output, null);
        tracker.exchangesReader(PaymentTrackerApp.resourceInputStream("/examples/usd.exchanges"));
    }

    @Test
    public void printer() throws Exception {

        tracker.reader(PaymentTrackerApp.resourceInputStream("/examples/bsc.txt"));
        tracker.printer( PaymentTrackerApp.PRINTER_MESSAGE, 1000L);

        Thread.sleep(2000L);

        assertEquals(PaymentTrackerApp.PRINTER_MESSAGE + "\n" +
                "USD 900\n" +
                "HKD 300\n" +
                "RMB 2000\n", baos.toString().replaceAll("\r",""));


        tracker.exit();
    }

    @Test(expected = IllegalStateException.class)
    public void secondPrinter() {

        tracker.printer(PaymentTrackerApp.PRINTER_MESSAGE, 10000);
        tracker.printer(PaymentTrackerApp.PRINTER_MESSAGE, 10000);
    }

    @Test
    public void quit() throws Exception {

        tracker.reader(new ByteArrayInputStream("quit\nUSD 900\n".getBytes(StandardCharsets.UTF_8)));
        assertEquals("final stats: \n", baos.toString().replaceAll("\r",""));
    }

    @Test
    public void readerInvalidInput() throws Exception {

        System.setErr(new PrintStream(baos));

        tracker.reader(new ByteArrayInputStream("USD 90a\n".getBytes(StandardCharsets.UTF_8)));
        assertEquals("Payment: Invalid input ( USD 90a )\n", baos.toString().replaceAll("\r",""));
    }

    @Test
    public void exchangeInvalidInput() throws Exception {

        System.setErr(new PrintStream(baos));

        tracker.exchangesReader(new ByteArrayInputStream("USD 90a\n".getBytes(StandardCharsets.UTF_8)));
        assertEquals("Exchange: Invalid input ( USD 90a )\n", baos.toString().replaceAll("\r",""));

    }
}