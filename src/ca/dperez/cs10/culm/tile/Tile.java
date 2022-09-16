package ca.dperez.cs10.culm.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.TickEvents;

public class Tile 
{	
	public static final Tile blankTile = new Tile(-1, -1, TileType.EMPTY, FoodType.NONE);
	private int x;
	private int y;
	private TileType type;
	private FoodType food;
	private FoodType defaultFood;
	private Tile teleportPortal;
	private boolean justEaten;
	private int powerPelletValue;
	
	public Tile(int x, int y, TileType type, FoodType food) 
	{
		this.x = x;
		this.y = y;
		this.type = type;
		this.food = food;
		this.defaultFood = food;
		this.powerPelletValue = -1;
	}
	
	public Tile(int x, int y, TileType type, int powerPelletValue) 
	{
		this.x = x;
		this.y = y;
		this.type = type;
		this.food = FoodType.POWER;
		this.defaultFood = FoodType.POWER;
		this.powerPelletValue = powerPelletValue;
	}
	
	public void draw(Graphics g)
	{
		draw((Graphics2D) g);
	}

	public void draw(Graphics2D g)
	{
		switch(type) 
		{
		case PATH: case INTERSECTION: case INTERSECTION_NO_UP: case TUNNEL_PATH:
			g.drawImage(PMConstants.TEXTURES[0], this.getXinPixels(), this.getYinPixels(), null);
			g.setColor(Color.YELLOW);
			if(food == FoodType.NORMAL)
				g.fillOval(24 * x + 10 + PMConstants.HORIZONTAL_MARGIN, 24 * (y + 3) + 10 + PMConstants.VERTICAL_MARGIN, 5, 5);
			else if(food == FoodType.POWER)
				if(TickEvents.getTime() % 40 < 20) // Flashing Power Pellets
					g.fillOval(24 * x + 5 + PMConstants.HORIZONTAL_MARGIN, 24 * (y + 3) + 5 + PMConstants.VERTICAL_MARGIN , 15, 15);
			break;
		case WALL:
			if((this.getSurroundingTiles()[0].isBorderType())
			&& (this.getSurroundingTiles()[1].isPathType())
			&& (this.getSurroundingTiles()[2].isBorderType())
			&& (this.getSurroundingTiles()[3].isBorderType())
			&& (this.getSurroundingTiles()[4].isBorderType())
			&& (this.getSurroundingTiles()[5].isBorderType())
			&& (this.getSurroundingTiles()[6].isBorderType())
			&& (this.getSurroundingTiles()[7].isBorderType()))
				g.drawImage(PMConstants.TEXTURES[3], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[0].isBorderType())
			&& (this.getSurroundingTiles()[1].isBorderType())
			&& (this.getSurroundingTiles()[2].isBorderType()) 
			&& (this.getSurroundingTiles()[3].isPathType())
			&& (this.getSurroundingTiles()[4].isBorderType())
			&& (this.getSurroundingTiles()[5].isBorderType())
			&& (this.getSurroundingTiles()[6].isBorderType())
			&& (this.getSurroundingTiles()[7].isBorderType()))
				g.drawImage(PMConstants.TEXTURES[4], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[0].isBorderType())
			&& (this.getSurroundingTiles()[1].isBorderType())
			&& (this.getSurroundingTiles()[2].isBorderType())
			&& (this.getSurroundingTiles()[3].isBorderType())
			&& (this.getSurroundingTiles()[4].isBorderType())
			&& (this.getSurroundingTiles()[5].isPathType())
			&& (this.getSurroundingTiles()[6].isBorderType())
			&& (this.getSurroundingTiles()[7].isBorderType()))
				g.drawImage(PMConstants.TEXTURES[5], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[0].isBorderType())
			&& (this.getSurroundingTiles()[1].isBorderType())
			&& (this.getSurroundingTiles()[2].isBorderType())
			&& (this.getSurroundingTiles()[3].isBorderType())
			&& (this.getSurroundingTiles()[4].isBorderType())
			&& (this.getSurroundingTiles()[5].isBorderType())
			&& (this.getSurroundingTiles()[6].isBorderType())
			&& (this.getSurroundingTiles()[7].isPathType()))
				g.drawImage(PMConstants.TEXTURES[6], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[2].isBorderType()) 
				&& (this.getSurroundingTiles()[6].isBorderType()))
				g.drawImage(PMConstants.TEXTURES[1], this.getXinPixels(), this.getYinPixels(), null);
			else if(this.getSurroundingTiles()[0].isBorderType()
				&& this.getSurroundingTiles()[4].isBorderType())
				g.drawImage(PMConstants.TEXTURES[2], this.getXinPixels(), this.getYinPixels(), null);
			else if(this.getSurroundingTiles()[0].isBorderType()
				&& this.getSurroundingTiles()[2].isBorderType())
				g.drawImage(PMConstants.TEXTURES[3], this.getXinPixels(), this.getYinPixels(), null);
			else if(this.getSurroundingTiles()[2].isBorderType() 
				&& this.getSurroundingTiles()[4].isBorderType())
				g.drawImage(PMConstants.TEXTURES[4], this.getXinPixels(), this.getYinPixels(), null);
			else if(this.getSurroundingTiles()[4].isBorderType()
				&& this.getSurroundingTiles()[6].isBorderType())
				g.drawImage(PMConstants.TEXTURES[5], this.getXinPixels(), this.getYinPixels(), null);
			else if(this.getSurroundingTiles()[6].isBorderType() 
				&& this.getSurroundingTiles()[0].isBorderType())
				g.drawImage(PMConstants.TEXTURES[6], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[0].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT))
			&& (this.getSurroundingTiles()[1].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT))
			&& (this.getSurroundingTiles()[2].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[3].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[4].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[5].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[6].checkType(TileType.WALL))
			&& (this.getSurroundingTiles()[7].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)))
				g.drawImage(PMConstants.TEXTURES[1], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[0].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[1].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT))
			&& (this.getSurroundingTiles()[2].checkType(TileType.WALL))
			&& (this.getSurroundingTiles()[3].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[4].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[5].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[6].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)) 
			&& (this.getSurroundingTiles()[7].checkType(TileType.PATH, TileType.EMPTY, TileType.TELEPORT)))
				g.drawImage(PMConstants.TEXTURES[1], this.getXinPixels(), this.getYinPixels(), null);
			break;
		case GHOST_EXIT:
			if((this.getSurroundingTiles()[2].checkType(TileType.GHOST_EXIT)) 
			&& (this.getSurroundingTiles()[6].checkType(TileType.WALL)))
				g.drawImage(PMConstants.TEXTURES[7], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[2].checkType(TileType.GHOST_EXIT))
			&& (this.getSurroundingTiles()[6].checkType(TileType.GHOST_EXIT)))
				g.drawImage(PMConstants.TEXTURES[8], this.getXinPixels(), this.getYinPixels(), null);
			else if((this.getSurroundingTiles()[2].checkType(TileType.WALL))
			&& (this.getSurroundingTiles()[6].checkType(TileType.GHOST_EXIT)))
				g.drawImage(PMConstants.TEXTURES[9], this.getXinPixels(), this.getYinPixels(), null);
			break;
		case EMPTY:	case TELEPORT:
			g.drawImage(PMConstants.TEXTURES[0], this.getXinPixels(), this.getYinPixels(), null);
			break;
		default:
			break;
			
		}
		
		
