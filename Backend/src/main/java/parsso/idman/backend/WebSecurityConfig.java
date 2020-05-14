package parsso.idman.backend;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;


@Configuration
@Order(99)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //String loginPage = "http://localhost:63343/Frontend/LoginPage/login.html";
    //String loginPage = "http://bamkaa.ir/parsso/LoginPage/login.html";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();

                //.loginPage()
                //.permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=people")
                .contextSource()
                .url("ldap://localhost:10389/dc=example,dc=com")
                .and()
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder() {
                })
                .passwordAttribute("userPassword");
    }
}