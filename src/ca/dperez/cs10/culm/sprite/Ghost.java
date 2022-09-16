package ca.dperez.cs10.culm.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.TickEvents;
import ca.dperez.cs10.culm.Utility;
import ca.dperez.cs10.culm.sprite.ghosts.GhostMode;
import ca.dperez.cs10.culm.sprite.ghosts.GhostTimer;
import ca.dperez.cs10.culm.tile.FoodType;
import ca.dperez.cs10.culm.tile.Tile;
import ca.dperez.cs10.culm.tile.TileType;

public abstract class Ghost 
{	
	public static double[] speed = PMConstants.GHOST_SPEED[0];
	private static int frightenedTime = PMConstants.FRIGHTENED[0][0];
	private static int frightenedFlashes = PMConstants.FRIGHTENED[0][1];
	protected Tile startTile;
	protected double x;
	protected double y;
	private Tile currentTile;
	private Tile targetTile;
	private Direction direction;
	private BufferedImage[] textures;
	private GhostTimer timer;
	private GhostMode mode;
	private Direction bestDirection;
	private boolean defaultActive;

	public Ghost(BufferedImage[] textures, Tile startTile, int dotLimit, boolean defaultActive)
	{
		setStartingTile(startTile);
		direction = Direction.UP;
		currentTile = startTile;
		// Starts in Ghost holding box
		mode = GhostMode.NONE;
		this.textures = textures;
		// Timer required to leave house
		this.timer = new GhostTimer(dotLimit);
		this.defaultActive = defaultActive;
		if(this.defaultActive) this.timer.activate();
	}
	
	public Ghost(BufferedImage[] textures, Tile startTile)
	{
		// default facing left
		setStartingTile(startTile);
		direction = Direction.LEFT;
		currentTile = startTile;
		mode = GhostMode.CHASE;
		this.textures = textures;
	}
	
	public abstract void chaseBehaviour();
	public abstract void scatterBehaviour();
	
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
		if(mode == GhostMode.FRIGHTENED)
		{
			
			if(TickEvents.getFrightenedTimer() < Ghost.getFrightenedFlashes() * (TickEvents.TICKS_PER_SECOND / 4))
				if(TickEvents.getFrightenedTimer() % (TickEvents.TICKS_PER_SECOND / 4) <= (TickEvents.TICKS_PER_SECOND / 8))
					g.drawImage(PMConstants.SPRITES[5][1], (int) x, (int) y, null);
				else
					g.drawImage(PMConstants.SPRITES[5][0], (int) x, (int) y, null);
			else
				g.drawImage(PMConstants.SPRITES[5][0], (int) x, (int) y, null);
			
		}
		else if(mode == GhostMode.RETREAT)
		{
			switch(direction)
			{
			case UP:
				g.drawImage(PMConstants.SPRITES[4][0], (int) x, (int) y, null);
				break;
			case LEFT:
				g.drawImage(PMConstants.SPRITES[4][1], (int) x, (int) y, null);
				break;
			case DOWN:
				g.drawImage(PMConstants.SPRITES[4][2], (int) x, (int) y, null);
				break;
			case RIGHT:
				g.drawImage(PMConstants.SPRITES[4][3], (int) x, (int) y, null);
				break;
			}
		}
		else
		{
			// Draw Ghost in current direction
			switch(direction)
			{
			case UP:
				g.drawImage(textures[0], (int) x, (int) y, null);
				break;
			case LEFT:
				g.drawImage(textures[1], (int) x, (int) y, null);
				break;
			case DOWN:
				g.drawImage(textures[2], (int) x, (int) y, null);
				break;
			case RIGHT:
				g.drawImage(textures[3], (int) x, (int) y, null);
				break;
			}
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
		switch (mode) 
		{
		case FRIGHTENED:
			changeDirectionFrightened();
			break;
		case RETREAT:
			setTargetTile(PacmanGame.maze.ghostExitTile);
			changeDirectionDefault();
			retreatMove();
			changeCurrentTile();
			return;
		case NONE:
			changeDirectionInactive();
			break;
		case SCATTER:
			scatterBehaviour();
			changeDirectionDefault();
			break;
		case CHASE:
			chaseBehaviour();
			changeDirectionDefault();
			break;
		}
		
		
		move();
		
		changeCurrentTile();
	}

