package com.wxxzin.portfolio.server.common.email.service.implement;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.springframework.stereotype.Component;

import com.wxxzin.portfolio.server.common.email.service.EmailContentGenerator;

@Component
public class EmailContentGeneratorImpl implements EmailContentGenerator {

    private static final String CERTIFICATION_EMAIL_TEMPLATE = "certification_email_template";
    private static final String TEMPORARY_PASSWORD_EMAIL_TEMPLATE = "temporary_password_email_template";

    private final SpringTemplateEngine templateEngine;

    public EmailContentGeneratorImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String createCertificationEmailContent(String certificationNumber) {
        return createContent(CERTIFICATION_EMAIL_TEMPLATE, "certificationNumber", certificationNumber);
    }

    @Override
    public String createTemporaryPasswordEmailContent(String temporaryPassword) {
        return createContent(TEMPORARY_PASSWORD_EMAIL_TEMPLATE, "temporaryPassword", temporaryPassword);
    }

    private String createContent(String templateName, String variableName, String variableValue) {
        Context context = new Context();
        context.setVariable(variableName, variableValue);

        return templateEngine.process(templateName, context);
    }
}
