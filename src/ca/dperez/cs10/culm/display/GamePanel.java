package ca.dperez.cs10.culm.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import ca.dperez.cs10.culm.GameState;
import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.Utility;
import ca.dperez.cs10.culm.sprite.Direction;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.sprite.PointIndicator;
import ca.dperez.cs10.culm.sprite.ghosts.GhostMode;
import ca.dperez.cs10.culm.tile.FoodType;
import ca.dperez.cs10.culm.tile.Tile;
import ca.dperez.cs10.culm.tile.TileType;

public class GamePanel extends JPanel {

	private Tile[][] tiles = new Tile[28][36];
	private Maze maze;
	public ArrayList<PointIndicator> indicators = new ArrayList<PointIndicator>();

	private GameState gameState = GameState.STARTING;
	public GhostMode currentGhostMode = GhostMode.SCATTER;
	
	public GamePanel(Maze maze) 
	{
		this.maze = maze;
		for(int i = 0; i < maze.tiles.length; i++)
			for(int j = 0; j < maze.tiles[i].length; j++)
				tiles[i][j + 3] = maze.tiles[i][j];
		for(int i = 0; i < tiles.length; i++)
			for(int j = 0; j < tiles[i].length; j++)
				if(tiles[i][j] == null)
					tiles[i][j] = new Tile(i, j - 3, TileType.EMPTY, FoodType.NONE);
		
		
		
	}
	
	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;
		
		// Fills black background
		g.setColor(Color.BLACK);
		g.fillRect(-1, -1, getWidth() + 3, getHeight() + 3);
		
		// **MAZE AND SPRITES**
		for(int i = 0; i < tiles.length; i++) 
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				// Draw all tiles
				tiles[i][j].draw(g);
			}
		}
		// Draw player (Pacman)
		maze.player.draw(g);
		if(this.getGameState() != GameState.DEATH)
		{
			// Draw all ghosts
			for(Ghost ghost : maze.ghosts)
			{
				if(this.getGameState() != GameState.GAME_OVER)
					ghost.draw(g);
				if(PMConstants.DEBUG_MODE)
					if (this.getGameState() == GameState.RUNNING && ghost.getTargetTile() != null)
						g.drawImage(ghost.getTextures()[4], ghost.getTargetTile().getXinPixels(), ghost.getTargetTile().getYinPixels(), null);
			}
		}
		
		// "Ready" text
		if(this.getGameState() == GameState.STARTING)
		{
			g.setColor(Color.YELLOW);
			g.setFont(PMConstants.FONT.deriveFont(20.0f));
			Utility.drawCenteredString(g, "READY!", PacmanGame.frame.getWidth() / 2, 500 + PMConstants.VERTICAL_MARGIN);
			g.setFont(PMConstants.FONT);
		}
		else if(this.getGameState() == GameState.GAME_OVER)
		{
			g.setColor(Color.RED);
			g.setFont(PMConstants.FONT.deriveFont(20.0f));
			Utility.drawCenteredString(g, "GAME OVER", PacmanGame.frame.getWidth() / 2, 500 + PMConstants.VERTICAL_MARGIN);
			g.setFont(PMConstants.FONT);
		}
		
		// Fruit
		if(PacmanGame.maze.fruit != null && this.getGameState() == GameState.RUNNING)
			PacmanGame.maze.fruit.draw(g);
		
		// Point Indicators
		for(PointIndicator pointIndicator : indicators)
			pointIndicator.draw(g);
		
		// **SCORES**
		// Prints score
		g.setFont(PMConstants.FONT);
		g.setColor(Color.RED);
		g.drawString("1UP", 32 + PMConstants.HORIZONTAL_MARGIN, 32 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.WHITE);
		g.drawString(Utility.scoreFormatter(maze.player.getScore()), 32 + PMConstants.HORIZONTAL_MARGIN, 64 + PMConstants.VERTICAL_MARGIN);
		// Prints highscore
		g.setColor(Color.RED);
		Utility.drawCenteredString(g, "HIGH SCORE", PacmanGame.frame.getWidth() / 2, 32 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.WHITE);
		Utility.drawCenteredString(g, Utility.scoreFormatter(PMConstants.HIGH_SCORE), PacmanGame.frame.getWidth() / 2, 64 + PMConstants.VERTICAL_MARGIN);
		
		// Prints Level Number
		g.setColor(Color.YELLOW);
		Utility.drawCenteredString(g, String.valueOf(PacmanGame.maze.level), PacmanGame.frame.getWidth() / 2, 845 + PMConstants.VERTICAL_MARGIN);
		
		// **LIVES**
		g.setColor(Color.YELLOW);
		if(PacmanGame.maze.player.getLives() > 0) g.fillArc( 63 + PMConstants.HORIZONTAL_MARGIN, 824 + PMConstants.VERTICAL_MARGIN, 25, 25, 215, 290);
		if(PacmanGame.maze.player.getLives() > 1) g.fillArc( 99 + PMConstants.HORIZONTAL_MARGIN, 824 + PMConstants.VERTICAL_MARGIN, 25, 25, 215, 290);
		if(PacmanGame.maze.player.getLives() > 2) g.fillArc(135 + PMConstants.HORIZONTAL_MARGIN, 824 + PMConstants.VERTICAL_MARGIN, 25, 25, 215, 290);
		if(PacmanGame.maze.player.getLives() > 3) g.fillArc(171 + PMConstants.HORIZONTAL_MARGIN, 824 + PMConstants.VERTICAL_MARGIN, 25, 25, 215, 290);
		if(PacmanGame.maze.player.getLives() > 4)
		{
			g.setFont(PMConstants.FONT.deriveFont(20.0f));
			g.drawString("+" + (PacmanGame.maze.player.getLives() - 4) , 207 + PMConstants.HORIZONTAL_MARGIN, 845 + PMConstants.VERTICAL_MARGIN);
		}
	}
	
	public void keyListener(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:	case KeyEvent.VK_A:
			maze.player.queueDirectionChange(Direction.LEFT);
			break;
		case KeyEvent.VK_RIGHT:	case KeyEvent.VK_D:
			maze.player.queueDirectionChange(Direction.RIGHT);
			break;
		case KeyEvent.VK_UP:	case KeyEvent.VK_W:
			maze.player.queueDirectionChange(Direction.UP);
			break;
		case KeyEvent.VK_DOWN:	case KeyEvent.VK_S:
			maze.player.queueDirectionChange(Direction.DOWN);
			break;
		case KeyEvent.VK_ESCAPE:
			PacmanGame.setScreen(FrameState.PAUSE);
			break;
		}
	}
	
	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
