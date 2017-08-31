package eu.cxn.paymenttracker;


import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 */
public class PaymentTracker {

    /* synchronized data repository */
    private PaymentRepository paymentRepository;

    /* output print stream */
    private PrintStream output;

    /* echo, print input data to output,
       is disabled by default
    */
    private boolean echo = false;

    /**
     *
     * @param out
     */
    public PaymentTracker(PrintStream out) {
        setOutput(out);

        paymentRepository = new PaymentRepository();
    }

    /**
     *
     * @param out
     */
    public void setOutput(PrintStream out) {
        if( out == null ) {
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
     *
     * @param in
     */
    public void reader(InputStream in) {

        /* read to EOF or 'quit' */
        Scanner s = new Scanner(in);

        while (s.hasNextLine()) {
            String line = s.nextLine();

            if (line.contains("quit")) {
                break;
            }

            if (echo) {
                output.println(line);
            }

            /* try parse line */
            PaymentRecord r = PaymentRecord.parse(line);
            if (r != null) {

                /* if ok, apply to repository */
                paymentRepository.put(r);
            } else {
                System.err.println("Invalid input");
            }
        }
    }

    /**
     *
     * @param timeout
     */
    public void printer(long timeout) {

        new Thread() {

            @Override
            @SuppressWarnings("CallToThreadRun")
            public void run() {
                try {

                    while (true) {

                        output.println("current amounts: ");
                        paymentRepository.forEach(v -> {
                            if( v.getAmount() != 0 ) {
                                v.print(output);
                            }
                        });

                        Thread.sleep(timeout);
                    }

                } catch (InterruptedException ie) {

                } finally {

                }
            }
        }.start();
    }
}
