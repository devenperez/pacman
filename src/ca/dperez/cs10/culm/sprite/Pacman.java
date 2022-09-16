package ca.dperez.cs10.culm.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ca.dperez.cs10.culm.GameState;
import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.TickEvents;
import ca.dperez.cs10.culm.Utility;
import ca.dperez.cs10.culm.sound.PlaySound;
import ca.dperez.cs10.culm.sprite.ghosts.GhostMode;
import ca.dperez.cs10.culm.tile.FoodType;
import ca.dperez.cs10.culm.tile.Tile;
import ca.dperez.cs10.culm.tile.TileType;

public class Pacman 
{
	private Tile startTile;
	private double x;
	private double y;
	private Tile currentTile;
	private Direction direction;
	private Direction queuedDirection;
	private int queueTolerance;
	private int score;
	private int lives;
	private double[] speed;
	private boolean extraLifeActivated;
	private int ghostsEaten;

	public Pacman(Tile startTile)
	{
		// default facing left
		setStartingTile(startTile);
		direction = Direction.LEFT;
		queuedDirection = direction;
		currentTile = startTile;
		score = 0;
		lives = PMConstants.STARTING_LIVES;
		speed = PMConstants.PACMAN_SPEED[0];
	}
	
	private void setStartingTile(Tile startTile)
	{
		// Sets starting position to current
		this.startTile = startTile;
		this.x = startTile.getXinPixels();
		this.y = startTile.getYinPixels();
	}
	
	public void draw(Graphics g)
	{
		// Allows draw to be called with a Graphics or Graphics2D parameter without casting
		draw((Graphics2D) g);
	}
	
	public void draw(Graphics2D g)
	{
		if(PacmanGame.gamePanel.getGameState() == GameState.RUNNING)
		{
			// Draw Pacman in current direction
			g.setColor(Color.YELLOW);
			switch(direction)
			{
			case UP:
				g.fillArc((int) x, (int) y, 25, 25, 135 - (TickEvents.getTime() * 2 % 45), 270 + (TickEvents.getTime() * 4 % 90)); // (90 - 125) (360 - 290)
				break;
			case LEFT:
				g.fillArc((int) x, (int) y, 25, 25, 225 - (TickEvents.getTime() * 2 % 45), 270 + (TickEvents.getTime() * 4 % 90)); // (180 - 215) (360 - 290)
				break;
			case DOWN:
				g.fillArc((int) x, (int) y, 25, 25, 315 - (TickEvents.getTime() * 2 % 45), 270 + (TickEvents.getTime() * 4 % 90)); // (270 - 305) (360 - 290)
				break;
			case RIGHT:
				g.fillArc((int) x, (int) y, 25, 25, 45 - (TickEvents.getTime() * 2 % 45), 270 + (TickEvents.getTime() * 4 % 90)); // (0 - 35) (360 - 290)
				break;
			}
		}
		else if(PacmanGame.gamePanel.getGameState() == GameState.STARTING)
		{
			g.setColor(Color.YELLOW);
			g.fillArc((int) x, (int) y, 25, 25, 0, 360);
		}
		else if(PacmanGame.gamePanel.getGameState() == GameState.DEATH)
		{
			g.setColor(Color.YELLOW);
			g.fillArc((int) x, (int) y, 25, 25, (180 - TickEvents.getDeathTimer() * 2) + 90, TickEvents.getDeathTimer() * 4);
		}
		
		
		// TESTING CURRENT TILE VISUAL
		if(PMConstants.DEBUG_MODE)
		{
			Color c = g.getColor();
			g.drawRect(this.currentTile.getXinPixels(), this.currentTile.getYinPixels(), 24, 24);
			g.setColor(Color.GREEN);
			for(Tile tile : this.currentTile.getSurroundingTiles())
				g.drawRect(tile.getXinPixels(), tile.getYinPixels(), 24, 24);
			g.setColor(c);
		}
		
	}
	
