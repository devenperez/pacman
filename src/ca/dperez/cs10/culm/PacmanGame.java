/**
 * 	Info on intended behaviors on:
 *  https://gameinternals.com/understanding-pac-man-ghost-behavior
 *  https://www.gamasutra.com/view/feature/132330/the_pacman_dossier.php
 * 
 * @author Deven Perez
 * January 22, 2020
 */

package ca.dperez.cs10.culm;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.dperez.cs10.culm.display.FrameState;
import ca.dperez.cs10.culm.display.GamePanel;
import ca.dperez.cs10.culm.display.Maze;
import ca.dperez.cs10.culm.display.PausePanel;
import ca.dperez.cs10.culm.display.StartPanel;


public class PacmanGame 
{
	
	public static JFrame frame;
	public static Maze maze;
	public static JPanel basePanel;
	public static StartPanel startPanel;
	public static GamePanel gamePanel;
	public static PausePanel pausePanel;
	private static Timer timer;
	public static int difficulty;
	private static FrameState frameState = FrameState.START;
	

	public static void main(String[] args) 
	{
		try
		{
			PMConstants.loadTextures();
			PMConstants.loadHighscore();
			PMConstants.loadFonts();
			PMConstants.loadFrightenedSettings();
			PMConstants.loadGhostSpeeds();
			PMConstants.loadPlayerSpeeds();
			PMConstants.loadLifeSettings();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		initFrame();
		
		start();
	}	
	
	public static void initFrame()
	{
		frame = new JFrame();
		frame.setIconImage(PMConstants.SPRITES[0][3]);
		frame.setTitle("Pac-Man");
		frame.setMinimumSize(new Dimension(672, 888));
		frame.setSize(672 + 2 * PMConstants.VERTICAL_MARGIN, 888 + 2 * PMConstants.HORIZONTAL_MARGIN);
		basePanel = new JPanel();
		basePanel.setLayout(new CardLayout());
		startPanel = new StartPanel();
		basePanel.add("START", startPanel);
		maze = new Maze(new File("saves/map.txt"));
		gamePanel = new GamePanel(maze);
		basePanel.add("GAME", gamePanel);
		pausePanel = new PausePanel();
		basePanel.add("PAUSE", pausePanel);
		frame.add(basePanel);

		frame.setResizable(false);
		// Save highscore on window close
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				try
				{
					(new File("saves/highscore.txt")).delete();
					FileWriter hsWriter = new FileWriter(new File("saves/highscore.txt"));
					hsWriter.write(String.valueOf(PMConstants.HIGH_SCORE));
					hsWriter.close();
					System.exit(0);
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
		});
		frame.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(frameState)
				{
				case GAME:
					gamePanel.keyListener(e);
					break;
				case START:
					startPanel.keyListener(e);
					break;
				case PAUSE:
					pausePanel.keyListener(e);
					break;
				}
			}
		});
		frame.setVisible(true);
	}
	
	public static void start()
	{
		timer = new Timer();
		timer.scheduleAtFixedRate(PMConstants.GAME_RUN_TASK, PMConstants.RUN_DELAY, PMConstants.RUN_DELAY);
	}

	public static void setScreen(FrameState state) 
	{
		((CardLayout) basePanel.getLayout()).show(basePanel, state.toString());
		frameState = state;
	}

	public static FrameState getFrameState() {
		return frameState;
	}

}
