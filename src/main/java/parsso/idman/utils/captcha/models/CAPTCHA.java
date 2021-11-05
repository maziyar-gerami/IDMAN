package parsso.idman.utils.captcha.models;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Random;

@Getter
@Setter
public class CAPTCHA {
    private String id;
    private String phrase;
    private Date createdAt = new Date();

    public CAPTCHA(String phrase) {
        this.phrase = phrase;
        long timeStamp = new Date().getTime();
        this.id = (new Random().nextInt(9999)) + 10000 + "" + timeStamp;
    }
}
