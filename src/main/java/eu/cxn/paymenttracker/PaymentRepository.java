package eu.cxn.paymenttracker;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * keep data map synchronized
 */
public class PaymentRepository {

    private Map<String, PaymentRecord> data;

    public PaymentRepository() {
        data = new LinkedHashMap<>();
    }

    public synchronized void put(PaymentRecord r) {

        if (data.containsKey(r.getCurrency())) {
            data.get(r.getCurrency()).add(r.getAmount());
        } else {
            data.put(r.getCurrency(), r);
        }
    }

    public synchronized void forEach(Consumer<PaymentRecord> c) {
        data.values().forEach(c);
    }
}
