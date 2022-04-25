package parsso.idman.configs;

import java.util.Collections;
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

@Configuration
@Order(99)

@EnableWebSecurity
@PropertySource(value = "file:${external.config}", ignoreResourceNotFound = true)
@PropertySource(value = "file:${external.cas}", ignoreResourceNotFound = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private final SingleSignOutFilter singleSignOutFilter;
  private final LogoutFilter logoutFilter;
  private final CasAuthenticationProvider casAuthenticationProvider;
  private final ServiceProperties serviceProperties;
  @Value("${cas.url.logout.path}")
  private String casLogout;
  @Value("${cas.url.login.path}")
  private String casLogin;

  /**
   * CAS configuration.
   * @throws all Exception
   */
  @Autowired
  public WebSecurityConfig(SingleSignOutFilter singleSignOutFilter, LogoutFilter logoutFilter,
      CasAuthenticationProvider casAuthenticationProvider,
      ServiceProperties serviceProperties) {
    this.logoutFilter = logoutFilter;
    this.singleSignOutFilter = singleSignOutFilter;
    this.serviceProperties = serviceProperties;
    this.casAuthenticationProvider = casAuthenticationProvider;
  }


  
  /**
   * CAS configuration.
   * @param http security
   * @throws all Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http

        .authorizeRequests().antMatchers("/dashboard", "/login").authenticated()
        // .antMatchers("")
        .and()
        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
        .and()
        .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
        .addFilterBefore(logoutFilter, LogoutFilter.class)

        .authorizeRequests().antMatchers("/dashboard", "/login")
        .authenticated()
        .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
        .and()

        
        
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
          .antMatchers("/changepassword").permitAll()
          .antMatchers("/newpassword**").permitAll()
          .antMatchers("/login/cas").permitAll()
          .antMatchers("/403").permitAll()
          .antMatchers("/error").permitAll()
          .antMatchers("/privacy").permitAll()
          .antMatchers("/resetpassword").permitAll()
          .antMatchers("/chagepassword").permitAll()
          .antMatchers("/#/resetpassword").permitAll()
          .antMatchers("/#/chagepassword").permitAll()
          
          //APIs
          .antMatchers("/api/public/**").permitAll()
          .antMatchers("/api/resetpassword**").permitAll()
          .antMatchers("/api/captcha/request").permitAll()
          .antMatchers("/api/mobile/**").permitAll()

          
          
          //************* Any Authenticated users Objects **********
          //Pages
          .antMatchers("/dashboard").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/events").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER",
          "SUPERUSER")
          .antMatchers("/audits").hasAnyRole("USER", "PRESENTER", "ADMIN", "SUPPORTER",
          "SUPERUSER")
          .antMatchers("/profile").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/privacy").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/reports").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/ticketing").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          
          
          //APIs
          .antMatchers("/api/groups/user").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/services/user").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/user/**").hasAnyRole("USER", "PRESENTER", "ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/logs/reports/user").hasAnyRole("USER","ADMIN",
          "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/logs/audits/user").hasAnyRole("USER","ADMIN", "SUPPORTER",
          "SUPERUSER")
          .antMatchers("/api/logs/events/user").hasAnyRole("USER","ADMIN", "SUPPORTER",
          "SUPERUSER")
          
          
          //****************** SUPERUSER & ADMIN & SUPPORTER ************************
          //pages
          .antMatchers("/services").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/createservice").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/users").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/groups").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/publicmessages").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          
          
          //APIs
          .antMatchers("/api/users/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/groups").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/services").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/groups/**").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/dashboard").hasAnyRole("ADMIN", "SUPPORTER", "SUPERUSER")
          .antMatchers("/api/logs/reports/users**").hasAnyRole("ADMIN", "SUPPORTER",
          "SUPERUSER")
          .antMatchers("/api/logs/audits/users**").hasAnyRole("ADMIN", "SUPPORTER",
          "SUPERUSER")
          .antMatchers("/api/logs/events/users**").hasAnyRole("ADMIN", "SUPPORTER",
          "SUPERUSER")
          .antMatchers("/api/transcripts/**").hasAnyRole("ADMIN", "SUPPORTER",
          "SUPERUSER")
          
          //******************SUPERUSER Objects ONLY *******************
          //pages
          .antMatchers("/roles").hasRole("SUPERUSER")
          .antMatchers("/configs**").hasRole("SUPERUSER")
          
          //APIs
          .antMatchers("/api/configs/**").hasRole("SUPERUSER")
          .antMatchers("/api/configs").hasRole("SUPERUSER")
          .antMatchers("/api/roles**").hasRole("SUPERUSER")
          .antMatchers("/api/roles/**").hasRole("SUPERUSER")
          .antMatchers("/api/refresh**").hasRole("SUPERUSER")
          .antMatchers("/api/refresh/**").hasRole("SUPERUSER")
          .antMatchers("/api/superuser/**").hasRole("SUPERUSER")
          .antMatchers("/api/settings").hasRole("SUPERUSER")
          
          
         //******************SUPPORTER and SUPERUSER Objects *******************
         //pages
          
         //APIs
         .antMatchers("/api/tickets").hasAnyRole("SUPPORTER", "SUPERUSER")
          
          
          //******************SUPPORTER and SUPERUSER and ADMIN Objects*******************
          
          
          //APIs
          .antMatchers("/api/supporter/**").hasAnyRole("ADMIN", "SUPERUSER",
          "SUPPORTER")
          .antMatchers("/api/properties**").hasAnyRole("ADMIN", "SUPERUSER",
          "SUPPORTER")
          
          .anyRequest().authenticated()
          .and()





         
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

  
  /** 
   * CAS configuration.
   * @param auth whch indicates who have logged-in.
   */
  @Override
  public void configure(AuthenticationManagerBuilder auth) {
    auth
        .authenticationProvider(casAuthenticationProvider);

  }

  
  /** 
   * Authentication Manager.
   * @return AuthenticationManager
   */
  @Bean
  @Override
  protected AuthenticationManager authenticationManager() {
    return new ProviderManager(Collections.singletonList(casAuthenticationProvider));
  }

  
  
  /**
   * Authentication Entry Point.
   * @return AuthenticationEntryPoint
   * @su
   */
  public AuthenticationEntryPoint authenticationEntryPoint() {
    CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
    entryPoint.setLoginUrl(casLogin);
    entryPoint.setServiceProperties(serviceProperties);
    return entryPoint;
  }
}
