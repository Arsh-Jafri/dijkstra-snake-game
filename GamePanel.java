import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int CELL_SIZE = 25;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 20;
    private static final int INITIAL_SNAKE_LENGTH = 4;
    private static final int CONTROL_PANEL_HEIGHT = 50;
    
    private Snake playerSnake;
    private Snake aiSnake;
    private Point food;
    private javax.swing.Timer timer;
    private Random random;
    private boolean gameOver;
    private boolean gamePaused;
    private JPanel controlPanel;
    private JButton restartButton;
    private JButton pauseResumeButton;
    private GameAreaPanel gameArea;
    private Color playerColor;
    private Color aiColor;
    
    private class GameAreaPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGame(g);
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        }
        
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
        
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    }
    
    public GamePanel(Color playerColor, Color aiColor, int gameSpeed) {
        this.playerColor = playerColor;
        this.aiColor = aiColor;
        
        setLayout(new BorderLayout(0, 10));
        
        // Initialize variables
        random = new Random();
        gameOver = false;
        gamePaused = false;
        
        // Create game area panel
        gameArea = new GameAreaPanel();
        gameArea.setBackground(Color.BLACK);
        gameArea.setFocusable(true);
        gameArea.addKeyListener(this);
        
        // Create control panel
        controlPanel = new JPanel();
        controlPanel.setBackground(Color.DARK_GRAY);
        controlPanel.setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, CONTROL_PANEL_HEIGHT));
        
        restartButton = new JButton("Restart");
        pauseResumeButton = new JButton("Pause");
        
        Dimension buttonSize = new Dimension(100, 30);
        restartButton.setPreferredSize(buttonSize);
        pauseResumeButton.setPreferredSize(buttonSize);
        
        restartButton.addActionListener(e -> {
            restartGame();
            gameArea.requestFocusInWindow();
        });
        
        pauseResumeButton.addActionListener(e -> {
            togglePause();
            gameArea.requestFocusInWindow();
        });
        
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(restartButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(pauseResumeButton);
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        wrapperPanel.add(gameArea, gbc);
        
        add(wrapperPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        timer = new javax.swing.Timer(gameSpeed, e -> {
            update();
            gameArea.repaint();
        });
        
        initializeSnakes();
        spawnFood();
        
        timer.start();
        
        SwingUtilities.invokeLater(() -> gameArea.requestFocusInWindow());
    }
    
    private void initializeSnakes() {
        playerSnake = new Snake(5, GRID_HEIGHT / 2, false);
        for (int i = 0; i < INITIAL_SNAKE_LENGTH - 1; i++) {
            playerSnake.grow();
        }
        
        aiSnake = new Snake(GRID_WIDTH - 5, GRID_HEIGHT / 2, true);
        for (int i = 0; i < INITIAL_SNAKE_LENGTH - 1; i++) {
            aiSnake.grow();
        }
    }
    
    private void initializeGame() {
        initializeSnakes();
        spawnFood();
        gameOver = false;
        gamePaused = false;
        pauseResumeButton.setText("Pause");
        if (!timer.isRunning()) {
            timer.start();
        }
        gameArea.requestFocusInWindow();
    }
    
    private void restartGame() {
        initializeGame();
        gameArea.repaint();
    }
    
    private void togglePause() {
        gamePaused = !gamePaused;
        if (gamePaused) {
            timer.stop();
            pauseResumeButton.setText("Resume");
        } else {
            timer.start();
            pauseResumeButton.setText("Pause");
        }
        gameArea.requestFocusInWindow();
    }
    
    private void update() {
        if (!gameOver && !gamePaused) {
            Queue<Point> aiPath = findPathToFood(aiSnake);
            aiSnake.setPathToFood(aiPath);
            
            playerSnake.move();
            aiSnake.move();
            
            Point playerHead = playerSnake.getHead();
            Point aiHead = aiSnake.getHead();
            
            if (playerHead.x < 0 || playerHead.x >= GRID_WIDTH || 
                playerHead.y < 0 || playerHead.y >= GRID_HEIGHT) {
                gameOver = true;
            }
            
            if (playerSnake.collidesWithSelf() || 
                aiSnake.collidesWith(playerHead) ||
                playerSnake.collidesWith(aiHead)) {
                gameOver = true;
            }
            
            if (playerHead.equals(food) || aiHead.equals(food)) {
                if (playerHead.equals(food)) {
                    playerSnake.grow();
                }
                if (aiHead.equals(food)) {
                    aiSnake.grow();
                }
                spawnFood();
            }
        }
    }
    
    private void drawGame(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        
        g.setColor(playerColor);
        for (Point p : playerSnake.getBody()) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        g.setColor(aiColor);
        for (Point p : aiSnake.getBody()) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOverText = "Game Over!";
            FontMetrics metrics = g.getFontMetrics();
            int x = (gameArea.getWidth() - metrics.stringWidth(gameOverText)) / 2;
            int y = gameArea.getHeight() / 2;
            g.drawString(gameOverText, x, y);
        } else if (gamePaused) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String pausedText = "Paused";
            FontMetrics metrics = g.getFontMetrics();
            int x = (gameArea.getWidth() - metrics.stringWidth(pausedText)) / 2;
            int y = gameArea.getHeight() / 2;
            g.drawString(pausedText, x, y);
        }
    }
    
    private void spawnFood() {
        do {
            food = new Point(random.nextInt(GRID_WIDTH), random.nextInt(GRID_HEIGHT));
        } while (playerSnake.collidesWith(food) || aiSnake.collidesWith(food));
    }
    
    private Queue<Point> findPathToFood(Snake snake) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Node> nodes = new HashMap<>();
        
        Point start = snake.getHead();
        Node startNode = new Node(start);
        startNode.setDistance(0);
        openSet.offer(startNode);
        nodes.put(start, startNode);
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            Point pos = current.getPosition();
            
            if (pos.equals(food)) {
                return reconstructPath(current);
            }
            
            closedSet.add(pos);
            
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : directions) {
                Point next = new Point(pos.x + dir[0], pos.y + dir[1]);
                
                if (next.x < 0 || next.x >= GRID_WIDTH || next.y < 0 || next.y >= GRID_HEIGHT
                    || closedSet.contains(next) || playerSnake.collidesWith(next) || aiSnake.collidesWith(next)) {
                    continue;
                }
                
                int newDist = current.getDistance() + 1;
                Node neighbor = nodes.computeIfAbsent(next, Node::new);
                
                if (newDist < neighbor.getDistance()) {
                    neighbor.setDistance(newDist);
                    neighbor.setPrevious(current);
                    openSet.offer(neighbor);
                }
            }
        }
        
        return new LinkedList<>();
    }
    
    private Queue<Point> reconstructPath(Node end) {
        LinkedList<Point> path = new LinkedList<>();
        Node current = end;
        
        while (current.getPrevious() != null) {
            path.addFirst(current.getPosition());
            current = current.getPrevious();
        }
        
        return path;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // This is now handled by the timer's lambda
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gamePaused) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    playerSnake.setDirection(0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                    playerSnake.setDirection(0, 1);
                    break;
                case KeyEvent.VK_LEFT:
                    playerSnake.setDirection(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    playerSnake.setDirection(1, 0);
                    break;
                case KeyEvent.VK_SPACE:
                    if (gameOver) {
                        initializeGame();
                    }
                    break;
                case KeyEvent.VK_P:
                    togglePause();
                    break;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            togglePause();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
} 