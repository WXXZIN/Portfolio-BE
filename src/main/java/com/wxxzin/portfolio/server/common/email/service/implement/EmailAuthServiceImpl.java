package com.wxxzin.portfolio.server.common.email.service.implement;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.auth.security.jwt.repository.JwtTokenRepository;
import com.wxxzin.portfolio.server.common.email.repository.EmailRepository;
import com.wxxzin.portfolio.server.common.email.service.EmailAuthService;
import com.wxxzin.portfolio.server.common.email.service.EmailContentGenerator;
import com.wxxzin.portfolio.server.common.email.service.EmailService;
import com.wxxzin.portfolio.server.common.exception.CertificationException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;
import com.wxxzin.portfolio.server.domain.user.entity.LocalUserEntity;
import com.wxxzin.portfolio.server.domain.user.enums.Provider;
import com.wxxzin.portfolio.server.domain.user.repository.BaseUserRepository;
import com.wxxzin.portfolio.server.domain.user.repository.LocalUserRepository;

@Service
@Slf4j
public class EmailAuthServiceImpl implements EmailAuthService {

    private static final String CERTIFICATION_SUBJECT = "이메일 인증 안내";
    private static final String TEMPORARY_PASSWORD_SUBJECT = "임시 비밀번호 안내";

    private final EmailService emailService;
    private final EmailRepository emailRepository;
    private final EmailContentGenerator emailContentGenerator;
    private final JwtTokenRepository jwtTokenRepository;
    private final BaseUserRepository baseUserRepository;
    private final LocalUserRepository localUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom random = new SecureRandom();
    
    public EmailAuthServiceImpl(
        EmailService emailService, 
        EmailRepository emailRepository,
        EmailContentGenerator emailContentGenerator,
        JwtTokenRepository jwtTokenRepository,
        BaseUserRepository baseUserRepository,
        LocalUserRepository localUserRepository
    ) {
        this.emailService = emailService;
        this.emailRepository = emailRepository;
        this.emailContentGenerator = emailContentGenerator;
        this.jwtTokenRepository = jwtTokenRepository;
        this.baseUserRepository = baseUserRepository;
        this.localUserRepository = localUserRepository;
    }

    @Override
    public void sendCertificationEmail(String email) {
        if (baseUserRepository.existsByProviderAndEmail(Provider.LOCAL, email)) {
            throw new CertificationException(ErrorMessage.EMAIL_ALREADY_EXISTS);
        }

        String certificationNumber = createCertificationNumber();
        String content = emailContentGenerator.createCertificationEmailContent(certificationNumber);

        emailService.sendMail(email, CERTIFICATION_SUBJECT, content);
        emailRepository.saveCertificationNumber(email, certificationNumber);
    }
    
    @Override
    @Transactional
    public void sendTemporaryPasswordEmail(String username, String email) {
        LocalUserEntity localUserEntity = localUserRepository.findByBaseUserEntity_Email(email)
                .orElseThrow(() -> new CertificationException(ErrorMessage.USER_NOT_FOUND));
        
        if (!localUserEntity.getUsername().equals(username)) {
            throw new CertificationException(ErrorMessage.USERNAME_NOT_MATCH);
        }

        String temporaryPassword = createTemporaryPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(temporaryPassword);
        localUserEntity.updatePassword(encodedPassword);
        localUserRepository.save(localUserEntity);

        jwtTokenRepository.deleteAllTokens(localUserEntity.getBaseUserEntity().getId());

        String content = emailContentGenerator.createTemporaryPasswordEmailContent(temporaryPassword);
        emailService.sendMail(email, TEMPORARY_PASSWORD_SUBJECT, content);
    }


    @Override
    public void checkCertificationNumber(String email, String certificationNumber) {
        if (email == null) {
            throw new CertificationException(ErrorMessage.INVALID_EMAIL);
        }

        if (certificationNumber == null) {
            throw new CertificationException(ErrorMessage.CERTIFICATION_NUMBER_IS_EMPTY);
        }

        if (!emailRepository.hasKey(email)) {
            throw new CertificationException(ErrorMessage.DATA_NOT_FOUND);
        }

        Optional<String> storedCertificationNumberOpt = emailRepository.getCertificationNumber(email);

       if (storedCertificationNumberOpt.isEmpty() || !certificationNumber.equals(storedCertificationNumberOpt.get())) {
           throw new CertificationException(ErrorMessage.INVALID_CERTIFICATION_NUMBER);
       }

       emailRepository.deleteCertificationNumber(email);
    }

    private String createCertificationNumber() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private String createTemporaryPassword() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder temporaryPassword = new StringBuilder(length);
    
        for (int i = 0; i < length; i++) {
            temporaryPassword.append(characters.charAt(random.nextInt(characters.length())));
        }
    
        return temporaryPassword.toString();
    }    
}
