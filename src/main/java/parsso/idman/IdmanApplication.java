package parsso.idman;

import org.apache.commons.io.FileUtils;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import parsso.idman.Configs.CasUserDetailService;
import parsso.idman.Repos.FilesStorageService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * The type Idman application.
 */
@SpringBootApplication

public class IdmanApplication implements CommandLineRunner {

    @Value("${cas.url.logout.path}")
    private String casLogout;


    @Value("${cas.url.validator}")
    private String ticketValidator;

    @Value("${base.url}")
    private String baseurl;




    private static final Logger logger = LoggerFactory.getLogger(IdmanApplication.class);

    /**
     * The Storage service.
     */
    @Resource
    FilesStorageService storageService;


    @Override
    public void run(String... arg) {
        //storageService.deleteAll();
        storageService.init();
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException {



        SpringApplication.run(IdmanApplication.class, args);

    }

    private static void copyFileUsingApacheCommonsIO(File s, File d) throws IOException {
        FileUtils.copyFile(s, d);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws IOException {

    }

    /**
     * Cas authentication filter cas authentication filter.
     *
     * @param authenticationManager the authentication manager
     * @param serviceProperties     the service properties
     * @return the cas authentication filter
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ServiceProperties serviceProperties) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setServiceProperties(serviceProperties);
        return filter;
    }

    /**
     * Service properties service properties.
     *
     * @return the service properties
     */
    @Bean
    public ServiceProperties serviceProperties() {
        logger.info("service properties");
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(baseurl + "/login/cas");
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    /**
     * Ticket validator ticket validator.
     *
     * @return the ticket validator
     */
    @Bean
    public TicketValidator ticketValidator() {
        return new Cas30ServiceTicketValidator(ticketValidator);
    }

    /**
     * Cas authentication provider cas authentication provider.
     *
     * @return the cas authentication provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());
        provider.setTicketValidator(ticketValidator());
        /*provider.setUserDetailsService(
                s -> new User("test@test.com", "Mellon", true, true, true, true,
                        AuthorityUtils.createAuthorityList("ADMIN")));*/
        CasUserDetailService casUserDetailService = new CasUserDetailService();
        provider.setAuthenticationUserDetailsService(casUserDetailService);
        provider.setKey("CAS_PROVIDER_LOCALHOST_8900");
        return provider;
    }


    /**
     * Security context logout handler security context logout handler.
     *
     * @return the security context logout handler
     */
    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    /**
     * Logout filter logout filter.
     *
     * @return the logout filter
     */
    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casLogout, securityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout/cas");
        return logoutFilter;
    }

    /**
     * Single sign out filter single sign out filter.
     *
     * @return the single sign out filter
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        //singleSignOutFilter.setCasServerUrlPrefix(casLogout);
        singleSignOutFilter.setLogoutCallbackPath("/exit/cas");
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

}





