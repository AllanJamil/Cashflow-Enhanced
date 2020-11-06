package com.project.cashflow.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {

    //TODO Should be dynamic fix email service
    private SendGrid sg = new SendGrid(System.getenv("MAIL_KEY"));

    private void sendMail(Mail mail) {

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.WARNING, ex.getMessage());
        }
    }

    public void sendEmailConfirmation(String email, String token) {
        Mail mail = new Mail();
        mail.setFrom(new Email("cashflow.reg@gmail.com"));
        mail.setTemplateId("d-2376f7b4c8fb4420b237af1c2078db94");

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("VERIFY_URL", "url" + token);
        personalization.addTo(new Email(email));
        mail.addPersonalization(personalization);
        sendMail(mail);
    }

    public void sendEmailPasswordChange(String email) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mail = new Mail();
        mail.setFrom(new Email("cashflow.reg@gmail.com"));
        mail.setTemplateId("d-70d71875f7ff47398802e41d78243d00");

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("TIME", LocalDateTime.now().format(formatter));
        personalization.addTo(new Email(email));
        mail.addPersonalization(personalization);
        sendMail(mail);
    }

    public void sendRecoveryEmail(String email, String recoveryToken) {
        Mail recoveryMail = new Mail();
        recoveryMail.setFrom(new Email("cashflow.reg@gmail.com"));
        recoveryMail.setTemplateId("TEMPLATE_ID");
        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("RESET_URL", "url" + recoveryToken);
        personalization.addTo(new Email(email));
        recoveryMail.addPersonalization(personalization);
        sendMail(recoveryMail);
    }
}
