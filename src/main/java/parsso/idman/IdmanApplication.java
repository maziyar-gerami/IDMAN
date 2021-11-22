package parsso.idman;


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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import parsso.idman.configs.CasUserDetailService;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.models.users.User;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.UserRepo;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
@SpringBootApplication
@EnableScheduling
public class IdmanApplication extends SpringBootServletInitializer implements CommandLineRunner {
    private static final int millis = 3600000;
    @Value("${max.pwd.lifetime.hours}")
    private static final long maxPwdLifetime = 10;
    private static final Logger logger = LogManager.getLogger("System");
    @Value("${expire.pwd.message.hours}")
    private static long expirePwdMessageTime;
    @Value("${interval.check.pass.hours}")
    private static long intervalCheckPassTime;
    @Value("${mongo.uri}")
    private static String mongoUri;
    @Resource
    FilesStorageService storageService;
    @Autowired
    CasUserDetailService casUserDetailService;
    @Value("${cas.url.logout.path}")
    private String casLogout;
    @Value("${cas.url.validator}")
    private String ticketValidator;
    @Value("${base.url}")
    private String baseurl;

    public static void main(String[] args) {

        SpringApplication.run(IdmanApplication.class, args);
        logger.warn("Started!");
    }

    @SuppressWarnings("unused")
    private static void pulling(ConfigurableApplicationContext context) throws ParseException {

        long deadline = maxPwdLifetime * 24 * millis;
        long message = expirePwdMessageTime * 24 * millis;

        List<User> users = context.getBean(UserRepo.class).retrieveUsersFull();

        InstantMessage instantMessage1 = context.getBean(InstantMessage.class); // <-- here

        for (User user : users) {

            Date pwdChangedTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(user.getPasswordChangedTime()));
            if ((deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)) <= (message / millis))
                instantMessage1.sendWarnExpireMessage(user, String.valueOf(deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)));
        }
    }

    @Override
    public void run(String... arg) {
        //storageService.deleteAll();
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
        serviceProperties.setService(baseurl + "/login/cas");
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
        //singleSignOutFilter.setCasServerUrlPrefix(casLogout);
        singleSignOutFilter.setLogoutCallbackPath("/exit/cas");
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

}





