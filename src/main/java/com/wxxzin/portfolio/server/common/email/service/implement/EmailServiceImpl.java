package com.wxxzin.portfolio.server.common.email.service.implement;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.wxxzin.portfolio.server.common.email.service.EmailService;
import com.wxxzin.portfolio.server.common.exception.EmailSendException;
import com.wxxzin.portfolio.server.common.response.error.ErrorMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    @Retryable(
            value = { MailException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000)
    )
    public void sendMail(String email, String subject, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);
        } catch (AddressException e) {
            throw new EmailSendException(ErrorMessage.INVALID_EMAIL);
        } catch (Exception e) {
            throw new EmailSendException(ErrorMessage.UNKNOWN_ERROR);
        }
    }
}
