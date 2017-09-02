package eu.cxn.paymenttracker;


import java.io.InputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

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
     * @param out
     */
    public PaymentTracker(PrintStream out) {
        setOutput(out);

        paymentRepository = new PaymentTrackerRepository();
        exchanges = new Hashtable<>();
    }

    /**
     * @param out
     */
    public void setOutput(PrintStream out) {
        if (out == null) {
            throw new IllegalArgumentException("PrintStream NULL");
        }

        this.output = out;
    }

    /**
     *
     */
    public void enableEcho() {
        echo = true;
    }

    /**
     * exchanges reader
     */
    public void exchangesReader(String base, InputStream in) {

        exchangeBase = base;

        /* read to EOF or 'quit' */
        Scanner s = new Scanner(in);

        while (s.hasNextLine()) {
            String line = s.nextLine();

            /* ignore empty lines */
            if (line.isEmpty()) {
                continue;
            }

            /* try parse line */
            ExchangeRecord r = ExchangeRecord.parse(line);
            if (r != null && !r.getCode().equals(base)) {

                /* if ok, apply to repository */
                paymentRepository.put(r);
            } else {
                System.err.println("Exchange: Invalid input ( " + line + " )");
            }
        }

    }


    /**
     * @param in
     */
    public void reader(InputStream in) {

        /* read to EOF or 'quit' */
        Scanner s = new Scanner(in);

        while (s.hasNextLine()) {
            String line = s.nextLine();

            /* ignore empty lines */
            if (line.isEmpty()) {
                continue;
            }

            /* quit command */
            if (line.contains("quit")) {
                printCurrentAmounts();
                break;
            }

            /* print echo */
            if (echo) {
                output.println(line);
            }

            /* try parse line */
            PaymentRecord r = PaymentRecord.parse(line);
            if (r != null) {

                /* if ok, apply to repository */
                paymentRepository.put(r);
            } else {
                System.err.println("Payment: Invalid input ( " + line + " )");
            }
        }
    }

    /**
     * @param period
     */
    public void printer(long period) {
        if( printerThread != null ) {
            throw new IllegalStateException("Impossible start second printer");
        }

        printerThread = new Thread() {

            @Override
            @SuppressWarnings("CallToThreadRun")
            public void run() {
                try {

                    while (true) {
                        printCurrentAmounts();
                        output.flush();

                        /* wait for next period */
                        Thread.sleep(period);
                    }

                } catch (InterruptedException ie) {
                    printerThread = null;
                }
            }
        };

        printerThread.start();
    }

    /**
     *
     */
    public void printCurrentAmounts() {

        output.println("\rcurrent amounts: ");
        paymentRepository.forEach((c, r, e) -> {

            if (r.getAmount() != 0) {
                output.print(c + " " + r.print());

                if (e != null) {
                    output.print(" (" + exchangeBase + " " + e.print(r.getAmount()) + ")");
                }

                output.println();
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
