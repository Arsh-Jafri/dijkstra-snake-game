import java.awt.*;
import java.util.*;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Point direction;
    private boolean isAI;
    private Queue<Point> pathToFood;
    
    public Snake(int x, int y, boolean isAI) {
        this.isAI = isAI;
        body = new ArrayList<>();
        body.add(new Point(x, y));  // Head
        direction = new Point(1, 0); // Initially moving right
        pathToFood = new LinkedList<>();
    }
    
    public void move() {
        Point head = getHead();
        Point newHead;
        
        if (isAI && !pathToFood.isEmpty()) {
            Point nextPoint = pathToFood.poll();
            newHead = nextPoint;
        } else {
            newHead = new Point(head.x + direction.x, head.y + direction.y);
        }
        
        body.add(0, newHead);
        body.remove(body.size() - 1);
    }
    
    public void grow() {
        Point tail = body.get(body.size() - 1);
        body.add(new Point(tail.x, tail.y));
    }
    
    public void setDirection(int x, int y) {
        // Prevent 180-degree turns
        if ((x != 0 && x == -direction.x) || (y != 0 && y == -direction.y)) {
            return;
        }
        direction = new Point(x, y);
    }
    
    public boolean collidesWith(Point point) {
        return body.contains(point);
    }
    
    public boolean collidesWithSelf() {
        Point head = getHead();
        return body.subList(1, body.size()).contains(head);
    }
    
    public Point getHead() {
        return body.get(0);
    }
    
    public List<Point> getBody() {
        return body;
    }
    
    public void setPathToFood(Queue<Point> path) {
        this.pathToFood = path;
    }
    
    public boolean isAI() {
        return isAI;
    }
} 