package eu.cxn.paymenttracker;

public class AbstractPaymentTrackerTest {

    /* append your special currency codes */
    static {
        System.setProperty("java.util.currency.data","currency.properties");
    }
}
