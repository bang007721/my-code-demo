import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;

    private boolean running = true;
    private boolean gameOver = false;

    // 难度系统
    private int currentSpeed = 0;
    private final int MAX_SPEED = 15;
    private final int SPEED_INCREASE_INTERVAL = 400;
    private boolean isDifficultyTriggered = false;
    private boolean isWarningActive = false;
    private int warningTimer = 0;
    private final int WARNING_DURATION = 3 * 60;
    private boolean warningBlink = false;

    // 障碍物生成参数（受难度影响）
    private int obstacleCooldown = 0;
    private int baseCooldownMin = 40;
    private int baseCooldownMax = 80;
    private float difficultyMultiplier = 1.0f;

    private Dino dino;
    private ArrayList<Obstacle> obstacles;
    private Ground ground;
    private ScoreManager scoreManager;
    private Timer timer;
    private Random random;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(240, 230, 200));
        setFocusable(true);
        addKeyListener(this);

        initGame();
        timer = new Timer(16, this);
        timer.start();
    }

    private void initGame() {
        dino = new Dino(80, HEIGHT - 60);
        obstacles = new ArrayList<>();
        ground = new Ground(HEIGHT - 30, WIDTH);
        scoreManager = new ScoreManager();
        random = new Random();
        gameOver = false;
        running = true;
        currentSpeed = 0;
        isDifficultyTriggered = false;
        isWarningActive = false;
        warningTimer = 0;
        difficultyMultiplier = 1.0f;
        obstacleCooldown = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !gameOver) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        // 1. 更新速度（从4开始，每400分+1）
        int newSpeed = 4+scoreManager.getScore() / SPEED_INCREASE_INTERVAL;
        currentSpeed = Math.min(newSpeed, MAX_SPEED);

        // 2. 检查是否触发难度加强（速度达到5/10/15时）
        if (newSpeed % 5 == 0 && newSpeed >= 5 && !isDifficultyTriggered && !isWarningActive) {
            isWarningActive = true;
            warningTimer = WARNING_DURATION;
            isDifficultyTriggered = true;
        }

        // 3. 警告倒计时
        if (isWarningActive) {
            warningTimer--;
            warningBlink = (warningTimer / 15) % 2 == 0;
            if (warningTimer <= 0) {
                isWarningActive = false;
                applyDifficultyUpgrade();
            }
        }

        // 4. 更新游戏对象
        dino.update();
        ground.update(currentSpeed);

        // 5. 生成障碍物
        int minDelay = (int)(baseCooldownMin / difficultyMultiplier);
        int maxDelay = (int)(baseCooldownMax / difficultyMultiplier);
        if (obstacleCooldown <= 0) {
            int delay = minDelay + random.nextInt(maxDelay - minDelay + 1);
            obstacleCooldown = delay;
            boolean isAir = random.nextInt(100) < 35;
            boolean isDiagonal = false;
            if (difficultyMultiplier > 1.0f && random.nextInt(100) < 25) {
                isDiagonal = true;
                isAir = false;
            }
            obstacles.add(new Obstacle(WIDTH, HEIGHT - 55, currentSpeed, isAir, isDiagonal));
        } else {
            obstacleCooldown--;
        }

        // 6. 更新障碍物
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            obs.update();
            if (obs.isOffScreen()) {
                it.remove();
            }
        }

        // 7. 碰撞检测
        Rectangle dinoBounds = dino.getBounds();
        for (Obstacle obs : obstacles) {
            if (dinoBounds.intersects(obs.getBounds())) {
                gameOver = true;
                running = false;
                promptSaveScore();
                break;
            }
        }

        // 8. 加分
        if (running && !gameOver) {
            scoreManager.addScore(1);
        }
    }

    private void applyDifficultyUpgrade() {
        difficultyMultiplier += 0.3f;
    }

    private void promptSaveScore() {
        String name = JOptionPane.showInputDialog(this, 
            "游戏结束！您的得分：" + scoreManager.getScore() + 
            "\n请输入您的名字（留空则使用默认名）：", 
            "保存分数", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) {
            name = "Player";
        }
        DatabaseManager.saveScore(name.trim(), scoreManager.getScore(), currentSpeed);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        ground.draw(g2d);
        for (Obstacle obs : obstacles) {
            obs.draw(g2d);
        }
        dino.draw(g2d);

        scoreManager.draw(g2d, currentSpeed, difficultyMultiplier);

        if (isWarningActive && warningBlink) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 40));
            g2d.setColor(Color.RED);
            String warn = "⚠ 难度升级！ ⚠";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (WIDTH - fm.stringWidth(warn)) / 2;
            g2d.drawString(warn, x, HEIGHT / 3);
        }

        if (gameOver) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
            g2d.setColor(Color.DARK_GRAY);
            String msg = "GAME OVER";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (WIDTH - fm.stringWidth(msg)) / 2;
            g2d.drawString(msg, x, HEIGHT / 2 - 80);

            g2d.setFont(new Font("Monospaced", Font.PLAIN, 16));
            g2d.setColor(Color.BLACK);
            java.util.List<DatabaseManager.ScoreEntry> top = DatabaseManager.getTopScores();
            int yPos = HEIGHT / 2 - 30;
            g2d.drawString("=== 排行榜 Top 10 ===", 250, yPos);
            yPos += 25;
            int rank = 1;
            for (DatabaseManager.ScoreEntry entry : top) {
                String line = String.format("%d. %-10s  %6d  (速度 %d)", 
                    rank, entry.name, entry.score, entry.speedLevel);
                g2d.drawString(line, 250, yPos);
                yPos += 22;
                rank++;
                if (rank > 10) break;
            }

            g2d.setFont(new Font("Monospaced", Font.PLAIN, 18));
            String restartMsg = "Press R to restart";
            x = (WIDTH - g2d.getFontMetrics().stringWidth(restartMsg)) / 2;
            g2d.drawString(restartMsg, x, HEIGHT - 40);
        }
    }

    private void restartGame() {
        initGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameOver && key == KeyEvent.VK_R) {
            restartGame();
        } else if (!gameOver && (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP)) {
            dino.jump();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}