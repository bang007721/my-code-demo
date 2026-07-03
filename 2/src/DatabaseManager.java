import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static Connection conn = null;

    // 获取连接（单例）
    private static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(
                Config.DB_URL,
                Config.DB_USER,
                Config.DB_PASSWORD
            );
        }
        return conn;
    }

    // 保存分数
    public static void saveScore(String playerName, int score, int speedLevel) {
        String sql = "INSERT INTO leaderboard (player_name, score, speed_level) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.setInt(3, speedLevel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("保存分数失败: " + e.getMessage());
        }
    }

    // 获取排行榜前十
    public static List<ScoreEntry> getTopScores() {
        List<ScoreEntry> list = new ArrayList<>();
        String sql = "SELECT player_name, score, speed_level, play_time FROM leaderboard ORDER BY score DESC LIMIT 10";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ScoreEntry(
                    rs.getString("player_name"),
                    rs.getInt("score"),
                    rs.getInt("speed_level"),
                    rs.getTimestamp("play_time")
                ));
            }
        } catch (SQLException e) {
            System.err.println("获取排行榜失败: " + e.getMessage());
        }
        return list;
    }

    // 内部类：排行榜条目
    public static class ScoreEntry {
        public String name;
        public int score;
        public int speedLevel;
        public Timestamp time;

        public ScoreEntry(String name, int score, int speedLevel, Timestamp time) {
            this.name = name;
            this.score = score;
            this.speedLevel = speedLevel;
            this.time = time;
        }
    }
}