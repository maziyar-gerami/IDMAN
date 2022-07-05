package parsso.idman;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import parsso.idman.configs.CasUserDetailService;
import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.SettingsRepo;

@SuppressWarnings("unchecked")
@SpringBootApplication
@EnableScheduling
// @EnableCaching
public class IdmanApplication extends SpringBootServletInitializer implements CommandLineRunner {
  private static final Logger logger = LogManager.getLogger("System");
  @Autowired
  FilesStorageService storageService;
  @Autowired
  CasUserDetailService casUserDetailService;
  @Autowired
  SettingsRepo settingsRepo;

  @Value("${cas.url.logout.path}")
  private String casLogout;
  @Value("${cas.url.validator}")
  private String ticketValidator;
  @Value("${base.url}")
  private String BASE_URL;
  @Value("${spring.ldap.base.dn}")
  private String BASE_DN;

  public IdmanApplication() {
  }

  public static void main(String[] args) {

    try {
      SpringApplication.run(IdmanApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.warn("Started!");

  }

  public void doSomethingAfterStartup() {
     new Prefs(Variables.PREFS_BASE_URL, BASE_URL);
     new Prefs(Variables.PREFS_BASE_DN, BASE_DN);
     new Prefs(settingsRepo.retrieve());
   }

  @Override
  public void run(String... arg) throws IOException {
    storageService.init();
  }

  @Bean
  public CasAuthenticationFilter casAuthenticationFilter(
      AuthenticationManager authenticationManager,
      ServiceProperties serviceProperties) {
    CasAuthenticationFilter filter = new CasAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setServiceProperties(serviceProperties);
    return filter;
  }

  @Bean
  public ServiceProperties serviceProperties() {
    ServiceProperties serviceProperties = new ServiceProperties();
    serviceProperties.setService(new StringBuilder(BASE_URL).append("/login/cas").toString());
    serviceProperties.setSendRenew(false);
    return serviceProperties;
  }

  @Bean
  public TicketValidator ticketValidator() {
    return new Cas30ServiceTicketValidator(ticketValidator);
  }

  @Bean
  public CasAuthenticationProvider casAuthenticationProvider() {
    CasAuthenticationProvider provider = new CasAuthenticationProvider();
    provider.setServiceProperties(serviceProperties());
    provider.setTicketValidator(ticketValidator());

    provider.setAuthenticationUserDetailsService(casUserDetailService);
    provider.setKey("CAS_PROVIDER_LOCALHOST_8900");
    return provider;
  }

  @Bean
  public SecurityContextLogoutHandler securityContextLogoutHandler() {
    return new SecurityContextLogoutHandler();
  }

  @Bean
  public LogoutFilter logoutFilter() {
    LogoutFilter logoutFilter = new LogoutFilter(casLogout, securityContextLogoutHandler());
    logoutFilter.setFilterProcessesUrl("/logout/cas");
    return logoutFilter;
  }

  @Bean
  public SingleSignOutFilter singleSignOutFilter() {
    SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
    // singleSignOutFilter.setCasServerUrlPrefix(casLogout);
    singleSignOutFilter.setLogoutCallbackPath("/exit/cas");
    singleSignOutFilter.setIgnoreInitConfiguration(true);
    return singleSignOutFilter;
  }

}
