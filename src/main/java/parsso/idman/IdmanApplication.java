package parsso.idman;


import org.apache.commons.io.FileUtils;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import parsso.idman.configs.CasUserDetailService;
import parsso.idman.impls.storage.FilesStorageServiceImpl;
import parsso.idman.repos.FilesStorageService;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * The type Idman application.
 */
@SpringBootApplication

public class IdmanApplication implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(IdmanApplication.class);
    @Autowired
    static MongoTemplate mongoTemplate;
    /**
     * The Storage service.
     */
    @Resource
    FilesStorageService storageService;
    @Value("${cas.url.logout.path}")
    private String casLogout;
    @Value("${cas.url.validator}")
    private String ticketValidator;
    @Value("${base.url}")
    private String baseurl;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {


        SpringApplication.run(IdmanApplication.class, args);


        String command = "wmic csproduct get UUID";
        StringBuffer output = new StringBuffer();

        Process SerNumProcess = Runtime.getRuntime().exec(command);
        BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

        String line = "";
        while ((line = sNumReader.readLine()) != null) {
            output.append(line + "\n");
        }
        String MachineID = output.toString().substring(output.indexOf("\n"), output.length()).trim();


    }


    private static void copyFileUsingApacheCommonsIO(File s, File d) throws IOException {
        FileUtils.copyFile(s, d);
    }

    @Override
    public void run(String... arg) {
        //storageService.deleteAll();
        storageService.init();
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
        CasUserDetailService casUserDetailService = new CasUserDetailService(mongoTemplate);
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





