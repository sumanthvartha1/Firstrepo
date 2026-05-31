package com.bank.payments;

import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentServletTest {
    
    @Test
    public void testProcessPayment() {
        PaymentServlet servlet = new PaymentServlet();
        double result = servlet.processPayment("100.50", "USD");
        assertEquals(100.50, result, 0.01);
    }
    
    @Test
    public void testProcessPaymentInvalid() {
        PaymentServlet servlet = new PaymentServlet();
        double result = servlet.processPayment("invalid", "USD");
        assertEquals(0.0, result, 0.01);
    }
}
