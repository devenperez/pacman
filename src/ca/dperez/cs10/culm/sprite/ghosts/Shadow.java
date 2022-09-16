package ca.dperez.cs10.culm.sprite.ghosts;

import java.awt.image.BufferedImage;

import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.tile.Tile;

public class Shadow extends Ghost {

	public Shadow(BufferedImage[] textures, Tile startTile) 
	{
		super(textures, startTile);
		
	}

	@Override
	public void chaseBehaviour() 
	{
		this.setTargetTile(PacmanGame.maze.player.getCurrentTile());
	}

	@Override
	public void scatterBehaviour() 
	{
		this.setTargetTile(26, -3);
	}

	@Override
	public void reset()
	{
		x = startTile.getXinPixels();
		y = startTile.getYinPixels();
	}
	
}
