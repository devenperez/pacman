package ca.dperez.cs10.culm;

import java.util.Iterator;

import ca.dperez.cs10.culm.sprite.Fruit;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.sprite.PointIndicator;
import ca.dperez.cs10.culm.sprite.ghosts.GhostMode;

public class TickEvents 
{
	
	private static int tickTimer = 0;
	private static int ghostModeTimer = 0;
	private static int stateTimer = -1;
	private static int frightenedTimer = -1;
	private static int deathTimer = -1;
	private static int fruitTimer = -1;
	public static final double TICKS_PER_SECOND = 1000.0 / PMConstants.RUN_DELAY;
	private static int[] modeSwitches = {7, 20, 7, 20, 5, 20, 5};
	private static boolean ghostFrightened = false;

	public static int getTime()
	{
		return tickTimer;
	}
	
	public static void addTick()
	{
		if(++tickTimer == Integer.MAX_VALUE)
			tickTimer = 0;
		
		if(stateTimer > -1)
			stateTimer++;
		
		if(frightenedTimer > -1)
			frightenedTimer--;
		
		if(deathTimer > -1)
			deathTimer--;
	}
	
	public static void spriteTick()
	{
		PacmanGame.maze.player.tick();
		for(Ghost g : PacmanGame.maze.ghosts)
			g.tick();
		for(Iterator<PointIndicator> iter = PacmanGame.gamePanel.indicators.iterator(); iter.hasNext(); )
			if(iter.next().tick() == 0)
				iter.remove();
		
	}
	
	public static void ghostModeTick()
	{	
		if(!ghostFrightened)
			if(++ghostModeTimer == (int) (timeAtSwitch(0) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.CHASE;
			else if (ghostModeTimer == (int) (timeAtSwitch(1) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.SCATTER;
			else if (ghostModeTimer == (int) (timeAtSwitch(2) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.CHASE;
			else if (ghostModeTimer == (int) (timeAtSwitch(3) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.SCATTER;
			else if (ghostModeTimer == (int) (timeAtSwitch(4) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.CHASE;
			else if (ghostModeTimer == (int) (timeAtSwitch(5) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.SCATTER;
			else if (ghostModeTimer == (int) (timeAtSwitch(6) * TICKS_PER_SECOND))
				PacmanGame.gamePanel.currentGhostMode = GhostMode.CHASE;
	}
	
	
	public static void displayTick()
	{
		PacmanGame.basePanel.repaint();
	}
	
	public static void fruitTick()
	{
		if(PacmanGame.maze.dotsEaten == 70 || PacmanGame.maze.dotsEaten == 170)
		{
			fruitTimer = (int) (9 * TICKS_PER_SECOND);
			switch(PacmanGame.maze.level)
			{
			case 1:
				new Fruit(PMConstants.FRUITS[0], 100); // Cherry
				break;
			case 2:
				new Fruit(PMConstants.FRUITS[1], 300); // Strawberry
				break;
			case 3: case 4:
				new Fruit(PMConstants.FRUITS[2], 500); // Peach
				break;
			case 5: case 6:
				new Fruit(PMConstants.FRUITS[3], 700); // Apple
				break;
			case 7: case 8:
				new Fruit(PMConstants.FRUITS[4], 1000); // Grapes
				break;
			case 9: case 10:
				new Fruit(PMConstants.FRUITS[5], 2000); // Galaxian
				break;
			case 11: case 12:
				new Fruit(PMConstants.FRUITS[6], 3000); // Bell
				break;
			default:
				new Fruit(PMConstants.FRUITS[7], 5000); // Key
				break;
			}
		}
		else if(PacmanGame.maze.fruit != null)
		{
			if(--fruitTimer == 0)
				PacmanGame.maze.fruit = null;
		}
	}

	public static void setGhostModeSwitches(int[] modeSwitches)
	{
		TickEvents.modeSwitches = modeSwitches;
	}

	private static int timeAtSwitch(int switchIndex)
	{
		int time = 0;
		for(int i = 0; i <= switchIndex; i++)
			time += modeSwitches[i];
		return time;
	}

	public static void startStateTimer() 
	{
		stateTimer++;
	}

	public static void stopStateTimer() 
	{
		stateTimer = -1;
	}

	public static int getStateTimer() 
	{
		return stateTimer;
	}

	public static void startFrightenedTimer() 
	{
		frightenedTimer = (int) (Ghost.getFrightenedTime() * TICKS_PER_SECOND);
	}

	public static void stopFrightenedTimer() 
	{
		frightenedTimer = -1;
	}

	public static int getFrightenedTimer() 
	{
		return frightenedTimer;
	}
	
	public static void pauseGhostModeTick()
	{
		ghostFrightened = true;
	}
	
	public static void runGhostModeTick()
	{
		ghostFrightened = false;
	}

	public static boolean isGhostFrightened() {
		return ghostFrightened;
	}

	public static int getDeathTimer() {
		return deathTimer;
	}

	public static void startDeathTimer() {
		TickEvents.deathTimer = 90;
	}
	
}
