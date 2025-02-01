package com.wxxzin.portfolio.server.common.email.service;

public interface EmailContentGenerator {
    String createCertificationEmailContent(String certificationNumber);
    String createTemporaryPasswordEmailContent(String temporaryPassword);
}
