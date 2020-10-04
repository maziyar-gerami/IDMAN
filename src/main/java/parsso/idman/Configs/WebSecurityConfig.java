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
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import parsso.idman.mobile.RepoImpls.JwtRequestFilter;

import java.util.Collections;


@Configuration
@Order(99)

@EnableWebSecurity
//@PropertySource("file:D:\\app.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${cas.url.logout.path}")
    private String casLogout;

    @Value("${cas.url.login.path}")
    private String casLogin;

    @Value("${base.selector}")
    private String selector;

    @Value("${spring.ldap.urls}")
    private String ldapUrl;


    @Value("${spring.ldap.base.dn}")
    private String baseDn;


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
                //.authorizeRequests().antMatchers("/**").fullyAuthenticated()

                //.and()


                .authorizeRequests().antMatchers("/dashboard", "/login").authenticated()
                //.antMatchers("")
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                .addFilterBefore(logoutFilter, LogoutFilter.class)
                //////.csrf().ignoringAntMatchers("/exit/cas")
                /*

                .authorizeRequests().antMatchers( "/dashboard", "/login")
                .authenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                */


                .csrf().disable()

                /*.authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/resetPass").permitAll()
                .antMatchers("/resetPassword").permitAll()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/login/cas").permitAll()
                .antMatchers("/api/users/u/{uid}/{pass}/{token}").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()*/

                .authorizeRequests()
                .antMatchers("/api/mobile/qrlogin").permitAll()
                .antMatchers("/api/mobile/login").permitAll()
                .antMatchers("/webSocket").permitAll()
                .antMatchers("/api/mobile/mobnumber").permitAll()


                .and()
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

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (selector.equals("cas")) {
            auth
                    .authenticationProvider(casAuthenticationProvider); // cas;
        } else if (selector.equals("QR")) {
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
        } else if (selector.equals("local")) {
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
                    .passwordAttribute("userPassword");
        }


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
