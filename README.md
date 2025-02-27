# Dijkstra's Snake Game

A Java implementation of the classic Snake game with an AI opponent that uses Dijkstra's algorithm for pathfinding.

## Features

- Player-controlled snake (blue by default) using arrow keys
- AI snake (red by default) that automatically finds the shortest path to food using Dijkstra's algorithm
- Customizable snake colors through color picker
- Adjustable game speed (Slow, Normal, Fast, Very Fast)
- 3-second countdown before game starts
- Grid-based game board with randomly spawning food
- Collision detection for walls, self, and other snake
- Game over screen with restart option
- Pause/Resume functionality
- Both snakes start with 4 blocks length

## Controls

- Arrow keys: Control the player snake's direction
- Space: Restart the game when game over
- P: Pause/Resume the game
- Restart button: Start a new game at any time
- Pause/Resume button: Toggle game pause state

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

- Control your snake (blue by default) to eat the food (red squares)
- Avoid colliding with:
  - Walls
  - Your own body
  - The AI snake (red by default)
- Each time either snake eats food, it grows longer
- The AI snake will automatically find the shortest path to the food using Dijkstra's algorithm
- Game ends if the player snake collides with walls, itself, or the AI snake

## Game Flow

1. Start Screen:
   - Choose your snake's color (default: blue)
   - Choose AI snake's color (default: red)
   - Select game speed (default: Normal)
   - Click "Start Game" to begin

2. Countdown Phase:
   - 3-second countdown before game starts
   - Snakes are visible but don't move during countdown
   - Both snakes start with 4 blocks length

3. Gameplay:
   - Move your snake with arrow keys
   - Collect food to grow longer
   - Avoid collisions
   - Pause anytime with 'P' key or Pause button
   - Restart anytime with Restart button

## Implementation Details

- Built using Java Swing for graphics rendering
- Uses AWT for keyboard input handling
- Implements Dijkstra's algorithm with a priority queue for efficient pathfinding
- The AI snake uses an adjacency list representation for the game grid
- Custom countdown timer for game start
- Separate timers for game logic and countdown
- Responsive controls and smooth animation 