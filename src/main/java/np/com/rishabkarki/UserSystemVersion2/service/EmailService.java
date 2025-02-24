package np.com.rishabkarki.UserSystemVersion2.service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Value("${smtp.sender.address}")
    private String smtpSender;

    @Value("${smtp.user.name}")
    private String smtpUser;

    @Value("${smtp.user.password}")
    private String smtpPassword;

    @Value("${smtp.host}")
    private String smtpHost;

    @Value("${smtp.port}")
    private String smtpPort;

    public void sendVerificationEmail(String mailReceiver, Integer token) {
        System.out.println(smtpUser);
        System.out.println(smtpPassword);
        System.out.println(smtpHost);
        System.out.println(smtpPort);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", smtpHost);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailReceiver));
            message.setSubject("Email Verification");
            message.setContent("Your verification code is: " + token, "text/html");

            Transport.send(message);
            System.out.println("Verification email sent successfully to " + mailReceiver);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}