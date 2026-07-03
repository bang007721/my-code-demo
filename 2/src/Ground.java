import java.awt.*;

public class Ground {
    private int y;
    private int width;
    private int x1, x2;
    private int speed;

    public Ground(int groundY, int screenWidth) {
        this.y = groundY;
        this.width = screenWidth;
        this.x1 = 0;
        this.x2 = width;
        this.speed = 5;
    }

    public void update(int currentSpeed) {
        this.speed = currentSpeed;
        x1 -= speed;
        x2 -= speed;
        if (x1 + width < 0) x1 = x2 + width;
        if (x2 + width < 0) x2 = x1 + width;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(180, 130, 70));
        g2d.fillRect(0, y, width, 5);
        g2d.fillRect(x1, y - 8, 40, 8);
        g2d.fillRect(x2, y - 8, 40, 8);
        g2d.setColor(new Color(140, 100, 50));
        g2d.fillRect(0, y + 3, width, 3);
    }
}