	private void retreatMove() 
	{
		if(currentTile.getSurroundingTiles()[4] == PacmanGame.maze.ghostExitTile)
		{
			direction = Direction.DOWN;
			setTargetTile(currentTile.getX(), currentTile.getY() - 2);
			this.y += speedValue();
		}
		
		if(currentTile.checkType(TileType.GHOST_HOUSE))
		{
			direction = Direction.UP;
			mode = PacmanGame.gamePanel.currentGhostMode;
			this.y -= speedValue();
		}
		
		// Modified move function
		switch(direction)
		{
		case UP:
			if(this.currentTile.getSurroundingTiles()[0].checkType(TileType.TELEPORT))
				this.y = this.currentTile.getSurroundingTiles()[0].getTeleportPortal().getYinPixels();
			else if(this.currentTile.getSurroundingTiles()[0].checkType(TileType.PATH, TileType.INTERSECTION, TileType.INTERSECTION_NO_UP, TileType.TUNNEL_PATH, TileType.GHOST_EXIT, TileType.GHOST_HOUSE) || 
					 this.y - 24 > this.currentTile.getSurroundingTiles()[0].getYinPixels())
				this.y -= speedValue();
			else if(this.currentTile.getSurroundingTiles()[0].checkType(TileType.EMPTY, TileType.WALL) && Utility.equalsWithError(y, this.currentTile.getYinPixels(), 3))
				this.y = this.currentTile.getYinPixels();
			break;
		case RIGHT:
			if(this.currentTile.getSurroundingTiles()[2].checkType(TileType.TELEPORT))
				this.x = this.currentTile.getSurroundingTiles()[2].getTeleportPortal().getXinPixels();
			else if(this.currentTile.getSurroundingTiles()[2].checkType(TileType.PATH, TileType.INTERSECTION, TileType.INTERSECTION_NO_UP, TileType.TUNNEL_PATH, TileType.GHOST_EXIT, TileType.GHOST_HOUSE) || 
					 this.x + 24 < this.currentTile.getSurroundingTiles()[2].getXinPixels())
				this.x += speedValue();
			else if(this.currentTile.getSurroundingTiles()[2].checkType(TileType.EMPTY, TileType.WALL) && Utility.equalsWithError(x, this.currentTile.getXinPixels(), 3))
				this.x = this.currentTile.getXinPixels();
			break;
		case DOWN:
			if(this.currentTile.getSurroundingTiles()[4].checkType(TileType.TELEPORT))
				this.y = this.currentTile.getSurroundingTiles()[4].getTeleportPortal().getYinPixels();
			else if(this.currentTile.getSurroundingTiles()[4].checkType(TileType.PATH, TileType.INTERSECTION, TileType.INTERSECTION_NO_UP, TileType.TUNNEL_PATH, TileType.GHOST_EXIT, TileType.GHOST_HOUSE) || 
					 this.y + 24 < this.currentTile.getSurroundingTiles()[4].getYinPixels())
				this.y += speedValue();
			else if(this.currentTile.getSurroundingTiles()[4].checkType(TileType.EMPTY, TileType.WALL) && Utility.equalsWithError(y, this.currentTile.getYinPixels(), 3))
				this.y = this.currentTile.getYinPixels();
			break;
		case LEFT:
			if(this.currentTile.getSurroundingTiles()[6].checkType(TileType.TELEPORT))
				this.x = this.currentTile.getSurroundingTiles()[6].getTeleportPortal().getXinPixels();
			else if(this.currentTile.getSurroundingTiles()[6].checkType(TileType.PATH, TileType.INTERSECTION, TileType.INTERSECTION_NO_UP, TileType.TUNNEL_PATH, TileType.GHOST_EXIT, TileType.GHOST_HOUSE) || 
					 this.x - 24 > this.currentTile.getSurroundingTiles()[6].getXinPixels())
				this.x -= speedValue();
			else if(this.currentTile.getSurroundingTiles()[6].checkType(TileType.EMPTY, TileType.WALL) && Utility.equalsWithError(x, this.currentTile.getXinPixels(), 3))
				this.x = this.currentTile.getXinPixels();
			break;
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
					 this.y - 24 > this.currentTile.getSurroundingTiles()[0].getYinPixels())
				this.y -= speedValue();
			else if(this.currentTile.getSurroundingTiles()[0].isBorderType() && Utility.equalsWithError(y, this.currentTile.getYinPixels(), 3))
				this.y = this.currentTile.getYinPixels();
			break;
		case RIGHT:
			if(this.currentTile.getSurroundingTiles()[2].checkType(TileType.TELEPORT))
				this.x = this.currentTile.getSurroundingTiles()[2].getTeleportPortal().getXinPixels();
			else if((this.currentTile.getSurroundingTiles()[2].isPathType()) || 
					 this.x + 24 < this.currentTile.getSurroundingTiles()[2].getXinPixels())
				this.x += speedValue();
			else if(this.currentTile.getSurroundingTiles()[2].isBorderType() && Utility.equalsWithError(x, this.currentTile.getXinPixels(), 3))
				this.x = this.currentTile.getXinPixels();
			break;
		case DOWN:
			if(this.currentTile.getSurroundingTiles()[4].checkType(TileType.TELEPORT))
				this.y = this.currentTile.getSurroundingTiles()[4].getTeleportPortal().getYinPixels();
			else if((this.currentTile.getSurroundingTiles()[4].isPathType()) || 
					 this.y + 24 < this.currentTile.getSurroundingTiles()[4].getYinPixels())
				this.y += speedValue();
			else if(this.currentTile.getSurroundingTiles()[4].isBorderType() && Utility.equalsWithError(y, this.currentTile.getYinPixels(), 3))
				this.y = this.currentTile.getYinPixels();
			break;
		case LEFT:
			if(this.currentTile.getSurroundingTiles()[6].checkType(TileType.TELEPORT))
				this.x = this.currentTile.getSurroundingTiles()[6].getTeleportPortal().getXinPixels();
			else if((this.currentTile.getSurroundingTiles()[6].isPathType()) || 
					 this.x - 24 > this.currentTile.getSurroundingTiles()[6].getXinPixels())
				this.x -= speedValue();
			else if(this.currentTile.getSurroundingTiles()[6].isBorderType() && Utility.equalsWithError(x, this.currentTile.getXinPixels(), 3))
				this.x = this.currentTile.getXinPixels();
			break;
		}
	}
	
	private double speedValue() {
		double maxSpeed;
		if(PacmanGame.difficulty == -1)
			maxSpeed = 2.5;
		else
			maxSpeed = PMConstants.GHOST_MAX_SPEED;
		
		if(mode == GhostMode.NONE)
			return 1;
		else if(currentTile.checkType(TileType.TUNNEL_PATH))
			return maxSpeed * speed[GhostSpeed.TUNNEL];
		else if(mode == GhostMode.FRIGHTENED)
			return maxSpeed * speed[GhostSpeed.FRIGHTENED];
		else if(mode == GhostMode.RETREAT)
			return maxSpeed * 2;
		else
			return maxSpeed * speed[GhostSpeed.NORMAL];
	}

	private void changeDirectionFrightened()
	{
		// Set value for direction 
		Tile tileInFront;
		switch(direction) 
		{
		case UP: tileInFront = this.currentTile.getSurroundingTiles()[0]; break;
		case RIGHT: tileInFront = this.currentTile.getSurroundingTiles()[2]; break;
		case DOWN: tileInFront = this.currentTile.getSurroundingTiles()[4]; break;
		case LEFT: default: tileInFront = this.currentTile.getSurroundingTiles()[6]; break;
		}
		
		if(tileInFront.checkType(TileType.INTERSECTION, TileType.INTERSECTION_NO_UP))
		{
			int dNum = (int) (Math.random() * 4) * 2;
			if(tileInFront.getSurroundingTiles()[dNum].isBorderType())
				if(tileInFront.getSurroundingTiles()[0].isPathType() && tileInFront.getSurroundingTiles()[0] != currentTile)
					bestDirection = Direction.UP;
				else if(tileInFront.getSurroundingTiles()[6].isPathType() && tileInFront.getSurroundingTiles()[6] != currentTile)
					bestDirection = Direction.LEFT;
				else if(tileInFront.getSurroundingTiles()[4].isPathType() && tileInFront.getSurroundingTiles()[4] != currentTile)
					bestDirection = Direction.DOWN;
				else
					bestDirection = Direction.RIGHT;
			else
				bestDirection = 
					dNum == 0 ? bestDirection = Direction.UP :
					dNum == 2 ? bestDirection = Direction.RIGHT :
					dNum == 4 ? bestDirection = Direction.DOWN :
					Direction.LEFT;
		}
		// If on intersection and turn deemed necessary, turn
		else if(this.currentTile.checkType(TileType.INTERSECTION, TileType.INTERSECTION_NO_UP))
		{
			if(bestDirection != null && bestDirection != direction)
			{
				// If going to pass center of tile after next move, set to center
				if((direction == Direction.LEFT || direction == Direction.RIGHT) && Utility.equalsWithError(this.x, this.currentTile.getXinPixels(), speedValue()))
				{
					this.x = this.currentTile.getXinPixels();
					this.direction = bestDirection;
				}
				else if((direction == Direction.UP || direction == Direction.DOWN) && Utility.equalsWithError(this.y, this.currentTile.getYinPixels(), speedValue()))
				{
					this.y = this.currentTile.getYinPixels();
					this.direction = bestDirection;
				}
			}
		}
		// If moving vertical into a corner, turn horizontally
		else if(((direction == Direction.UP && currentTile.getSurroundingTiles()[0].isBorderType()) || 
				(direction == Direction.DOWN && currentTile.getSurroundingTiles()[4].isBorderType())) &&
				Utility.equalsWithError(this.y, this.currentTile.getYinPixels(), speedValue()))
		{
			this.y = this.currentTile.getYinPixels();
			if(currentTile.getSurroundingTiles()[2].isPathType()) 
			{
				direction = Direction.RIGHT;
			}
			else if(currentTile.getSurroundingTiles()[6].isPathType()) 
			{
				direction = Direction.LEFT;
			}
		}
		// If moving horizontal into a corner, turn vertically
		else if(((direction == Direction.RIGHT && currentTile.getSurroundingTiles()[2].isBorderType()) || 
				(direction == Direction.LEFT && currentTile.getSurroundingTiles()[6].isBorderType())) &&
				Utility.equalsWithError(this.x, this.currentTile.getXinPixels(), speedValue()))
		{
			this.x = this.currentTile.getXinPixels();
			if(currentTile.getSurroundingTiles()[0].isPathType()) 
			{
				direction = Direction.UP;
			}
			else if(currentTile.getSurroundingTiles()[4].isPathType()) 
			{
				direction = Direction.DOWN;
			}
		}
	}

	private void changeDirectionInactive()
	{
		if(this.direction == Direction.UP 
		&& (this.currentTile.getSurroundingTiles()[0].isBorderType()))
			this.direction = Direction.DOWN;
		else if(this.direction == Direction.DOWN
		&& (this.currentTile.getSurroundingTiles()[4].isBorderType()))
			this.direction = Direction.UP;
	}

	private void changeDirectionDefault()
	{
		// Set value for direction 
		Tile tileInFront;
		switch(direction) 
		{
		case UP: tileInFront = this.currentTile.getSurroundingTiles()[0]; break;
		case RIGHT: tileInFront = this.currentTile.getSurroundingTiles()[2]; break;
		case DOWN: tileInFront = this.currentTile.getSurroundingTiles()[4]; break;
		case LEFT: default: tileInFront = this.currentTile.getSurroundingTiles()[6]; break;
		}
		
		if(tileInFront.checkType(TileType.INTERSECTION))
		{
			bestDirection = (tileInFront.getSurroundingTiles()[0].isPathType() && this.direction != Direction.DOWN) ? Direction.UP : 
					(tileInFront.getSurroundingTiles()[6].isPathType() && this.direction != Direction.RIGHT) ? Direction.LEFT : 
					(tileInFront.getSurroundingTiles()[4].isPathType() && this.direction != Direction.UP) ? Direction.DOWN : 
					Direction.RIGHT;
			
			if(PMConstants.DEBUG_MODE)
				System.out.print(this.toString() + " -> Starting: " + bestDirection);
			
			Tile minDistance = tileInFront.getSurroundingTiles()[0].isPathType() ? tileInFront.getSurroundingTiles()[0] : 
								tileInFront.getSurroundingTiles()[6].isPathType() ? tileInFront.getSurroundingTiles()[6] : 
								tileInFront.getSurroundingTiles()[4].isPathType() ? tileInFront.getSurroundingTiles()[4] : 
								tileInFront.getSurroundingTiles()[2];
								
			if(bestDirection == Direction.UP && this.direction != Direction.RIGHT &&
			tileInFront.getSurroundingTiles()[6].isPathType() && tileInFront.getSurroundingTiles()[6].distanceFrom(targetTile) < minDistance.distanceFrom(targetTile)) 
			{
				minDistance = tileInFront.getSurroundingTiles()[6];
				bestDirection = Direction.LEFT;
				if(PMConstants.DEBUG_MODE) 
				{
					PacmanGame.gamePanel.getGraphics().drawLine(targetTile.getXinPixels(), targetTile.getYinPixels(), tileInFront.getSurroundingTiles()[6].getXinPixels(), tileInFront.getSurroundingTiles()[6].getYinPixels());
					System.out.print(", Change to " + bestDirection);
				}
			}
			
			if((bestDirection == Direction.UP || bestDirection == Direction.LEFT) && this.direction != Direction.UP &&
			tileInFront.getSurroundingTiles()[4].isPathType() && tileInFront.getSurroundingTiles()[4].distanceFrom(targetTile) < minDistance.distanceFrom(targetTile)) 
			{
				minDistance = tileInFront.getSurroundingTiles()[4];
				bestDirection = Direction.DOWN;
				if(PMConstants.DEBUG_MODE) 
				{
					PacmanGame.gamePanel.getGraphics().drawLine(targetTile.getXinPixels(), targetTile.getYinPixels(), tileInFront.getSurroundingTiles()[4].getXinPixels(), tileInFront.getSurroundingTiles()[4].getYinPixels());
					System.out.print(", Change to " + bestDirection);
				}
			}
			
			if((bestDirection == Direction.UP || bestDirection == Direction.LEFT || bestDirection == Direction.DOWN) && this.direction != Direction.LEFT &&
			tileInFront.getSurroundingTiles()[2].isPathType() && tileInFront.getSurroundingTiles()[2].distanceFrom(targetTile) < minDistance.distanceFrom(targetTile)) 
			{
				minDistance = tileInFront.getSurroundingTiles()[2];
				bestDirection = Direction.RIGHT;
				if(PMConstants.DEBUG_MODE) 
				{
					PacmanGame.gamePanel.getGraphics().drawLine(targetTile.getXinPixels(), targetTile.getYinPixels(), tileInFront.getSurroundingTiles()[2].getXinPixels(), tileInFront.getSurroundingTiles()[2].getYinPixels());
					System.out.print(", Change to " + bestDirection);
				}
			}
			
			if(PMConstants.DEBUG_MODE)
				System.out.println();
		}
		else if(tileInFront.checkType(TileType.INTERSECTION_NO_UP))
		{
			bestDirection = (tileInFront.getSurroundingTiles()[6].isPathType() && this.direction != Direction.RIGHT) ? Direction.LEFT : 
					(tileInFront.getSurroundingTiles()[4].isPathType() && this.direction != Direction.UP) ? Direction.DOWN : 
					Direction.RIGHT;
			
			if(PMConstants.DEBUG_MODE)
				System.out.print(this.toString() + " -> Starting: " + bestDirection);
			
			Tile minDistance = tileInFront.getSurroundingTiles()[6].isPathType() ? tileInFront.getSurroundingTiles()[6] : 
							tileInFront.getSurroundingTiles()[4].isPathType() ? tileInFront.getSurroundingTiles()[4] : 
							tileInFront.getSurroundingTiles()[2];
							
			if(this.direction != Direction.RIGHT &&
			tileInFront.getSurroundingTiles()[6].isPathType() && tileInFront.getSurroundingTiles()[6].distanceFrom(targetTile) < minDistance.distanceFrom(targetTile)) 
			{
				minDistance = tileInFront.getSurroundingTiles()[6];
				bestDirection = Direction.LEFT;
				if(PMConstants.DEBUG_MODE) 
				{
					PacmanGame.gamePanel.getGraphics().drawLine(targetTile.getXinPixels(), targetTile.getYinPixels(), tileInFront.getSurroundingTiles()[6].getXinPixels(), tileInFront.getSurroundingTiles()[6].getYinPixels());
					System.out.print(", Change to " + bestDirection);
				}
			}
			
			if(bestDirection == Direction.LEFT && this.direction != Direction.UP &&
			tileInFront.getSurroundingTiles()[4].isPathType() && tileInFront.getSurroundingTiles()[4].distanceFrom(targetTile) < minDistance.distanceFrom(targetTile)) 
			{
				minDistance = tileInFront.getSurroundingTiles()[4];
				bestDirection = Direction.DOWN;
				if(PMConstants.DEBUG_MODE) 
				{
					PacmanGame.gamePanel.getGraphics().drawLine(targetTile.getXinPixels(), targetTile.getYinPixels(), tileInFront.getSurroundingTiles()[4].getXinPixels(), tileInFront.getSurroundingTiles()[4].getYinPixels());
					System.out.print(", Change to " + bestDirection);
				}
			}
			
			if((bestDirection == Direction.LEFT || bestDirection == Direction.DOWN) && this.direction != Direction.LEFT &&
			tileInFront.getSurroundingTiles()[2].isPathType() && tileInFront.getSurroundingTiles()[2].distanceFrom(targetTile) < minDistance.distanceFrom(targetTile)) 
			{
				minDistance = tileInFront.getSurroundingTiles()[2];
				bestDirection = Direction.RIGHT;
				if(PMConstants.DEBUG_MODE) 
				{
					PacmanGame.gamePanel.getGraphics().drawLine(targetTile.getXinPixels(), targetTile.getYinPixels(), tileInFront.getSurroundingTiles()[2].getXinPixels(), tileInFront.getSurroundingTiles()[2].getYinPixels());
					System.out.print(", Change to " + bestDirection);
				}
			}
			if(PMConstants.DEBUG_MODE) System.out.println();
			
		}
		// If on intersection and turn deemed necessary, turn
		else if(this.currentTile.checkType(TileType.INTERSECTION, TileType.INTERSECTION_NO_UP))
		{
			if(bestDirection != null && bestDirection != direction)
			{
				// If going to pass center of tile after next move, set to center
				if((direction == Direction.LEFT || direction == Direction.RIGHT) && Utility.equalsWithError(this.x, this.currentTile.getXinPixels(), speedValue()))
				{
					this.x = this.currentTile.getXinPixels();
					this.direction = bestDirection;
				}
				else if((direction == Direction.UP || direction == Direction.DOWN) && Utility.equalsWithError(this.y, this.currentTile.getYinPixels(), speedValue()))
				{
					this.y = this.currentTile.getYinPixels();
					this.direction = bestDirection;
				}
			}
		}
		// If moving vertical into a corner, turn horizontally
		else if(((direction == Direction.UP && currentTile.getSurroundingTiles()[0].isBorderType()) || 
				(direction == Direction.DOWN && currentTile.getSurroundingTiles()[4].isBorderType())) &&
				Utility.equalsWithError(this.y, this.currentTile.getYinPixels(), speedValue()))
		{
			this.y = this.currentTile.getYinPixels();
			if(currentTile.getSurroundingTiles()[2].isPathType()) 
			{
				direction = Direction.RIGHT;
			}
			else if(currentTile.getSurroundingTiles()[6].isPathType()) 
			{
				direction = Direction.LEFT;
			}
		}
		// If moving horizontal into a corner, turn vertically
		else if(((direction == Direction.RIGHT && currentTile.getSurroundingTiles()[2].isBorderType()) || 
				(direction == Direction.LEFT && currentTile.getSurroundingTiles()[6].isBorderType())) &&
				Utility.equalsWithError(this.x, this.currentTile.getXinPixels(), speedValue()))
		{
			this.x = this.currentTile.getXinPixels();
			if(currentTile.getSurroundingTiles()[0].isPathType()) 
			{
				direction = Direction.UP;
			}
			else if(currentTile.getSurroundingTiles()[4].isPathType()) 
			{
				direction = Direction.DOWN;
			}
		}
	}
	
	private void changeCurrentTile()
	{
		checkCollision();
		// Change current tile
		Tile oldTile = currentTile;
		Tile newTile = PacmanGame.maze.tiles[(int) (this.x - PMConstants.HORIZONTAL_MARGIN + 13) / 24][(int) (this.y - PMConstants.VERTICAL_MARGIN - 59) / 24];
		if(oldTile != newTile) 
		{
			currentTile = newTile;

			// Change mode on 
			if(mode != GhostMode.NONE && TickEvents.getFrightenedTimer() == -1 && mode != PacmanGame.gamePanel.currentGhostMode)
				modeChange();
		}
		checkCollision();
	}
	
	private void checkCollision()
	{
		if(PacmanGame.maze.player.getCurrentTile() == currentTile)
			if(mode != GhostMode.FRIGHTENED && mode != GhostMode.RETREAT)
				PacmanGame.maze.player.death(); 
			else if(mode == GhostMode.FRIGHTENED)
			{
				mode = GhostMode.RETREAT;
				PacmanGame.maze.player.eatGhost();
			}
	}
	
	private void modeChange() 
	{
		// Turn around if not going from frightened to chase/scatter
		if(!(mode == GhostMode.FRIGHTENED && 
		  (PacmanGame.gamePanel.currentGhostMode == GhostMode.CHASE || PacmanGame.gamePanel.currentGhostMode == GhostMode.SCATTER)))
			direction = 
					direction == Direction.LEFT ? Direction.RIGHT :
					direction == Direction.RIGHT ? Direction.LEFT :
					direction == Direction.UP ? Direction.DOWN :
					Direction.UP;
		
		mode = PacmanGame.gamePanel.currentGhostMode;
	}

	protected void setTargetTile(Tile targetTile)
	{
		this.targetTile = targetTile;
	}

	protected void setTargetTile(int x, int y)
	{
		this.targetTile = new Tile(x, y, TileType.EMPTY, FoodType.NONE);
	}

	public GhostMode getMode() {
		return mode;
	}

	public void setMode(GhostMode mode) {
		this.mode = mode;
	}
	
	public void reset()
	{
		mode = GhostMode.NONE;
		x = startTile.getXinPixels();
		y = startTile.getYinPixels();
		direction = Direction.UP;
		currentTile = startTile;
		if(defaultActive) timer.activate();
	}
	

	public Tile getCurrentTile() {
		return currentTile;
	}

	protected Direction getDirection() {
		return direction;
	}

	public void activate()
	{
		timer.pause();
		x = PacmanGame.maze.ghostStartTile.getXinPixels();
		y = PacmanGame.maze.ghostStartTile.getYinPixels();
		currentTile = PacmanGame.maze.ghostStartTile;
		mode = PacmanGame.gamePanel.currentGhostMode;
		direction = Direction.LEFT;
	}

	public GhostTimer getTimer() {
		return timer;
	}

	public BufferedImage[] getTextures() {
		return textures;
	}

	public Tile getTargetTile() {
		return targetTile;
	}
	
	public static void frighten()
	{
		TickEvents.startFrightenedTimer();
		for(Ghost g : PacmanGame.maze.ghosts)
			if(g.mode != GhostMode.NONE)
				g.mode = GhostMode.FRIGHTENED;
		TickEvents.pauseGhostModeTick();
	}

	public static int getFrightenedTime() {
		return frightenedTime;
	}

	public static int getFrightenedFlashes() {
		return frightenedFlashes;
	}

	public static void setFrightenedSettings(int time, int flashes)
	{
		frightenedTime = time;
		frightenedFlashes = flashes;
	}

}

class GhostSpeed 
{
	static final int NORMAL = 0;
	static final int FRIGHTENED = 1;
	static final int TUNNEL = 2;
}
