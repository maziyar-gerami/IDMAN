package parsso.idman.backend;


import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
import org.springframework.core.annotation.Order;
=======
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;


@Configuration
<<<<<<< HEAD
@Order(99)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //String loginPage = "http://localhost:63343/Frontend/LoginPage/login.html";
    //String loginPage = "http://bamkaa.ir/parsso/LoginPage/login.html";
=======
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
<<<<<<< HEAD
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();

                //.loginPage()
                //.permitAll();
=======
                .anyRequest().fullyAuthenticated();
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
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