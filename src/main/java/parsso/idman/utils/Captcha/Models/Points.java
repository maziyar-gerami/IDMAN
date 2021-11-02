package parsso.idman.utils.Captcha.Models;


import lombok.Getter;

@Getter
public class Points {
    private final int width;
    private final int height;
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    public Points(int width, int height) {
        this.width = width;
        this.height = height;

        x1 = (int) (Math.random() * width);
        y1 = (int) (Math.random() * height);

        x2 = (int) (Math.random() * width);
        y2 = (int) (Math.random() * height);

    }
}