	public void tick()
	{
		changeDirection();
		
		move();
		
		changeCurrentTile();
		
		if(!extraLifeActivated && score > PMConstants.EXTRA_LIFE_POINTS)
		{
			lives++;
			new PlaySound("extrapac");
			extraLifeActivated = true;
		}
	}
	
	private void move() 
	{
		// Check for wall, if none, move
		switch(direction)
		{
		case UP:
			if(this.currentTile.getSurroundingTiles()[0].checkType(TileType.TELEPORT))
				this.y = this.currentTile.getSurroundingTiles()[0].getTeleportPortal().getYinPixels();
			else if((this.currentTile.getSurroundingTiles()[0].isPathType()) || 
			(int) this.y - 24 > this.currentTile.getSurroundingTiles()[0].getYinPixels())
				this.y -= speedValue();
			break;
		case RIGHT:
			if(this.currentTile.getSurroundingTiles()[2].checkType(TileType.TELEPORT))
				this.x = this.currentTile.getSurroundingTiles()[2].getTeleportPortal().getXinPixels();
			else if((this.currentTile.getSurroundingTiles()[2].isPathType()) || 
					(int) this.x + 24 < this.currentTile.getSurroundingTiles()[2].getXinPixels())
				this.x += speedValue();
			break;
		case DOWN:
			if(this.currentTile.getSurroundingTiles()[4].checkType(TileType.TELEPORT))
				this.y = this.currentTile.getSurroundingTiles()[4].getTeleportPortal().getYinPixels();
			else if((this.currentTile.getSurroundingTiles()[4].isPathType()) || 
					(int) this.y + 24 < this.currentTile.getSurroundingTiles()[4].getYinPixels())
				this.y += speedValue();
			break;
		case LEFT:
			if(this.currentTile.getSurroundingTiles()[6].checkType(TileType.TELEPORT))
				this.x = this.currentTile.getSurroundingTiles()[6].getTeleportPortal().getXinPixels();
			else if((this.currentTile.getSurroundingTiles()[6].isPathType()) || 
					(int) this.x - 24 > this.currentTile.getSurroundingTiles()[6].getXinPixels())
				this.x -= speedValue();
			break;
		}
	}

	private double speedValue() 
	{
		double maxSpeed;
		if(PacmanGame.difficulty == -1)
			maxSpeed = 3.5;
		else
			maxSpeed = PMConstants.PACMAN_MAX_SPEED;
		
		if(currentTile.isJustEaten())
			return maxSpeed * speed[PacmanGame.gamePanel.currentGhostMode == GhostMode.FRIGHTENED ? 3 : 1];
		else
			return maxSpeed * speed[PacmanGame.gamePanel.currentGhostMode == GhostMode.FRIGHTENED ? 2 : 0];
	}

	public Tile getCurrentTile() 
	{
		return currentTile;
	}

	public void queueDirectionChange(Direction queuedDirection)
	{
		// Queue change; override any previous queue
		queueTolerance = 0;
		this.queuedDirection = queuedDirection;
	}
	
	private void changeDirection()
	{
		// Check for queued direction
		if(queuedDirection != direction && Utility.equalsWithError(x, currentTile.getXinPixels(), 3) && Utility.equalsWithError(y, currentTile.getYinPixels(), 3)) 
		{
			
			if(queuedDirection == Direction.LEFT && this.currentTile.getSurroundingTiles()[6].isPathType())
			{
				direction = queuedDirection;
				y = currentTile.getYinPixels();
			}
			else if(queuedDirection == Direction.RIGHT && this.currentTile.getSurroundingTiles()[2].isPathType()) 
			{
				direction = queuedDirection;
				y = currentTile.getYinPixels();
			}
			else if(queuedDirection == Direction.UP && this.currentTile.getSurroundingTiles()[0].isPathType()) 
			{
				direction = queuedDirection;
				x = currentTile.getXinPixels();
			}
			else if(queuedDirection == Direction.DOWN && this.currentTile.getSurroundingTiles()[4].isPathType()) 
			{
				direction = queuedDirection; 
				x = currentTile.getXinPixels();
			}
		}
	}
	
