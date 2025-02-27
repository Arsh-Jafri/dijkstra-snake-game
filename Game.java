import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final String GAME_TITLE = "Dijkstra's Snake Game";
    private static final Dimension GAME_DIMENSION = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    
    public Game() {
        setTitle(GAME_TITLE);
        setPreferredSize(GAME_DIMENSION);
        setMinimumSize(GAME_DIMENSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Start with the start screen
        StartScreen startScreen = new StartScreen(this);
        startScreen.setPreferredSize(GAME_DIMENSION);
        add(startScreen);
        
        pack();
    }
    
    public void startGame(Color playerColor, Color aiColor, int gameSpeed) {
        getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(playerColor, aiColor, gameSpeed);
        gamePanel.setPreferredSize(GAME_DIMENSION);
        add(gamePanel);
        revalidate();
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }
} 