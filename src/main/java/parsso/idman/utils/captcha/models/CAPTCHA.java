package parsso.idman.utils.captcha.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;

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

    public static int checkCaptcha(MongoTemplate mongoTemplate, User user, String cid, String answer){
        int s1=checkCaptcha(mongoTemplate, cid, answer);
        if (s1<0)
            return s1;

        if (user == null || user.getUserId() == null)
            return -3;

        return 0;
    }

    public static int checkCaptcha(MongoTemplate mongoTemplate, String cid, String answer) {
        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;

        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, Variables.col_captchas);
            return -1;
        }

        return 0;
    }
}
