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

    @Value("${cas.url.logout.path}")
    private String casLogout;

    @Value("${cas.url.login.path}")
    private String casLogin;

    @Value("${spring.ldap.urls}")
    private String ldapUrl;

    private final SingleSignOutFilter singleSignOutFilter;
    private final LogoutFilter logoutFilter;
    private final CasAuthenticationProvider casAuthenticationProvider;
    private final ServiceProperties serviceProperties;

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
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                .csrf().disable()



///*

                .authorizeRequests()
                //****************Public Objects*********************
                //resources
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/images/**").permitAll()
                //pages
                .antMatchers("/resetPass").permitAll()
                .antMatchers("/resetPassword").permitAll()
                .antMatchers("/login/cas").permitAll()
                .antMatchers("/403").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/privacy").permitAll()


                //APIs
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/resetPass**").permitAll()

                .antMatchers("/api/captcha/request").permitAll()
                //.antMatchers("/api/mobile/qrCode").anonymous()
                //.antMatchers("/api/mobile/sendsms").anonymous()
                //.antMatchers("/api/mobile/active").anonymous()
                .antMatchers("/api/resetPass/**").permitAll()

                //******************SUPERADMIN Objects************************
                //pages
                .antMatchers("/configs/**").hasRole("SUPERADMIN")

                //APIs
                .antMatchers("/api/configs/*").hasRole("SUPERADMIN")
                
                //******************ADMIN Objects************************
                //pages
                .antMatchers("/createservice").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/dashboard").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/events").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/groups").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/privacy").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/services").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/users").hasAnyRole("ADMIN","SUPERADMIN")

                //APIs
                .antMatchers("/api/users/**").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/api/groups").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/api/services").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/api/services").hasAnyRole("ADMIN","SUPERADMIN")

                .antMatchers("/api/groups/user").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/api/groups/**").hasAnyRole("ADMIN","SUPERADMIN")


                //******************User Objects************************
                //pages
                .antMatchers("/dashboard").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/events").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/services").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/events").hasAnyRole("USER", "ADMIN", "SUPERADMIN")

                .antMatchers("/privacy").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                //APIs
                .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/api/events/**").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/api/groups/user").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/api/services/user").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                //.antMatchers("/api/mobile/profile").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                //.antMatchers("/api/mobile/services").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                //.antMatchers("/api/mobile/events").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .antMatchers("/api/dashboard").hasAnyRole("ADMIN", "SUPERADMIN")

                //Excepts
                .antMatchers("/api/mobile/**").permitAll()


                //
                .anyRequest().authenticated()
                .and()

//*/

                .formLogin()
                .loginPage("/login")

                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
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
