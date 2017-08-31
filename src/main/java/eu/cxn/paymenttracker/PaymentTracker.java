package eu.cxn.paymenttracker;


import org.apache.commons.cli.*;

import java.io.InputStream;
import java.util.Scanner;

public class PaymentTracker {

    private CommandLine arguments;

    private PaymentRepository paymentRepository;

    public static void main(String[] args) {

        PaymentTracker pt = new PaymentTracker(args);
        pt.printer(60000L);

        pt.read(System.in);
    }

    public PaymentTracker( String[] args ) {
        arguments = parseArguments(args);

        paymentRepository = new PaymentRepository();
    }

    public void read(InputStream in) {

        /* read */
        Scanner s = new Scanner(in);

        while (s.hasNextLine()) {
            String line = s.nextLine();

            if( arguments.hasOption("e")) {
                System.out.println(line);
            }

            if( line.contains("quit")) {
                break;
            }

            PaymentRecord r = PaymentRecord.parse(line);
            if( r != null ) {
                paymentRepository.record(r);
                System.out.println("OK");
            } else {
                System.out.println( "ERROR");
            }
        }
    }

    public CommandLine parseArguments( String[] args) {

        final Options argumentOptions = new Options()
                .addOption("?", false, "this message")
                .addOption("f", true, "input file name")
                .addOption("e", false, "echo");


        try {
            return (new GnuParser()).parse(argumentOptions, args);

        } catch (ParseException exp) {
            System.out.println(exp.getMessage());

            /* print using message */
            (new HelpFormatter()).printHelp("payment-tracker", argumentOptions);

            throw new IllegalArgumentException();
        }
    }

    public void printer(long timeout) {

        new Thread() {

            @Override
            @SuppressWarnings("CallToThreadRun")
            public void run() {
                try {

                    while(true) {
                        paymentRepository.print(System.out);
                        sleep(timeout);
                    }

                } catch (Exception e) {

                } finally {

                }
            }
        }.start();
    }
}
