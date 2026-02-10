package com.example.payment;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
             // Handle the event
             // In a real application, you would deserialize the event data object
             // e.g. PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
             System.out.println("Payment succeeded!");
        }

        return ResponseEntity.ok("Received");
    }
}
