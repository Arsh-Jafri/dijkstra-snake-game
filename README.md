# Dijkstra's Snake Game

A Java implementation of the classic Snake game with an AI opponent that uses Dijkstra's algorithm for pathfinding.

## Features

- Player-controlled snake using arrow keys
- AI snake that automatically finds the shortest path to food using Dijkstra's algorithm
- Grid-based game board with randomly spawning food
- Collision detection for walls, self, and other snake
- Game over screen with restart option

## Controls

- Arrow keys: Control the player snake's direction
- Space: Restart the game when game over

## How to Run

1. Make sure you have Java Development Kit (JDK) installed on your system
2. Compile the Java files:
   ```bash
   javac *.java
   ```
3. Run the game:
   ```bash
   java Game
   ```

## Game Rules

- Control your snake (green) to eat the food (red)
- Avoid colliding with:
  - Walls
  - Your own body
  - The AI snake (blue)
- Each time either snake eats food, it grows longer
- The AI snake will automatically find the shortest path to the food using Dijkstra's algorithm
- Game ends when the player snake collides with walls, itself, or the AI snake

## Implementation Details

- Built using Java Swing for graphics rendering
- Uses AWT for keyboard input handling
- Implements Dijkstra's algorithm with a priority queue for efficient pathfinding
- The AI snake uses an adjacency list representation for the game grid 