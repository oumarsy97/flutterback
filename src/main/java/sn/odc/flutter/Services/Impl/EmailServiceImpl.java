package sn.odc.flutter.Services.Impl;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.odc.flutter.Services.Interfaces.EmailService;

import java.io.File;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    private final String fromEmail;
    private final String emailPassword;
    private final String smtpHost;
    private final int smtpPort;
    private final Session session;

    public EmailServiceImpl(
            @Value("${spring.mail.username}") String fromEmail,
            @Value("${spring.mail.password}") String emailPassword,
            @Value("${spring.mail.host}") String smtpHost,
            @Value("${spring.mail.port}") int smtpPort) {

        this.fromEmail = fromEmail;
        this.emailPassword = emailPassword;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.trust", "*");

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Création du contenu texte
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(content, "UTF-8");

            // Assemblage du message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Création du contenu HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            // Assemblage du message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email HTML", e);
        }
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String content, String attachmentPath) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Création du contenu texte
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(content, "UTF-8");

            // Création de la pièce jointe
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(new File(attachmentPath));
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(source.getName());

            // Assemblage du message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email avec pièce jointe", e);
        }
    }
}