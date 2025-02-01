package com.wxxzin.portfolio.server.common.email.service;

public interface EmailAuthService {
    void sendCertificationEmail(String email);
    void sendTemporaryPasswordEmail(String username, String email);
    void checkCertificationNumber(String email, String certificationNumber);
}
