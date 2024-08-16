package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final ApplicationRepository applicationRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public void send() {
        List<Application> applications = applicationRepository.findByApplicationStatusIn(
                Arrays.asList(ApplicationStatus.APPROVED, ApplicationStatus.REJECTED)
        );
        for (Application application : applications) {
            String mailTemplate = createTemplate(application);
            sendMail(application.getEmail(), mailTemplate);
        }
    }

    private void sendMail(String email, String mailTemplate) {
        try {
            String title = "GDSC KNU 지원 결과 안내";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            messageHelper.setFrom(sender);
            messageHelper.setTo(email);
            messageHelper.setSubject(title);
            messageHelper.setText(mailTemplate, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            // 예외 발생 시 로그 기록
            log.error(email+ " 의 메일 전송에 실패하였습니다. " + e.getMessage());
        }
    }

    private String createTemplate(Application application) {
        Context context = new Context();
        context.setVariable("name", application.getName());
        String templateName = application.getApplicationStatus() == ApplicationStatus.APPROVED ? "pass" : "fail";
        return templateEngine.process(templateName, context);
    }

}
