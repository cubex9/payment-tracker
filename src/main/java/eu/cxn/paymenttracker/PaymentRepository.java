package eu.cxn.paymenttracker;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * keep put and forEach action threadsafe
 */
public class PaymentRepository {

    private Map<String, PaymentRecord> data;

    /**
     *
     */
    public PaymentRepository() {
        data = new LinkedHashMap<>();
    }

    /**
     *
     * @param r
     */
    public synchronized void put(PaymentRecord r) {

        if (data.containsKey(r.getCurrency())) {
            data.get(r.getCurrency()).add(r.getAmount());
        } else {
            data.put(r.getCurrency(), r);
        }
    }

    /**
     *
     * @param c
     */
    public synchronized void forEach(Consumer<PaymentRecord> c) {
        data.values().forEach(c);
    }
}
