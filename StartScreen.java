import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JPanel {
    private Color selectedPlayerColor = Color.GREEN;
    private Color selectedAIColor = Color.BLUE;
    private int gameSpeed = 100; // Default speed
    private boolean gameStarted = false;
    private Game parentGame;
    
    public StartScreen(Game parent) {
        this.parentGame = parent;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        // Create title
        JLabel titleLabel = new JLabel("Dijkstra's Snake Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Create settings panel
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        settingsPanel.setBackground(Color.BLACK);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Player color selection
        JPanel playerColorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerColorPanel.setBackground(Color.BLACK);
        JLabel playerColorLabel = new JLabel("Player Snake Color: ");
        playerColorLabel.setForeground(Color.WHITE);
        JButton playerColorButton = new JButton("Choose Color");
        playerColorButton.setBackground(selectedPlayerColor);
        playerColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Player Snake Color", selectedPlayerColor);
            if (newColor != null) {
                selectedPlayerColor = newColor;
                playerColorButton.setBackground(newColor);
            }
        });
        playerColorPanel.add(playerColorLabel);
        playerColorPanel.add(playerColorButton);
        
        // AI color selection
        JPanel aiColorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        aiColorPanel.setBackground(Color.BLACK);
        JLabel aiColorLabel = new JLabel("AI Snake Color: ");
        aiColorLabel.setForeground(Color.WHITE);
        JButton aiColorButton = new JButton("Choose Color");
        aiColorButton.setBackground(selectedAIColor);
        aiColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose AI Snake Color", selectedAIColor);
            if (newColor != null) {
                selectedAIColor = newColor;
                aiColorButton.setBackground(newColor);
            }
        });
        aiColorPanel.add(aiColorLabel);
        aiColorPanel.add(aiColorButton);
        
        // Speed control
        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        speedPanel.setBackground(Color.BLACK);
        JLabel speedLabel = new JLabel("Game Speed: ");
        speedLabel.setForeground(Color.WHITE);
        String[] speeds = {"Slow", "Normal", "Fast", "Very Fast"};
        JComboBox<String> speedComboBox = new JComboBox<>(speeds);
        speedComboBox.setSelectedIndex(1); // Normal speed by default
        speedComboBox.addActionListener(e -> {
            switch (speedComboBox.getSelectedIndex()) {
                case 0: gameSpeed = 150; break; // Slow
                case 1: gameSpeed = 100; break; // Normal
                case 2: gameSpeed = 70; break;  // Fast
                case 3: gameSpeed = 40; break;  // Very Fast
            }
        });
        speedPanel.add(speedLabel);
        speedPanel.add(speedComboBox);
        
        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(e -> {
            gameStarted = true;
            startGame();
        });
        
        // Add components to settings panel
        settingsPanel.add(playerColorPanel);
        settingsPanel.add(aiColorPanel);
        settingsPanel.add(speedPanel);
        settingsPanel.add(startButton);
        
        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(settingsPanel, BorderLayout.CENTER);
    }
    
    private void startGame() {
        parentGame.startGame(selectedPlayerColor, selectedAIColor, gameSpeed);
    }
    
    public boolean hasGameStarted() {
        return gameStarted;
    }
} 