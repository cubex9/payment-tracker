package eu.cxn.paymenttracker;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * keep payment and exchange data threadsafe
 */
class PaymentTrackerRepository {

    @FunctionalInterface
    interface CompleteInfo {

        void get( String code, PaymentRecord record, ExchangeRecord exchange );
    }

    private Map<String, PaymentRecord> data;

    private Map<String, ExchangeRecord> exchanges;

    /**
     *
     */
    PaymentTrackerRepository() {

        /* data keep positions */
        data = new LinkedHashMap<>();
        exchanges = new Hashtable<>();
    }

    /**
     *
     *
     * @param r
     */
    synchronized void put(PaymentRecord r) {

        if (data.containsKey(r.getCode())) {
            data.get(r.getCode()).inc(r);
        } else {
            data.put(r.getCode(), r);
        }
    }

    synchronized void put(ExchangeRecord e) {
        exchanges.put(e.getCode(),e);
    }

    /**
     * in time forEach run, other threads can't change infomration in repository
     *
     * @param c
     */
    synchronized void forEach(CompleteInfo c) {
        data.values().forEach( r -> c.get(r.getCode(), r, exchanges.get(r.getCode())));
    }
}
