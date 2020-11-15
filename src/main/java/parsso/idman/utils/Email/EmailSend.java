package parsso.idman.utils.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailSend {

    // Recipient's email ID needs to be mentioned.

    // Sender's email ID needs to be mentioned
    @Value("${address.email.sender}")
    String from;
    @Value("${password.email.sender}")
    String password;

    // Assuming you are sending email from through gmails
    @Value("${host.email.sender}")
    String host;

    public void sendMail(String to, String uid, String name, String token) {

        String subject = "بازنشانی رمز عبور";
        String body = " عزیز \nشما این پیام را مبنی بر بازنشانی رمز عبور برای نام کاربری زیر دریافت نموده اید.\n" +
                "در صورتی که این درخواست از طرف شما انجام نشده است، از این پیام صرف نظر کنید.\n" +
                "در غیر این صورت با کلیک بر روی  لینک زیر نسبت به بازنشانی و تغییر رمز عبور خود اقدام نمایید.\n";
        String end = "\n\nاگر این لینک برای شما بصورت یک لینک قابل کلیک نشان داده نشد، آن را عینا در مرورگر خود کپی کنید";

        String stringUid = "نام کاربری: ";
        String stringLink = "لینک بازنشانی رمز عبور: ";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(name + body + stringUid + uid + "\n" + stringLink + token + end, "UTF8");

            // Send message
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}