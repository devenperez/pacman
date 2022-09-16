package ca.dperez.cs10.culm.sprite.ghosts;

import ca.dperez.cs10.culm.PacmanGame;

public class GlobalGhostTimer extends GhostTimer {

	public GlobalGhostTimer() 
	{
		super(32);
	}
	
	@Override
	public boolean count() 
	{
		boolean complete = super.count();
		if(getDotsEaten() == 7)
			PacmanGame.maze.ghosts[1].activate();
		else if(getDotsEaten() == 17)
			PacmanGame.maze.ghosts[2].activate();
		else if(getDotsEaten() == 32)
			PacmanGame.maze.ghosts[3].activate();
		return complete;
	}

}
