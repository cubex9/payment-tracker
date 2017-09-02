package eu.cxn.paymenttracker;

import org.apache.commons.cli.*;

import java.io.*;

/**
 * console app
 */
public class PaymentTrackerApp {

    /* print period in miliseconds */
    private static final long PRINT_PERIOD = 60000L;

    /* exchanges base */
    public static final String EXCHANGE_BASE_CODE = "USD";

    /* current status of repository message */
    public static final String PRINTER_MESSAGE = "current status: ";

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

        PaymentTracker pt = new PaymentTracker(System.out, EXCHANGE_BASE_CODE);

        /* enable echo */
        if (arguments.hasOption("e")) {
            pt.enableEcho();
        }

        /* exchage rates */
        if (arguments.hasOption("x")) {
            try {
                pt.exchangesReader(fileInputStream(arguments.getOptionValue("x")));
            } catch (IOException ioe) {
                System.err.println("Can't read exchange rates file: " + ioe.getMessage());
                return;
            }
        }

        /* if input file is set, read */
        if (arguments.hasOption("f")) {
            try {
                pt.reader(fileInputStream(arguments.getOptionValue("f")));
                pt.printCurrentAmounts("payment stats after read: " + arguments.getOptionValue("f"));

            } catch (IOException ioe) {
                System.err.println("Can't read file: " + ioe.getMessage());
                return;
            }
        }

        if( !arguments.hasOption("q")) {

            /* start periodial printer thread */
            pt.printer(PRINTER_MESSAGE,PRINT_PERIOD);

            /* read input stream */
            try {
                pt.reader(System.in);
            } catch( IOException ioe) {
                System.err.println("Error in input stream: " + ioe.getMessage());
            } finally {
                pt.exit();
            }
        }
    }

    /**
     * check, parse and prepare command line arguments
     *
     * @param args
     * @return
     */
    static CommandLine parseArguments(String[] args) {

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
    static InputStream fileInputStream(String fileName) throws IOException {
        return new FileInputStream(new File(fileName));
    }

    /**
     * get input stream from resource file
     *
     * @param resourceName
     * @return
     * @throws IOException
     */
    static FileInputStream resourceInputStream(String resourceName) throws IOException {
        return new FileInputStream(PaymentTrackerApp.class.getResource(resourceName).getFile());
    }
}