//		g.setColor(Color.RED);
//		g.drawRect(24 * x + PMConstants.HORIZONTAL_MARGIN, 24 * y + PMConstants.VERTICAL_MARGIN, 24, 24);
//		g.drawString(x + ", " + y, 24 * x + PMConstants.HORIZONTAL_MARGIN, 24 * y + PMConstants.VERTICAL_MARGIN);
	}
	
	public FoodType getFood() {
		return food;
	}

	public void eatFood() {
		food = FoodType.NONE;
	}
	
	public TileType getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getXinPixels() {
		return 24 * x + PMConstants.HORIZONTAL_MARGIN;
	}

	public int getYinPixels() {
		return 24 * (y + 3) + PMConstants.VERTICAL_MARGIN;
	}

	public double distanceFrom(Tile target)
	{
		return Math.sqrt(Math.pow(this.x - target.x, 2) + Math.pow(this.y - target.y, 2));
	}
	
	@Override
	public String toString() 
	{
		return type.toString() + " @ " + x + ", " + y; 
	}
	
	public Tile[] getSurroundingTiles() 
	{
		final int maxX = PacmanGame.maze.tiles.length - 1;
		final int maxY = PacmanGame.maze.tiles[0].length - 1;
		final int tileX = this.x;
		final int tileY = this.y;
		Tile[] surroundingTiles = new Tile[8];
		surroundingTiles[0] = (tileY > 0) ? PacmanGame.maze.tiles[tileX][tileY - 1] : blankTile;
		surroundingTiles[1] = (tileX < maxX && tileY > 0) ? PacmanGame.maze.tiles[tileX + 1][tileY - 1] : blankTile;
		surroundingTiles[2] = (tileX < maxX) ? PacmanGame.maze.tiles[tileX + 1][tileY] : blankTile;
		surroundingTiles[3] = (tileX < maxX && tileY < maxY) ? PacmanGame.maze.tiles[tileX + 1][tileY + 1] : blankTile;
		surroundingTiles[4] = (tileY < maxY) ? PacmanGame.maze.tiles[tileX][tileY + 1] : blankTile;
		surroundingTiles[5] = (tileX > 0 && tileY < maxY) ? PacmanGame.maze.tiles[tileX - 1][tileY + 1] : blankTile;
		surroundingTiles[6] = (tileX > 0) ? PacmanGame.maze.tiles[tileX - 1][tileY] : blankTile;
		surroundingTiles[7] = (tileX > 0 && tileY > 0) ? PacmanGame.maze.tiles[tileX - 1][tileY - 1] : blankTile;
		return surroundingTiles;
		
	}
	
	public void setTeleportPortal(Tile teleportPortal)
	{
		if(this.type == TileType.TELEPORT)
			this.teleportPortal = teleportPortal;
	}

	public Tile getTeleportPortal() 
	{
		return teleportPortal;
	}
	
	public boolean checkType(TileType...tileTypes)
	{
		for(TileType type : tileTypes)
			if(this.type == type)
				return true;
		return false;	
	}
	
	public boolean isBorderType()
	{
		return checkType(TileType.EMPTY, TileType.GHOST_EXIT, TileType.WALL);
	}
	
	public boolean isPathType()
	{
		return checkType(TileType.PATH, TileType.INTERSECTION, TileType.INTERSECTION_NO_UP, TileType.TELEPORT, TileType.TUNNEL_PATH, TileType.GHOST_HOUSE);
	}

	public boolean isJustEaten() 
	{
		return justEaten;
	}

	public void setJustEaten(boolean justEaten) 
	{
		this.justEaten = justEaten;
	}
	
	public void reset()
	{
		food = defaultFood;
		justEaten = false;
	}

	public int getPowerPelletValue() {
		return powerPelletValue;
	}
}


