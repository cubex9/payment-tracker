package eu.cxn.paymenttracker;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class PaymentTrackerAppTest {

    private ByteArrayOutputStream baos;

    @Before
    public void setUp() {

        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        System.setErr(new PrintStream(baos));
    }

    @Test
    public void mainArguments() {

        PaymentTrackerApp.main(new String[]{
                "-q",
                "-e",
                "-f", "src/test/resources/examples/bsc.txt",
                "-x", "src/test/resources/examples/usd.exchanges"}
        );

        assertEquals("USD 1000\n" +
                        "HKD 100\n" +
                        "USD -100\n" +
                        "RMB 2000\n" +
                        "HKD 200\n" +
                        "payment stats after read: src/test/resources/examples/bsc.txt" +
                        "\n" +
                        "USD 900\n" +
                        "HKD 300 (USD 38.35)\n" +
                        "RMB 2000 (USD 304.83)\n",
                baos.toString().replaceAll("\r", ""));

    }

    @Test
    public void mainHelp() {

        PaymentTrackerApp.main(new String[]{"-?"});

        assertEquals("usage: payment-tracker\n" +
                        " -?         this message\n" +
                        " -e         print input data to output\n" +
                        " -f <arg>   input file name\n" +
                        " -q         quit after read file\n" +
                        " -x <arg>   exchange rates file\n"
                , baos.toString().replaceAll("\r", ""));
    }

    @Test
    public void mainExp() {

        PaymentTrackerApp.main(new String[]{"-f"});
        assertEquals("Missing argument for option: f\n" +
                        "usage: payment-tracker\n" +
                        " -?         this message\n" +
                        " -e         print input data to output\n" +
                        " -f <arg>   input file name\n" +
                        " -q         quit after read file\n" +
                        " -x <arg>   exchange rates file\n"
                , baos.toString().replaceAll("\r", ""));
    }

    @Test
    public void mainIn() {

        System.setIn(new ByteArrayInputStream("USD 900".getBytes(StandardCharsets.UTF_8)));
        PaymentTrackerApp.main(new String[]{"-e"});

        assertEquals("USD 900\n", baos.toString().replaceAll("\r", ""));
    }

    @Test
    public void mainFileEx() {
        PaymentTrackerApp.main(new String[]{"-f", "test"});
        assertEquals("Can't read file: test (The system cannot find the file specified)\n", baos.toString().replaceAll("\r", ""));
    }

    @Test
    public void mainExchangesEx() {

        PaymentTrackerApp.main(new String[]{"-x", "test"});
        assertEquals("Can't read exchange rates file: test (The system cannot find the file specified)\n", baos.toString().replaceAll("\r", ""));


    }
}