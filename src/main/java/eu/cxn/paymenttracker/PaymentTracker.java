package eu.cxn.paymenttracker;


import java.io.*;
import java.util.Date;
import java.util.function.Consumer;

/**
 * Head class of payment-tracker
 *
 * base use with console in/out:
 * <code>
 *
 *     PaymentTracker pt = new PaymentTracker( System.out );
 *     pt.reader(System.in);
 *     pt.stop();
 *
 * </code>
 */
public class PaymentTracker {

    /* synchronized data repository */
    private PaymentTrackerRepository paymentRepository;

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
    public PaymentTracker(PrintStream output) {
        if (output == null) {
            throw new IllegalArgumentException("output stream is: NULL");
        }
        this.output = output;
        this.paymentRepository = new PaymentTrackerRepository();
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
    public boolean reader(InputStream in) throws IOException {
        return reader(in, l -> paymentRepository.put(PaymentRecord.parse(l)), true, echo, true);
    }

    /**
     * read input data from stream
     *
     * @param in
     * @throws IOException
     */
    public boolean reader(InputStream in, boolean skypSyntaxError) throws IOException {
        return reader(in, l -> paymentRepository.put(PaymentRecord.parse(l)), skypSyntaxError, echo, skypSyntaxError);
    }

    /**
     * exchanges reader
     */
    public boolean exchangesReader(InputStream in) throws IOException {
        return reader(in, l -> paymentRepository.put(ExchangeRateRecord.parse(l)), false, false, false);
    }

    /**
     * @param in
     * @return false if error
     */
    private boolean reader(InputStream in, Consumer<String> resolver, boolean quit, boolean echo, boolean skipSyntaxError) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("input stream is NULL");
        }

        try ( BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line = null;
            while((line = br.readLine())!=null) {

                if (line.isEmpty()) {
                    continue;
                }

                if (line.equals("quit")) {
                    printCurrentAmounts("final stats: ");
                    return quit;
                }

                if (echo) {
                    printEcho(line);
                }

                try {
                    resolver.accept(line);

                } catch (IllegalArgumentException iae) {

                    /* print error or exception */
                    if (skipSyntaxError) {
                        System.err.println(iae.getMessage());
                    } else {
                        throw iae;
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param period
     */
    public synchronized void printer(String message, long period) {
        if (printerThread != null) {
            throw new IllegalStateException("Impossible start second printer");
        }

        printerThread = new Thread() {

            @Override
            @SuppressWarnings("CallToThreadRun")
            public void run() {

                while (true) {

                    try {
                        /* wait on next period */
                        Thread.sleep(period);

                    } catch (InterruptedException ie) {

                        setPrinterThread(null);
                        break;
                    }

                    printCurrentAmounts(message);
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

        if (message != null) {
            output.printf("\r" + message + "%n", new Date());
        }

        /* ( code, paymentRecord, exchangeRecord ) */
        paymentRepository.forEach(r -> {

            if (r.getAmount() != 0) {
                output.println(r.print());
            }
        });

    }

    /**
     * interrupt and remove printer thread, call on end
     */
    public synchronized void stop() {
        if (printerThread != null && printerThread.isAlive()) {
            printerThread.interrupt();
        }

        setPrinterThread(null);
    }

    private synchronized void setPrinterThread(Thread t) {
        printerThread = t;
    }
}
