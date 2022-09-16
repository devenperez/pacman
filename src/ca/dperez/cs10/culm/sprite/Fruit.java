package ca.dperez.cs10.culm.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.tile.Tile;

public class Fruit 
{
	public static Tile fruitTile;
	private BufferedImage texture;
	private int value;
	
	public Fruit(BufferedImage texture, int value)
	{
		this.texture = texture;
		this.value = value;
		PacmanGame.maze.fruit = this;
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(texture, fruitTile.getXinPixels(), fruitTile.getYinPixels(), null);
	}

	public int getValue() 
	{
		return value;
	}
}
