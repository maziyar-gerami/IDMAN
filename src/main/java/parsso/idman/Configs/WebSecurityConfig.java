package parsso.idman.Configs;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Value("${cas.url.logout.path}")
    private String casLogout;

    @Value("${cas.url.login.path}")
    private String casLogin;

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

                .authorizeRequests().antMatchers( "/dashboard", "/login").authenticated()
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
                /*
                .authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/resetPass").permitAll()
                .antMatchers("/resetPassword").permitAll()
                .antMatchers("/api/users/validateToken/**").permitAll()
                .antMatchers("/api/users/sendMail/**").permitAll()
                .antMatchers("/api/users/checkMail/**").permitAll()
                .antMatchers("/api/users/u/{id}/{token}").permitAll()
                .antMatchers("/login/cas").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/users/u/{uid}/{pass}/{token}").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                */
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
                .authenticationProvider(casAuthenticationProvider); // cas;
                /*.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=people")
                .contextSource()
                .url("ldap://localhost:10389/dc=example,dc=com")

                .and()
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder() {
                })
                .passwordAttribute("userPassword");*/
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
