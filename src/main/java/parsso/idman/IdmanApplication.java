package parsso.idman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import parsso.idman.Email.EmailSend;

@SpringBootApplication
public class IdmanApplication {

	private JavaMailSender javaMailSender;

	public static void main(String[] args) {
		SpringApplication.run(IdmanApplication.class, args);

	}
}