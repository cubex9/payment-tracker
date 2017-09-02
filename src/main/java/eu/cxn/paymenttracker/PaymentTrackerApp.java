package eu.cxn.paymenttracker;

import org.apache.commons.cli.*;

import java.io.*;

/**
 * console app
 */
public class PaymentTrackerApp {

    /* print period in miliseconds */
    private static final long PRINT_PRERIOD = 60000L;

    /* exchanges base */
    public static final String EXCHANGE_BASE_CODE = "USD";

    /* command line arguments definition */
    private static final Options argumentOptions = new Options()
            .addOption("?", false, "this message")
            .addOption("f", true, "input file name")
            .addOption("q", false, "quit after read file")
            .addOption("e", false, "print input data to output")
            .addOption("x", true, "exchange rates file");

    /**
     * console application
     *
     * @param args
     */
    public static void main(String[] args) {
        CommandLine arguments = parseArguments(args);

        /* help message and quit */
        if( arguments == null || arguments.hasOption("?")) {
            (new HelpFormatter()).printHelp("payment-tracker", argumentOptions);
            return;
        }

        PaymentTracker pt = new PaymentTracker(System.out);

        /* enable echo */
        if (arguments.hasOption("e")) {
            pt.enableEcho();
        }

        /* exchage rates */
        if (arguments.hasOption("x")) {
            try {
                pt.exchangesReader(EXCHANGE_BASE_CODE,fileInputStream(arguments.getOptionValue("x")));
            } catch (IOException ioe) {
                System.err.println("Can't read exchange rates file: " + ioe.getMessage());
            }
        }

        /* if input file is set, read */
        if (arguments.hasOption("f")) {
            try {
                pt.reader(fileInputStream(arguments.getOptionValue("f")));
            } catch (IOException ioe) {
                System.err.println("Can't read file: " + ioe.getMessage());
            }
        }

        if( arguments.hasOption("q")) {
            pt.printCurrentAmounts();
        } else {
            pt.printer(PRINT_PRERIOD);
            pt.reader(System.in);
            pt.exit();
        }
    }

    /**
     * check, parse and prepare command line arguments
     *
     * @param args
     * @return
     */
    public static CommandLine parseArguments(String[] args) {

        try {
            return (new GnuParser()).parse(argumentOptions, args);

        } catch (ParseException exp) {
            System.err.println(exp.getMessage());
            return null;
        }
    }

    /**
     * get InputStream from file
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static InputStream fileInputStream(String fileName) throws IOException {
        return new FileInputStream(new File(fileName));
    }

    /**
     * get input stream from resource file
     *
     * @param resourceName
     * @return
     * @throws IOException
     */
    public static FileInputStream resourceInputStream(String resourceName) throws IOException {
        return new FileInputStream(PaymentTrackerApp.class.getResource(resourceName).getFile());
    }
}
