package eu.cxn.paymenttracker;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;
import java.util.function.BiFunction;

/**
 *
 */
public class PaymentTracker {

    /* synchronized data repository */
    private PaymentTrackerRepository paymentRepository;

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
        return reader(in, this::resolveInputLine, true);
    }

    /**
     * read input data from stream
     *
     * @param in
     * @throws IOException
     */
    public boolean reader(InputStream in, boolean skypSyntaxError) throws IOException {
        return reader(in, this::resolveInputLine, skypSyntaxError);
    }

    /**
     * exchanges reader
     */
    public boolean exchangesReader(InputStream in) throws IOException {
        if (exchangeBase == null) {
            throw new IllegalStateException("Base exchange currnecy code is not set");
        }

        return reader(in, this::resolveExchangeLine, false);
    }

    /**
     * @param in
     * @return false if error
     */
    private boolean reader(InputStream in, BiFunction<String, Boolean, Boolean> resolver, boolean skipSyntaxError) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("input stream is NULL");
        }

        /* read to EOF or 'quit' */
        Scanner s = new Scanner(in);
        boolean result = true;

        while (s.hasNextLine()) {
            String line = s.nextLine();

            /* ignore empty lines and call line resolver  */
            if (!(line.isEmpty() || (result = resolver.apply(line, skipSyntaxError)))) {
                break;
            }
        }

        if (s.ioException() != null) {
            throw s.ioException();
        }

        return result;
    }

    /**
     * manage one input line of data source
     *
     * @param line
     * @return
     */
    private boolean resolveInputLine(String line, boolean skypSyntaxError) {

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
            return skypSyntaxError;
        }

        return true;
    }

    /**
     * manage one line of exchange source stream
     *
     * @param line
     * @return
     */
    private boolean resolveExchangeLine(String line, boolean skypSyntaxError) {

        /* try parse line */
        ExchangeRecord r = ExchangeRecord.parse(line);
        if (r != null && !r.getCode().equals(exchangeBase)) {

            /* if ok, apply to repository */
            paymentRepository.put(r);
        } else {
            System.err.println("Exchange: Invalid input ( " + line + " )");
            return skypSyntaxError;
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

                while (true) {

                    try {
                        /* wait for next period */
                        Thread.sleep(period);

                    } catch (InterruptedException ie) {

                        printerThread = null;
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
