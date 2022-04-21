package parsso.idman.utils.captcha.impl.subClass;

import parsso.idman.utils.captcha.models.CAPTCHA;

import parsso.idman.utils.captcha.models.CAPTCHAimage;
import parsso.idman.utils.captcha.models.Points;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Image {
  private int nLines = 20;

  public CAPTCHAimage create(String phrase, CAPTCHA captcha) {

    String text = phrase + " ";

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

}
