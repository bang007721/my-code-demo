import java.awt.*;
import java.util.Random;

public class Obstacle {
    public static final int TYPE_GROUND = 0;
    public static final int TYPE_AIR = 1;
    public static final int TYPE_DIAGONAL = 2;

    private static final int GROUND_W = 20, GROUND_H = 35;
    private static final int AIR_W = 28, AIR_H = 18;
    private static final int DIAG_W = 25, DIAG_H = 25;
    private static final Random RAND = new Random();
    private static final Color
        GROUND_COLOR = new Color(50, 100, 30),
        GROUND_DARK = new Color(30, 70, 20),
        AIR_BODY = new Color(80, 60, 50),
        AIR_DARK = new Color(100, 70, 50),
        DIAG_BODY = new Color(200, 50, 50);

    private int type;
    private double x, y;
    private int width, height;
    private double dx, dy;
    private int groundLevel;

    public Obstacle(int startX, int groundY, int gameSpeed, boolean isAir, boolean isDiagonal) {
        this.groundLevel = groundY;
        this.type = isDiagonal ? TYPE_DIAGONAL : (isAir ? TYPE_AIR : TYPE_GROUND);
        this.x = startX;

        switch (type) {
            case TYPE_GROUND:
                width = GROUND_W;
                height = GROUND_H;
                y = groundY - height;
                dx = -gameSpeed;
                break;
            case TYPE_AIR:
                width = AIR_W;
                height = AIR_H;
                y = groundY - 65 + RAND.nextInt(20) - height;
                dx = -gameSpeed;
                break;
            default:
                width = DIAG_W;
                height = DIAG_H;
                y = RAND.nextInt(groundY / 2);
                double angle = 0.5 + RAND.nextDouble() * 0.5;
                double speed = gameSpeed * 1.2;
                dx = -speed * Math.cos(angle) - 7;
                dy = speed * Math.sin(angle);
        }
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics2D g) {
        int rx = (int) x, ry = (int) y;
        switch (type) {
            case TYPE_GROUND:
                g.setColor(GROUND_COLOR);
                g.fillRect(rx, ry, width, height);
                g.setColor(GROUND_DARK);
                g.fillRect(rx + 5, ry - 5, 4, 5);
                g.fillRect(rx + 12, ry - 7, 4, 7);
                break;
            case TYPE_AIR:
                g.setColor(AIR_BODY);
                g.fillOval(rx, ry, width, height);
                g.setColor(Color.ORANGE);
                g.fillOval(rx + width - 8, ry + 4, 6, 6);
                g.setColor(Color.BLACK);
                g.fillOval(rx + width - 6, ry + 6, 3, 3);
                g.setColor(AIR_DARK);
                g.fillArc(rx + 5, ry - 5, 15, 12, 0, 180);
                break;
            case TYPE_DIAGONAL:
                g.setColor(DIAG_BODY);
                g.fillOval(rx, ry, width, height);
                g.setColor(Color.YELLOW);
                g.fillOval(rx + 6, ry + 6, 12, 12);
                g.setColor(Color.BLACK);
                g.fillOval(rx + 10, ry + 10, 4, 4);
                break;
        }
    }

    public boolean isOffScreen() {
        return type == TYPE_DIAGONAL
            ? x + width < 0 || y > groundLevel + 50
            : x + width < 0;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}