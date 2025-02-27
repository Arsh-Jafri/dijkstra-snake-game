import java.awt.Point;
import java.util.*;

public class Node implements Comparable<Node> {
    private Point position;
    private int distance;
    private Node previous;
    
    public Node(Point position) {
        this.position = position;
        this.distance = Integer.MAX_VALUE;
        this.previous = null;
    }
    
    public Point getPosition() {
        return position;
    }
    
    public int getDistance() {
        return distance;
    }
    
    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    public Node getPrevious() {
        return previous;
    }
    
    public void setPrevious(Node previous) {
        this.previous = previous;
    }
    
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return position.equals(node.position);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
} 