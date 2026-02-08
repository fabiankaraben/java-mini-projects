package com.example.emailsender;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static EmailService emailService;

    public static void main(String[] args) {
        String smtpHost = System.getenv().getOrDefault("SMTP_HOST", "localhost");
        int smtpPort = Integer.parseInt(System.getenv().getOrDefault("SMTP_PORT", "1025"));
        String smtpUser = System.getenv().getOrDefault("SMTP_USER", "");
        String smtpPass = System.getenv().getOrDefault("SMTP_PASS", "");

        emailService = new EmailService(smtpHost, smtpPort, smtpUser, smtpPass);

        Javalin app = Javalin.create().start(7070);

        app.post("/send-email", Main::sendEmailHandler);

        logger.info("Server started on port 7070");
        logger.info("SMTP Config: {}:{}", smtpHost, smtpPort);
    }

    private static void sendEmailHandler(Context ctx) {
        try {
            EmailRequest request = ctx.bodyAsClass(EmailRequest.class);
            
            if (request.getTo() == null || request.getSubject() == null || request.getBody() == null) {
                ctx.status(400).result("Missing required fields: to, subject, body");
                return;
            }

            emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
            ctx.status(200).result("Email sent successfully");
            logger.info("Email sent to {}", request.getTo());

        } catch (Exception e) {
            logger.error("Failed to send email", e);
            ctx.status(500).result("Failed to send email: " + e.getMessage());
        }
    }
}
