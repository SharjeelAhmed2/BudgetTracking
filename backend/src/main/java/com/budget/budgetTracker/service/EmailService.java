package com.budget.budgetTracker.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBudgetExceededAlert(String toEmail, String userName, BigDecimal limit, BigDecimal totalSpent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Budget Alert ⚠️");
            message.setText(
                    "Hello " + userName + ",\n\n" +
                            "Your monthly budget limit of " + limit + " has been exceeded.\n" +
                            "Total spent: " + totalSpent + "\n\n" +
                            "Consider reviewing your expenses in the Budget Tracker app."
            );
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("No email found or failed to send alert.");
        }
    }
}
