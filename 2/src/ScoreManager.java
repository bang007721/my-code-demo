import java.awt.*;

public class ScoreManager {
    private int score;
    private int highScore;

    public ScoreManager() {
        this.score = 0;
        this.highScore = 0;
    }

    public void addScore(int points) {
        score += points;
        if (score > highScore) {
            highScore = score;
        }
    }

    public void draw(Graphics2D g2d, int speed, float difficulty) {
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 20, 40);
        g2d.drawString("Best: " + highScore, 20, 65);
        g2d.drawString("Speed: " + speed, 20, 90);
        g2d.drawString("Diff: x" + String.format("%.1f", difficulty), 20, 115);
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}