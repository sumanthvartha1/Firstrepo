package com.bank.payments;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        
        String amount = req.getParameter("amount");
        String currency = req.getParameter("currency");
        
        out.println("<html><body>");
        out.println("<h1>Payment Service API</h1>");
        out.println("<p>Amount: " + amount + "</p>");
        out.println("<p>Currency: " + currency + "</p>");
        out.println("<p>Status: PROCESSING</p>");
        out.println("<p>Version: 1.0.0</p>");
        out.println("</body></html>");
    }
    
    public double processPayment(String amount, String currency) {
        // Simple business logic for SonarQube to scan
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
