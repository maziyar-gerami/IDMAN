package parsso.idman.Configs;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
@Setter
@Getter
public class CaptchaSettings {

    private String site;
    private String secret;

}
