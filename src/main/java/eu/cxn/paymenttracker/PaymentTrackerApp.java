package eu.cxn.paymenttracker;

import org.apache.commons.cli.*;

import java.io.*;

/**
 * console app
 * */
public class PaymentTrackerApp {

    public static void main(String[] args) {
        CommandLine arguments = parseArguments(args);

        PaymentTracker pt = new PaymentTracker(System.out);

        /* print input data into output */
        if( arguments.hasOption("e")) {
            pt.enableEcho();
        }

        /* if input file is set, read */
        if (arguments.hasOption("f")) {
            try {
                pt.reader(
                        new FileInputStream(
                                new File((String) arguments.getParsedOptionValue("f")))
                );
            } catch (ParseException pe) {
                System.err.println("Incorrect input file argument: " + pe.getMessage());
            } catch (IOException ioe) {
                System.err.println("Can't read file: " + ioe.getMessage());
            }
        }

        pt.printer(60000L);
        pt.reader(System.in);
    }

    /**
     * check, parse and prepare command line arguments
     *
     * @param args
     * @return
     */
    public static CommandLine parseArguments(String[] args) {

        final Options argumentOptions = new Options()
                .addOption("?", false, "this message")
                .addOption("f", true, "input file name")
                .addOption("e", false, "print input data to output");


        try {
            return (new GnuParser()).parse(argumentOptions, args);

        } catch (ParseException exp) {
            System.err.println(exp.getMessage());

            /* print using message */
            (new HelpFormatter()).printHelp("payment-tracker", argumentOptions);

            throw new IllegalArgumentException();
        }
    }
}
