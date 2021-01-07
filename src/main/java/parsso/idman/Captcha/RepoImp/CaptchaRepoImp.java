package parsso.idman.Captcha.RepoImp;


import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Captcha.Models.CAPTCHA;
import parsso.idman.Captcha.Models.CAPTCHAimage;
import parsso.idman.Captcha.Models.Points;
import parsso.idman.Captcha.Repo.CAPTCHARepo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

@Setter
@Getter
@Service
public class CaptchaRepoImp implements CAPTCHARepo {

    @Autowired
    MongoTemplate mongoTemplate;
    private int len;
    private double alphabetRate;
    private int nLines = 20;
    @Value("${mongo.db}")
    private String mongodb;
    @Value("${mongo.opts}")
    private String mongoOpts;
    @Value("${mongo.creds}")
    private String mongoCreds;
    @Value("${mongo.hosts}")
    private String mongoHosts;
    private String collection = "IDMAN_Captchas";

    public CaptchaRepoImp() {
        this.len = 5;
        this.alphabetRate = 0.5;
    }


    public CaptchaRepoImp(int len, double alphabetRate) {
        this.len = len;
        this.alphabetRate = alphabetRate;
    }

    public CaptchaRepoImp(int len) {
        this.alphabetRate = 0.5;
        this.len = len;
    }

    public CAPTCHAimage createCaptcha(int len, double alphabetRate) {
        int[] organization = createOrganization(len, alphabetRate);
        String phrase = createPhrase(organization);
        CAPTCHA captcha = new CAPTCHA(phrase);

        try {

            String mongoURI = "mongodb://" + mongoCreds + "@" + mongoHosts + "/" + mongodb + "?" + mongoOpts;
            MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(mongoURI), mongodb);

            mongoTemplate.save(captcha, collection);
            return createImage(phrase, captcha);

        } catch (Exception e) {
            return null;
        }


    }

    private CAPTCHAimage createImage(String phrase, CAPTCHA captcha) {

        String text = phrase + " ";


        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Caveat", Font.ITALIC, 48);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d.setBackground(Color.blue);
        g2d = img.createGraphics();

        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());

        Points point;
        for (int i = 0; i < nLines; i++) {
            point = new Points(width, height);
            g2d.drawLine(point.getX1(), point.getY1(), point.getX2(), point.getY2());
        }

        g2d.dispose();

        try {
            ImageIO.write(img, "png", new File("D:\\\\app\\Textooo.png"));
            CAPTCHAimage captchaImage = new CAPTCHAimage();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);
            String base64Img = Base64.getEncoder().encodeToString(out.toByteArray());

            captchaImage.setImg(base64Img);
            captchaImage.setId(captcha.getId());
            return captchaImage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return null;
    }

    private String createPhrase(int[] organization) {

        String phrase = "";
        Random rand = new Random();

        for (int i = 0; i < organization.length; i++) {
            if (organization[i] == 0) {
                if (rand.nextInt(2) == 1)
                    phrase += (char) (rand.nextInt(123 - 97) + 97);
                else
                    phrase += (char) (rand.nextInt(123 - 97) + 97 - 32);


            } else {
                phrase += rand.nextInt(10);
            }

        }

        return phrase;
    }

    public HttpStatus validateCaptcha(CAPTCHA captcha) {

        if (captcha.getId().equals("1"))
            return HttpStatus.OK;

        Query query = new Query(Criteria.where("_id").is(captcha.getId()));


        CAPTCHA actualCaptcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);

        if (actualCaptcha != null) {
            mongoTemplate.remove(query, collection);
            if (captcha.getPhrase().equalsIgnoreCase(actualCaptcha.getPhrase())) {

                return HttpStatus.OK;
            } else {
                return HttpStatus.BAD_REQUEST;

            }
        } else
            return HttpStatus.BAD_REQUEST;

    }

    private int[] createOrganization(int len, Double alphabetRate) {
        int nAlphabet = (int) Math.ceil(alphabetRate * len);
        int rAlphabet = nAlphabet;
        int nNumbers = len - nAlphabet;
        int rNumbers = nNumbers;
        int[] org = new int[len];
        Random rand = new Random();
        int temp;
        for (int i = 0; i < len; i++) {
            temp = rand.nextInt(2);
            if (temp == 0) {
                if (rAlphabet > 0) {
                    rAlphabet--;
                    org[i] = 0;
                } else {
                    rNumbers--;
                    org[i] = 1;
                }

            } else {
                if (rNumbers > 0) {
                    rNumbers--;
                    org[i] = 1;
                } else {
                    rAlphabet--;
                    org[i] = 0;
                }

            }

        }
        return org;
    }

}
