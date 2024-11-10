package sn.odc.flutter.Services.Interfaces;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String content);
    void sendHtmlEmail(String to, String subject, String htmlContent);
    void sendEmailWithAttachment(String to, String subject, String content, String attachmentPath);
}