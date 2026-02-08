package com.example.emailsender;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Test
    public void testSendEmail() throws Exception {
        EmailService emailService = new EmailService("localhost", 3025, null, null);
        
        emailService.sendEmail("test@example.com", "Test Subject", "Test Body");

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Test Subject", receivedMessages[0].getSubject());
        assertEquals("Test Body", receivedMessages[0].getContent().toString().trim());
        assertEquals("test@example.com", receivedMessages[0].getRecipients(MimeMessage.RecipientType.TO)[0].toString());
    }
}
