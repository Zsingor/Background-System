package com.example.projectbackend.service;

public interface EmailService {
    void sendEmail(String to,String subject,String text);
}
