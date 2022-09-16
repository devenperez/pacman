package ca.dperez.cs10.culm.sprite.ghosts;

import java.awt.image.BufferedImage;

import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.tile.Tile;

public class Speedy extends Ghost {

	public Speedy(BufferedImage[] textures, Tile startTile) 
	{
		super(textures, startTile, 0, true);
	}

	@Override
	public void chaseBehaviour() 
	{
		switch(PacmanGame.maze.player.getDirection())
		{
			case LEFT:
				this.setTargetTile(PacmanGame.maze.player.getCurrentTile().getX() - 4, PacmanGame.maze.player.getCurrentTile().getY());
				break;
			case RIGHT:
				this.setTargetTile(PacmanGame.maze.player.getCurrentTile().getX() + 4, PacmanGame.maze.player.getCurrentTile().getY());
				break;
			case DOWN:
				this.setTargetTile(PacmanGame.maze.player.getCurrentTile().getX(), PacmanGame.maze.player.getCurrentTile().getY() + 4);
				break;
			case UP: // **Original game bug recreated**
				this.setTargetTile(PacmanGame.maze.player.getCurrentTile().getX() - 4, PacmanGame.maze.player.getCurrentTile().getY() - 4);
				break;
		}
	}

	@Override
	public void scatterBehaviour() 
	{
		this.setTargetTile(3, -3);
	}

}
