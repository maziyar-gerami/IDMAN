package parsso.idman.Utils.Captcha.Models;


import lombok.Getter;

@Getter
public class Points {

    private final int width;
    private final int height;

    private int x1 = 0;
    private int y1 = 0;

    private int x2 = 0;
    private int y2 = 0;

    public Points(int width, int height) {
        this.width = width;
        this.height = height;

        x1 = (int) (Math.random() * width);
        y1 = (int) (Math.random() * height);

        x2 = (int) (Math.random() * width);
        y2 = (int) (Math.random() * height);

    }
}
