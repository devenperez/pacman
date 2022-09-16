package ca.dperez.cs10.culm.sprite.ghosts;

import java.awt.image.BufferedImage;

import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.tile.Tile;

public class Pokey extends Ghost {

	public Pokey(BufferedImage[] textures, Tile startTile) 
	{
		super(textures, startTile, 64, false);
		
	}

	@Override
	public void chaseBehaviour() 
	{
		if(this.getCurrentTile().distanceFrom(PacmanGame.maze.player.getCurrentTile()) > 8)
			this.setTargetTile(PacmanGame.maze.player.getCurrentTile());
		else
			scatterBehaviour();
	}

	@Override
	public void scatterBehaviour() 
	{
		this.setTargetTile(0, 32);
	}

}
