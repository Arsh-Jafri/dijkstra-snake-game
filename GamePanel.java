import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int CELL_SIZE = 20;
    private static final int GRID_WIDTH = 40;
    private static final int GRID_HEIGHT = 30;
    private static final int GAME_SPEED = 100; // Milliseconds between updates
    
    private Snake playerSnake;
    private Snake aiSnake;
    private Point food;
    private javax.swing.Timer timer;
    private Random random;
    private boolean gameOver;
    
    public GamePanel() {
        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        random = new Random();
        initializeGame();
        
        timer = new javax.swing.Timer(GAME_SPEED, this);
        timer.start();
    }
    
    private void initializeGame() {
        playerSnake = new Snake(5, GRID_HEIGHT / 2, false);
        aiSnake = new Snake(GRID_WIDTH - 5, GRID_HEIGHT / 2, true);
        spawnFood();
        gameOver = false;
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
            
            // Check all four directions
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
        
        return new LinkedList<>(); // No path found
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw grid (optional)
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        
        // Draw player snake
        g.setColor(Color.GREEN);
        for (Point p : playerSnake.getBody()) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        // Draw AI snake
        g.setColor(Color.BLUE);
        for (Point p : aiSnake.getBody()) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOverText = "Game Over!";
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(gameOverText)) / 2;
            int y = getHeight() / 2;
            g.drawString(gameOverText, x, y);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Update AI snake's path
            Queue<Point> aiPath = findPathToFood(aiSnake);
            aiSnake.setPathToFood(aiPath);
            
            // Move snakes
            playerSnake.move();
            aiSnake.move();
            
            // Check collisions
            Point playerHead = playerSnake.getHead();
            Point aiHead = aiSnake.getHead();
            
            // Wall collisions
            if (playerHead.x < 0 || playerHead.x >= GRID_WIDTH || 
                playerHead.y < 0 || playerHead.y >= GRID_HEIGHT) {
                gameOver = true;
            }
            
            // Snake collisions
            if (playerSnake.collidesWithSelf() || 
                aiSnake.collidesWith(playerHead) ||
                playerSnake.collidesWith(aiHead)) {
                gameOver = true;
            }
            
            // Food collision
            if (playerHead.equals(food) || aiHead.equals(food)) {
                if (playerHead.equals(food)) {
                    playerSnake.grow();
                }
                if (aiHead.equals(food)) {
                    aiSnake.grow();
                }
                spawnFood();
            }
            
            repaint();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
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
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
} 