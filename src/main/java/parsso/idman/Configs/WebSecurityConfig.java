package parsso.idman.Configs;


import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Collections;


@Configuration
@Order(99)

@EnableWebSecurity
@PropertySource(value = "file:${external.config}", ignoreResourceNotFound = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SingleSignOutFilter singleSignOutFilter;
    private final LogoutFilter logoutFilter;
    private final CasAuthenticationProvider casAuthenticationProvider;
    private final ServiceProperties serviceProperties;
    @Value("${cas.url.logout.path}")
    private String casLogout;
    @Value("${cas.url.login.path}")
    private String casLogin;
    @Value("${spring.ldap.urls}")
    private String ldapUrl;


    @Autowired
    public WebSecurityConfig(SingleSignOutFilter singleSignOutFilter, LogoutFilter logoutFilter,
                             CasAuthenticationProvider casAuthenticationProvider,
                             ServiceProperties serviceProperties) {
        this.logoutFilter = logoutFilter;
        this.singleSignOutFilter = singleSignOutFilter;
        this.serviceProperties = serviceProperties;
        this.casAuthenticationProvider = casAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http

                .authorizeRequests().antMatchers("/dashboard", "/login").authenticated()
                //.antMatchers("")
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                .addFilterBefore(logoutFilter, LogoutFilter.class)


                .authorizeRequests().antMatchers("/dashboard", "/login")
                .authenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()


/*


                .authorizeRequests()
                //****************Public Objects*********************
                //resources
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/public/Parsso-User-Guide.pdf").permitAll()

                //pages
                .antMatchers("/resetpassword").permitAll()
                .antMatchers("/newpassword**").permitAll()
                .antMatchers("/login/cas").permitAll()
                .antMatchers("/403").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/privacy").permitAll()

                //APIs
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/resetpassword**").permitAll()
                .antMatchers("/api/captcha/request").permitAll()
                .antMatchers("/api/mobile/**").permitAll()


                //************* Any Authenticated Users Objects **********
                //Pages
                .antMatchers("/dashboard").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/events").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/audits").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/profile").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/privacy").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/reports").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/ticketing").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")


                //APIs
                .antMatchers("/api/groups/user").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/events/user/**").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/audits/user/**").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/reports/user/**").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/services/user").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/user/**").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER", "SUPERADMIN")


                //****************** SUPERADMIN & ADMIN & SUPPORTER Objects************************
                //pages
                .antMatchers("/services").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/transcripts").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/createservice").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/users").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/groups").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/publicmessages").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")


                //APIs
                .antMatchers("/api/users/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/groups").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/services").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/groups/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/dashboard").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/reports/users/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/audits/users/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")
                .antMatchers("/api/events/users/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERADMIN")


                //******************SUPERADMIN Objects ONLY *******************
                //pages
                .antMatchers("/roles").hasRole("SUPERADMIN")
                .antMatchers("/configs**").hasRole("SUPERADMIN")

                //APIs
                .antMatchers("/api/configs/**").hasRole("SUPERADMIN")
                .antMatchers("/api/configs").hasRole("SUPERADMIN")
                .antMatchers("/api/roles**").hasRole("SUPERADMIN")
                .antMatchers("/api/roles/**").hasRole("SUPERADMIN")
                .antMatchers("/api/refresh**").hasRole("SUPERADMIN")
                .antMatchers("/api/refresh/**").hasRole("SUPERADMIN")
                .antMatchers("/api/superadmin/**").hasRole("SUPERADMIN")

                //******************SUPPORTER and SUPERADMIN Objects *******************
                //pages

                //APIs
                .antMatchers("/api/tickets").hasAnyRole("SUPPORTER", "SUPERADMIN")


                //******************SUPPORTER and SUPERADMIN and ADMIN Objects *******************


                //APIs
                .antMatchers("/api/supporter/**").hasAnyRole("ADMIN", "SUPERADMIN", "SUPPORTER")


                .anyRequest().authenticated()
                .and()


*/


                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                .csrf().disable()

                .formLogin()
                .loginPage("/login")

                .defaultSuccessUrl("/dashboard", true)
                .permitAll()

                .and()
                .exceptionHandling().accessDeniedPage("/403")

                .and()
                .logout()
                .logoutUrl(casLogout)
                .logoutSuccessUrl("/dashboard")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(casAuthenticationProvider);


    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(casAuthenticationProvider));
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(casLogin);
        entryPoint.setServiceProperties(serviceProperties);

        return entryPoint;
    }
}
