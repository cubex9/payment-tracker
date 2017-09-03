package eu.cxn.paymenttracker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaymentTrackerRepositoryTest extends AbstractPaymentTrackerTest {

    @Test
    public void paymentSynchronization() {

        /*
         * if remove 'synchonize' from .put(PaymentRecord) method and result != 0
         */
        PaymentTrackerRepository rep = new PaymentTrackerRepository();

        Thread a = new Thread() {

            @Override
            public void run() {

                for( int i = 0; i < 100000; i++ ) {
                    rep.put(new PaymentRecord("USD", 1L));
                    rep.put(new PaymentRecord("CZK", -1L));
                }

                interrupt();
            }
        };

        Thread b = new Thread() {

            @Override
            public void run() {

                for( int i = 0; i < 100000; i++ ) {
                    rep.put( new PaymentRecord("USD", -1L));
                    rep.put(new PaymentRecord("CZK", 1L));
                }

                interrupt();
            }
        };

        a.start();
        b.start();

        while( a.isAlive() || b.isAlive()) {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException ioe) {
                break;
            }
        }

        rep.forEach(r -> assertEquals(0L, r.getAmount()));
    }

}