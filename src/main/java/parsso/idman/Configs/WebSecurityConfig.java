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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import parsso.idman.mobile.RepoImpls.JwtRequestFilter;

import java.security.Principal;
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


    @Value("${spring.ldap.base.dn}")
    private String baseDn;

    /* @Value("${spring.ldap.base.dn.pattern}")
    private String baseDnPattern; */

    @Value("${spring.ldap.username}")
    private String ldapUsername;

    @Value("${spring.ldap.password}")
    private String ldapPassword;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    private SingleSignOutFilter singleSignOutFilter;
    private LogoutFilter logoutFilter;
    private CasAuthenticationProvider casAuthenticationProvider;
    private ServiceProperties serviceProperties;

    /* @Autowired
    public WebSecurityConfig(SingleSignOutFilter singleSignOutFilter, LogoutFilter logoutFilter,
                             CasAuthenticationProvider casAuthenticationProvider,
                             ServiceProperties serviceProperties) {
        this.logoutFilter = logoutFilter;
        this.singleSignOutFilter = singleSignOutFilter;
        this.serviceProperties = serviceProperties;
        this.casAuthenticationProvider = casAuthenticationProvider;
    } */

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                //.authorizeRequests().antMatchers("/**").fullyAuthenticated()

                //.and()


                .authorizeRequests().antMatchers("/dashboard", "/login").authenticated()
                //.antMatchers("")
                /* .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()) */
                .and()
                //.addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                //.addFilterBefore(logoutFilter, LogoutFilter.class)
                //////.csrf().ignoringAntMatchers("/exit/cas")
                /*

                .authorizeRequests().antMatchers( "/dashboard", "/login")
                .authenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                */

                .csrf().disable()


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
                .antMatchers("/api/mobile/qrCode").permitAll()
                .antMatchers("/api/mobile/sendSms").permitAll()
                .antMatchers("/api/mobile/activate").permitAll()
                .antMatchers("/api/mobile/qrlogin").permitAll()
                .antMatchers("/api/mobile/login").permitAll()
                .antMatchers("/stomp/**").permitAll()
                .antMatchers("/room.2/**").permitAll()
                .antMatchers("/api/mobile/mobnumber").permitAll()



/*
                //******************ADMIN Objects************************
                //pages
                .antMatchers("/configs/**").hasRole("ADMIN")
                .antMatchers("/createservice").hasRole("ADMIN")
                .antMatchers("/dashboard").hasRole("ADMIN")
                .antMatchers("/dashboard").hasRole("ADMIN")
                .antMatchers("/events").hasRole("ADMIN")
                .antMatchers("/groups").hasRole("ADMIN")
                .antMatchers("/privacy").hasRole("ADMIN")
                .antMatchers("/services").hasRole("ADMIN")
                .antMatchers("/users").hasRole("ADMIN")

                //APIs
                .antMatchers("/api/users/**").hasRole("ADMIN")
                .antMatchers("/api/groups").hasRole("ADMIN")
                //.antMatchers("/api/services/**").hasRole("ADMIN")
                .antMatchers("/api/groups/user").hasRole("ADMIN")
                .antMatchers("/api/groups/**").hasRole("ADMIN")




                //******************User Objects************************
                //pages
                .antMatchers("/dashboard").hasAnyRole("USER","ADMIN")
                .antMatchers("/events").hasAnyRole("USER","ADMIN")
                .antMatchers("/services").hasAnyRole("USER","ADMIN")
                .antMatchers("/events").hasAnyRole("USER","ADMIN")

                .antMatchers("/privacy").hasAnyRole("USER","ADMIN")
                //APIs
                .antMatchers("/api/user/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/api/events/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/api/groups/user").hasAnyRole("USER","ADMIN")
                .antMatchers("/api/services/user").hasAnyRole("USER","ADMIN")
                .antMatchers("/api/mobile/profile").hasAnyRole("USER","ADMIN")
                .antMatchers("/api/mobile/services").hasAnyRole("USER","ADMIN")
                .antMatchers("/api/mobile/events").hasAnyRole("USER","ADMIN")
 */


                .anyRequest().authenticated()
               .and().sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                .and()


                .formLogin()
                .loginPage("/login")

                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/dashboard")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .deleteCookies("Authorization");

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=people")
                .contextSource()
                .url(ldapUrl + "/" + baseDn)
                .managerDn(ldapUsername)
                .managerPassword(ldapPassword)
                .and()
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder() {
                })
                .passwordAttribute("mobileToken");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /*
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
    } */


}
