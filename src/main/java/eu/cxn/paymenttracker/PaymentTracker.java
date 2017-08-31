package eu.cxn.paymenttracker;


import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class PaymentTracker {

    private PaymentRepository paymentRepository;

    private PrintStream printStream;

    private boolean echo = false;

    public PaymentTracker(PrintStream ps) {
        setPrintStream(ps);

        paymentRepository = new PaymentRepository();
    }

    public void setPrintStream(PrintStream ps) {
        if( ps == null ) {
            throw new IllegalArgumentException("PrintStream NULL");
        }

        this.printStream = ps;
    }

    public void enableEcho() {
        echo = true;
    }


    public void reader(InputStream in) {

        /* read to EOF or 'quit' */
        Scanner s = new Scanner(in);

        while (s.hasNextLine()) {
            String line = s.nextLine();

            if (line.contains("quit")) {
                break;
            }

            if (echo) {
                printStream.println(line);
            }

            /* try parse line */
            PaymentRecord r = PaymentRecord.parse(line);
            if (r != null) {

                /* if ok, apply to repository */
                paymentRepository.put(r);
                System.out.println("OK");
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public void printer(long timeout) {

        new Thread() {

            @Override
            @SuppressWarnings("CallToThreadRun")
            public void run() {
                try {

                    while (true) {

                        printStream.println("current amounts: ");
                        paymentRepository.forEach(v -> {
                            if( v.getAmount() != 0 ) {
                                v.print(printStream);
                            }
                        });

                        sleep(timeout);
                    }

                } catch (Exception e) {

                } finally {

                }
            }
        }.start();
    }
}
