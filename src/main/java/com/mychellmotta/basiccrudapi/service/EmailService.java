package com.mychellmotta.basiccrudapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String EMAIL_FROM = "email_here";
    private static final String EMAIL_TO = "email_here";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String method, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(MimeMessage.RecipientType.TO, EMAIL_TO);
            message.setSubject("Email sent from Mychell's Spring API");

            String htmlContent = "<h1>Called by method: " + method + "</h1>" +
                    "<p><strong>" + text + "</strong></p>";
            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("error sending the email: " + e.getMessage(), e);
        } finally {
            System.out.println("email sent!");
        }
    }
}
