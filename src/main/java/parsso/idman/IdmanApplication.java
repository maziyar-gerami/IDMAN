package parsso.idman;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import parsso.idman.Email.EmailSend;

@SpringBootApplication

public class IdmanApplication {


	private JavaMailSender javaMailSender;

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter(
			AuthenticationManager authenticationManager,
			ServiceProperties serviceProperties) throws Exception {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager);
		filter.setServiceProperties(serviceProperties);
		return filter;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		//logger.info("service properties");
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService("http://localhost:8080/login");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	public TicketValidator ticketValidator() {
		return new Cas30ServiceTicketValidator("https://parsso.razi.ac.ir");
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider(
			TicketValidator ticketValidator,
			ServiceProperties serviceProperties) {
		CasAuthenticationProvider provider = new CasAuthenticationProvider();
		provider.setServiceProperties(serviceProperties);
		provider.setTicketValidator(ticketValidator);
		provider.setUserDetailsService(
				s -> new User("test@test.com", "Mellon", true, true, true, true,
						AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
		provider.setKey("CAS_PROVIDER_LOCALHOST_8900");
		return provider;
	}


	public static void main(String[] args) {
		SpringApplication.run(IdmanApplication.class, args);

	}





}
