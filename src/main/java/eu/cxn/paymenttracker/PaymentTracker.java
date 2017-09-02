package eu.cxn.paymenttracker;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

/**
 *
 */
public class PaymentTracker {

    /* synchronized data repository */
    private PaymentTrackerRepository paymentRepository;

    /* exchanges table */
    private Map<String, ExchangeRecord> exchanges;

    /* exchange base */
    private String exchangeBase;

    /* output print stream */
    private PrintStream output;

    /* echo, print input data to output,
       disabled by default
    */
    private boolean echo = false;

    /* printer thread */
    private Thread printerThread;

    /**
     * @param output
     */
    public PaymentTracker(PrintStream output, String exchangeBase) {
        if (output == null) {
            throw new IllegalArgumentException("output stream is: NULL");
        }
        this.output = output;
        this.exchangeBase = exchangeBase;

        paymentRepository = new PaymentTrackerRepository();
        exchanges = new Hashtable<>();
    }

    /**
     *
     */
    public void enableEcho() {
        echo = true;
    }

    /**
     * read input data from stream
     *
     * @param in
     * @throws IOException
     */
    public void reader(InputStream in) throws IOException {
        reader(in,this::resolveInputLine);
    }

    /**
     * exchanges reader
     */
    public void exchangesReader(InputStream in) throws IOException {
        if (exchangeBase == null) {
            throw new IllegalStateException("Base exchange currnecy code is not set");
        }

        reader(in,this::resolveExchangeLine);
    }

    /**
     * @param in
     */
    private void reader(InputStream in, Function<String,Boolean> resolver) throws IOException {
        if( in == null) {
            throw new IllegalArgumentException("input stream is NULL");
        }

        /* read to EOF or 'quit' */
        Scanner s = new Scanner(in);

        while (s.hasNextLine()) {
            String line = s.nextLine();

            /* ignore empty lines and call line resolver  */
            if (line.isEmpty() || resolver.apply(line)) {
                continue;
            } else {
                break;
            }
        }

        if (s.ioException() != null) {
            throw s.ioException();
        }
    }

    /**
     * manage one input line of data source
     *
     * @param line
     * @return
     */
    private boolean resolveInputLine(String line) {

        /* quit command */
        if (line.contains("quit")) {
            printCurrentAmounts("final stats: ");
            return false;
        }

        /* print echo */
        if (echo) {
            printEcho(line);
        }

        /* try parse line */
        PaymentRecord r = PaymentRecord.parse(line);
        if (r != null) {

            /* if ok, apply to repository */
            paymentRepository.put(r);
        } else {
            System.err.println("Payment: Invalid input ( " + line + " )");
        }

        return true;
    }

    /**
     * manage one line of exchange source stream
     *
     * @param line
     * @return
     */
    private boolean resolveExchangeLine(String line) {

        /* try parse line */
        ExchangeRecord r = ExchangeRecord.parse(line);
        if (r != null && !r.getCode().equals(exchangeBase)) {

            /* if ok, apply to repository */
            paymentRepository.put(r);
        } else {
            System.err.println("Exchange: Invalid input ( " + line + " )");
        }

        return true;
    }


    /**
     * @param period
     */
    public void printer(String message, long period) {
        if (printerThread != null) {
            throw new IllegalStateException("Impossible start second printer");
        }

        printerThread = new Thread() {

            @Override
            @SuppressWarnings("CallToThreadRun")
            public void run() {
                try {

                    while (true) {
                        /* wait for print */
                        Thread.sleep(period);

                        printCurrentAmounts(message);
                    }

                } catch (InterruptedException ie) {
                    printerThread = null;
                }
            }
        };

        printerThread.start();
    }

    /**
     * don't mix printCurrentAmounts and printEcho
     *
     * @param line
     */
    private synchronized void printEcho(String line) {
        output.println(line);
    }

    /**
     *
     */
    public synchronized void printCurrentAmounts(String message) {

        if( message != null ) {
            output.println("\r"+message);
        }

        /* ( code, paymentRecord, exchangeRecord ) */
        paymentRepository.forEach((c, r, e) -> {

            if (r.getAmount() != 0) {
                String o = c + " " + r.print();

                if (e != null) {
                    o += " (" + exchangeBase + " " + e.print(r.getAmount()) + ")";
                }

                output.println(o);
            }
        });

    }

    /**
     * interrupt printer thread, call on end
     */
    public void exit() {
        if (printerThread != null && printerThread.isAlive()) {
            printerThread.interrupt();
        }

        printerThread = null;
    }
}
