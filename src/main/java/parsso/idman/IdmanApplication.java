package parsso.idman;


import lombok.Getter;
import lombok.SneakyThrows;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import parsso.idman.Configs.CasUserDetailService;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Models.User;
import parsso.idman.RepoImpls.UserRepoImpl;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.UserRepo;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The type Idman application.
 */
@SpringBootApplication
@EnableScheduling
public class IdmanApplication extends SpringBootServletInitializer implements CommandLineRunner {


    @Autowired
    static MongoTemplate mongoTemplate;

    @Autowired
    static Pullings pullings;

    @Autowired
    UserRepo userRepo;
    /**
     * The Storage service.
     */
    private static final int millis = 3600000;
    @Resource
    FilesStorageService storageService;
    @Value("${cas.url.logout.path}")
    private String casLogout;
    @Value("${cas.url.validator}")
    private String ticketValidator;
    @Value("${base.url}")
    private String baseurl;
    @Value("${max.pwd.lifetime.hours}")
    private static final long maxPwdLifetime=10;
    @Value("${expire.pwd.message.hours}")
    private static long expirePwdMessageTime;
    @Value("${interval.check.pass.hours}")
    private static long intervalCheckPassTime;



    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {




        String command = "wmic csproduct get UUID";
        StringBuffer output = new StringBuffer();

        Process SerNumProcess = Runtime.getRuntime().exec(command);
        BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

        String line = "";
        while ((line = sNumReader.readLine()) != null) {
            output.append(line + "\n");
        }



        ConfigurableApplicationContext context = SpringApplication.run(IdmanApplication.class, args);

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true){
                    Thread.sleep(intervalCheckPassTime*millis);
                    //pulling(context);
                }
            }
        };


        Thread thread = new Thread(runnable);
        thread.start();


    }

    private static void pulling(ConfigurableApplicationContext context) throws ParseException {

        long deadline = maxPwdLifetime*24*millis;
        long message = expirePwdMessageTime*24*millis;

        List <User> users = context.getBean(UserRepo.class).retrieveUsersFull();

        InstantMessage instantMessage1 = context.getBean(InstantMessage.class); // <-- here

        for (User user : users) {

            Date pwdChangedTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(user.getPasswordChangedTime()));
            if ((deadline/ millis-((new Date().getTime()-pwdChangedTime.getTime())/ millis))<=(message/ millis))
                instantMessage1.sendWarnExpireMessage(user, String.valueOf(deadline/ millis-((new Date().getTime()-pwdChangedTime.getTime())/ millis)));
        }
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

    @Service
    @Getter
    public static class Pullings{
        @Autowired
        UserRepoImpl userRepo;


    }

}