	private void changeCurrentTile()
	{
		
		// Change current tile
		Tile oldTile = currentTile;
		Tile newTile = PacmanGame.maze.tiles[(int) ((this.x - PMConstants.HORIZONTAL_MARGIN + 13) / 24)][(int) ((this.y - PMConstants.VERTICAL_MARGIN - 59) / 24)];
		if(oldTile != newTile) 
		{
			oldTile.setJustEaten(false);
			currentTile = newTile;
			if(queueTolerance++ == 1)
				queuedDirection = direction;
			eatFood();
			eatFruit();
		}
	}

	private void eatFood() 
	{
		// Eats food if available and adds appropriate score
		if(currentTile.getFood() == FoodType.NORMAL)
		{
			currentTile.eatFood();
			currentTile.setJustEaten(true);
			score += 10;
			PacmanGame.maze.dotsEaten++;
			if(score > PMConstants.HIGH_SCORE) PMConstants.HIGH_SCORE = score;
		}
		else if(currentTile.getFood() == FoodType.POWER)
		{
			currentTile.eatFood();
			currentTile.setJustEaten(true);
			score += 50;
			PacmanGame.maze.dotsEaten++;
			ghostsEaten = 0;
			Ghost.frighten();
			if(score > PMConstants.HIGH_SCORE) PMConstants.HIGH_SCORE = score;
		}
		else
		{
			return;
		}
		
		
		// Tick Ghost Timer
		if(!PacmanGame.maze.globalTimer.isActive())
		{
			for(int i = 1; i < PacmanGame.maze.ghosts.length; i++)
			{
				if(PacmanGame.maze.ghosts[i].getTimer().count())
				{
					if(PacmanGame.maze.ghosts[i].getTimer().exceedsLimit())
					{
						PacmanGame.maze.ghosts[i].activate();
						if(i < PacmanGame.maze.ghosts.length - 1) 
							PacmanGame.maze.ghosts[i + 1].getTimer().activate();
					}
					break;
				}
			}
		}
		else
		{
			PacmanGame.maze.globalTimer.count();
		}
	}
	
	public void eatGhost()
	{
		score += 100 * Math.pow(2, ++ghostsEaten);
		new PlaySound("eatghost");
		new PointIndicator((int) x, (int) y, (int) (100 * Math.pow(2, ghostsEaten)));
	}
	
	public void eatFruit()
	{
		if(currentTile == Fruit.fruitTile && PacmanGame.maze.fruit != null)
		{
			PacmanGame.maze.player.score += PacmanGame.maze.fruit.getValue();
			new PlaySound("eatfruit");
			new PointIndicator((int) x, (int) y, PacmanGame.maze.fruit.getValue());
			PacmanGame.maze.fruit = null;
		}
	}
	
	public void death()
	{
		PacmanGame.gamePanel.setGameState(GameState.DEATH);
		new PlaySound("death").play();
		TickEvents.startDeathTimer();
		PacmanGame.maze.globalTimer.reset();
		PacmanGame.maze.globalTimer.activate();
	}
	
	public void loseLife()
	{
		PacmanGame.maze.reset();
		if(--lives >= 0)
		{
			PacmanGame.gamePanel.setGameState(GameState.STARTING);
		}
		else
		{
			PacmanGame.gamePanel.setGameState(GameState.GAME_OVER);
		}
		
	}
	
	public void reset()
	{
		x = startTile.getXinPixels();
		y = startTile.getYinPixels();
		direction = Direction.LEFT;
		queuedDirection = direction;
		currentTile = startTile;
	}
	public void restart()
	{
		score = 0;
		lives = PacmanGame.difficulty == -1 ? 5 : PMConstants.STARTING_LIVES;
		extraLifeActivated = false;
		ghostsEaten = 0;
	}

	public int getScore() {
		return score;
	}

	public int getLives() {
		return lives;
	}

	public void setSpeed(double[] speed) {
		this.speed = speed;
	}

	public Direction getDirection() {
		return direction;
	}
}
