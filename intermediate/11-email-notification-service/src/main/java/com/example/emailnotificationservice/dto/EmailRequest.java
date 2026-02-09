package com.example.emailnotificationservice.dto;

public record EmailRequest(String to, String subject, String body) {
}
