package parsso.idman.Configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@ComponentScan("org.springframework.security.samples.mvc")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    // ...

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("http://bamkaa.ir/parsso/LoginPage/login.html").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}

