import java.awt.*;

public class Dino {
    private int x, y;
    private int width = 30;
    private int height = 40;
    private int groundY;
    private double vy;
    private boolean isJumping;
    private static final double GRAVITY = 0.4; // 调整重力，使跳跃时间延长
    private static final double JUMP_POWER = -8.485; // 调整起跳速度以匹配原跳跃高度

    public Dino(int startX, int groundY) {
        this.x = startX;
        this.groundY = groundY;
        this.y = groundY - height;
        this.vy = 0;
        this.isJumping = false;
    }

    public void jump() {
        if (!isJumping) {
            vy = JUMP_POWER;
            isJumping = true;
        }
    }

    public void update() {
        if (isJumping) {
            vy += GRAVITY;
            y += vy;
            if (y >= groundY - height) {
                y = groundY - height;
                isJumping = false;
                vy = 0;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        // 更精细的图形设计
        // 1. 身体
        g2d.setColor(new Color(90, 60, 40));
        g2d.fillOval(x, y, 30, 30); // 椭圆形身体
        g2d.setColor(new Color(70, 50, 30));
        g2d.fillOval(x + 5, y + 5, 20, 20); // 腹部

        // 2. 头部
        g2d.setColor(new Color(120, 80, 60));
        g2d.fillOval(x + 8, y - 10, 14, 14); // 头部

        // 3. 眼睛
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + 12, y - 6, 6, 6); // 左眼
        g2d.fillOval(x + 16, y - 6, 6, 6); // 右眼
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x + 13, y - 5, 3, 3); // 左瞳孔
        g2d.fillOval(x + 17, y - 5, 3, 3); // 右瞳孔

        // 4. 肢体
        g2d.setColor(new Color(90, 60, 40));
        g2d.fillRect(x + 5, y + 25, 20, 7); // 腿部
        g2d.fillRect(x + 15, y + 32, 10, 4); // 脚部

        // 5. 翅膀 (新增，提高分辨率)
        g2d.setColor(new Color(100, 80, 60));
        g2d.fillOval(x - 10, y + 10, 10, 5); // 拟态翅膀

        // 6. 阴影
        g2d.setColor(new Color(30, 20, 10));
        g2d.fillOval(x + 1, y + 1, 28, 35); // 阴影加强体感
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}