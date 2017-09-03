package eu.cxn.paymenttracker;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * keep payment and exchange data threadsafe
 */
class PaymentTrackerRepository {

    private Map<String, PaymentRecord> data = new TreeMap<>();

    /**
     *
     */
    PaymentTrackerRepository() {
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

    /**
     * set exchange rate to payment record, if not exist, create him with 0 amount
     *
     * @param e
     */
    synchronized void put(ExchangeRateRecord e) {

        /** in this time, when we have 1:1 payment records vs exchange record, is cheaper
         * put exchange into payment record, if in future need more payment records,
         * example by user/company atp.., better keep exchange in separate table and
         * pair with payment record in output actions
         *
         */
        if( !data.containsKey(e.getCode())) {
            data.put( e.getCode(), new PaymentRecord(e.getCode(),0L));
        }
        data.get(e.getCode()).setExchange(e);
    }

    /**
     * in time forEach run, other threads can't change infomration in repository
     *
     * @param c
     */
    synchronized void forEach(Consumer<PaymentRecord> c) {
        data.values().forEach(c);
    }
}
