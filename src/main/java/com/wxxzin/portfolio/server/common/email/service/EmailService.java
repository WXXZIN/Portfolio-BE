package com.wxxzin.portfolio.server.common.email.service;

public interface EmailService {
    void sendMail(String email, String subject, String content);
}
