package eu.cxn.paymenttracker;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentRepository {

    private Map<String,Long> data;

    public PaymentRepository() {
        data = new LinkedHashMap<>();
    }

    public synchronized void record( PaymentRecord r ) {

        if( data.containsKey(r.getCurrency())) {
            data.put( r.getCurrency(), data.get(r.getCurrency()) + r.getAmount());
        } else {
            data.put( r.getCurrency(), r.getAmount());
        }
    }

    public synchronized void print(PrintStream destination ) {

        destination.print("actual: ");

        data.forEach( (k,v) -> {
            if( v != 0 ) {
                destination.println(k + " " + v);
            }
        });
    }
}
