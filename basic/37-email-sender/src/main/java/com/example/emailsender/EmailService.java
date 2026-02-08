package com.example.emailsender;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public EmailService(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // If username/password are provided, enable auth
        if (username != null && !username.isEmpty()) {
            props.put("mail.smtp.auth", "true");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (username != null && !username.isEmpty()) {
                    return new PasswordAuthentication(username, password);
                }
                return null;
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("no-reply@example.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
