package ca.dperez.cs10.culm.sprite.ghosts;

import java.awt.image.BufferedImage;

import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.tile.Tile;

public class Bashful extends Ghost 
{

	public Bashful(BufferedImage[] textures, Tile startTile) 
	{
		super(textures, startTile, 30, false);
	}

	@Override
	public void chaseBehaviour() 
	{
		int deltaX = PacmanGame.maze.ghosts[0].getCurrentTile().getX() - PacmanGame.maze.player.getCurrentTile().getX(); 
		int deltaY = PacmanGame.maze.ghosts[0].getCurrentTile().getY() - PacmanGame.maze.player.getCurrentTile().getY();
		
		this.setTargetTile(PacmanGame.maze.player.getCurrentTile().getX() - deltaX, PacmanGame.maze.player.getCurrentTile().getY() - deltaY);
	}

	@Override
	public void scatterBehaviour() 
	{
		this.setTargetTile(27, 32);
	}

}
