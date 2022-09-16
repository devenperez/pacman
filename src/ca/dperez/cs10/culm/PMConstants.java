package ca.dperez.cs10.culm;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;

import ca.dperez.cs10.culm.display.FrameState;
import ca.dperez.cs10.culm.sound.PlaySound;

public class PMConstants 
{
	public static boolean DEBUG_MODE = false;
	public static int RUN_DELAY = 16;
	public static int VERTICAL_MARGIN = 10;
	public static int HORIZONTAL_MARGIN = 10;
	public static int EXTRA_LIFE_POINTS;
	public static int STARTING_LIVES;
	public static double GHOST_MAX_SPEED;
	public static double PACMAN_MAX_SPEED;
	public static BufferedImage[] TEXTURES;
	public static BufferedImage[] FRUITS;
	public static BufferedImage[][] SPRITES;
	public static AudioInputStream[] SOUND_BITES;
	public static BufferedImage NAMCO_LOGO;
	public static Font FONT;
	public static Font LOGO_FONT;
	public static int HIGH_SCORE;
	public static double[][] GHOST_SPEED;
	public static double[][] PACMAN_SPEED;
	public static int[][] FRIGHTENED;
	public static TimerTask GAME_RUN_TASK = new TimerTask() {
		
		@Override
		public void run() {
			TickEvents.displayTick();
			if(PacmanGame.getFrameState() == FrameState.GAME)
			{
				TickEvents.addTick();
				switch(PacmanGame.gamePanel.getGameState())
				{
				case RUNNING:
					if(!PacmanGame.maze.isLevelOver()) 
					{
						if(TickEvents.getFrightenedTimer() == -1)
							TickEvents.runGhostModeTick();
						TickEvents.spriteTick();
						TickEvents.ghostModeTick();
						TickEvents.displayTick();
						TickEvents.fruitTick();
					}
					else
					{
						PacmanGame.maze.nextLevel();
					}
					break;
				case STARTING:
					if(TickEvents.getStateTimer() == -1) 
					{
						TickEvents.startStateTimer();
						new PlaySound("beginning").play();
					}
					else if(TickEvents.getStateTimer() == (int) (4.2 * TickEvents.TICKS_PER_SECOND))
					{
						TickEvents.stopStateTimer();
						PacmanGame.gamePanel.setGameState(GameState.RUNNING);
					}
					TickEvents.displayTick();
					break;
				case DEATH:
					TickEvents.displayTick();
					if(TickEvents.getDeathTimer() == 0)
						PacmanGame.maze.player.loseLife();
					break;
				case GAME_OVER:
					if(TickEvents.getStateTimer() == -1) 
					{
						TickEvents.startStateTimer();
					}
					else if(TickEvents.getStateTimer() == (int) (4.2 * TickEvents.TICKS_PER_SECOND))
					{
						TickEvents.stopStateTimer();
						PacmanGame.setScreen(FrameState.START);
					}
					TickEvents.displayTick();
					break;
				}
			}
		}
	};

	public static void loadLifeSettings() throws FileNotFoundException 
	{
		Scanner lifeScan = new Scanner(new File("settings/life-settings.txt"));
		STARTING_LIVES = Integer.valueOf(lifeScan.nextLine());
		EXTRA_LIFE_POINTS = Integer.valueOf(lifeScan.nextLine());
		lifeScan.close();
		
	}

	public static void loadPlayerSpeeds() throws FileNotFoundException 
	{
		Scanner pSpeedScan = new Scanner(new File("settings/player-speed.txt"));
		PACMAN_MAX_SPEED = Double.valueOf(pSpeedScan.nextLine());
		PACMAN_SPEED = new double[4][4];
		for(int i = 0; i < 4; i++)
		{
			String[] speedChunk = pSpeedScan.nextLine().split(",");
			for(int j = 0; j < 4; j++)
				PACMAN_SPEED[i][j] = Double.valueOf(speedChunk[j]);
		}
		pSpeedScan.close();
	}

	public static void loadGhostSpeeds() throws FileNotFoundException 
	{
		Scanner gSpeedScan = new Scanner(new File("settings/ghost-speed.txt"));
		GHOST_MAX_SPEED = Double.valueOf(gSpeedScan.nextLine());
		GHOST_SPEED = new double[4][3];
		for(int i = 0; i < 4; i++)
		{
			String[] speedChunk = gSpeedScan.nextLine().split(",");
			for(int j = 0; j < 3; j++)
				GHOST_SPEED[i][j] = Double.valueOf(speedChunk[j]);
		}
		gSpeedScan.close();
	}

	public static void loadFrightenedSettings() throws FileNotFoundException 
	{
		Scanner frightScan = new Scanner(new File("settings/ghost-frightened.txt"));
		FRIGHTENED = new int[19][2];
		for(int i = 0; i < 19; i++)
		{
			String[] speedChunk = frightScan.nextLine().split(",");
			for(int j = 0; j < 2; j++)
				FRIGHTENED[i][j] = Integer.valueOf(speedChunk[j]);
		}
		frightScan.close();
	}

	public static void loadTextures() throws IOException
	{
		TEXTURES = new BufferedImage[] {
			ImageIO.read(new File("assets/blank.png")),
			ImageIO.read(new File("assets/wall/edge-hori.png")),
			ImageIO.read(new File("assets/wall/edge-vert.png")),
			ImageIO.read(new File("assets/wall/corner-tr.png")),
			ImageIO.read(new File("assets/wall/corner-rb.png")),
			ImageIO.read(new File("assets/wall/corner-bl.png")),
			ImageIO.read(new File("assets/wall/corner-lt.png")),
			ImageIO.read(new File("assets/wall/ghost-exit-left.png")),
			ImageIO.read(new File("assets/wall/ghost-exit-center.png")),
			ImageIO.read(new File("assets/wall/ghost-exit-right.png")),
			ImageIO.read(new File("assets/volume-off.png")),
			ImageIO.read(new File("assets/volume-on.png"))
		};	
		
		
		FRUITS = new BufferedImage[] {
				ImageIO.read(new File("assets/fruits/cherry.png")),
				ImageIO.read(new File("assets/fruits/strawberry.png")),
				ImageIO.read(new File("assets/fruits/peach.png")),
				ImageIO.read(new File("assets/fruits/apple.png")),
				ImageIO.read(new File("assets/fruits/grapes.png")),
				ImageIO.read(new File("assets/fruits/galaxian.png")),
				ImageIO.read(new File("assets/fruits/bell.png")),
				ImageIO.read(new File("assets/fruits/key.png"))
			};
		
		SPRITES = new BufferedImage[][] {
			{	
				ImageIO.read(new File("assets/sprites/shadow-up.png")),
				ImageIO.read(new File("assets/sprites/shadow-left.png")),
				ImageIO.read(new File("assets/sprites/shadow-down.png")),
				ImageIO.read(new File("assets/sprites/shadow-right.png")),
				ImageIO.read(new File("assets/shadow-target-tile.png"))
			},
		
			{
				ImageIO.read(new File("assets/sprites/speedy-up.png")),
				ImageIO.read(new File("assets/sprites/speedy-left.png")),
				ImageIO.read(new File("assets/sprites/speedy-down.png")),
				ImageIO.read(new File("assets/sprites/speedy-right.png")),
				ImageIO.read(new File("assets/speedy-target-tile.png"))
			},
		
			{
				ImageIO.read(new File("assets/sprites/bashful-up.png")),
				ImageIO.read(new File("assets/sprites/bashful-left.png")),
				ImageIO.read(new File("assets/sprites/bashful-down.png")),
				ImageIO.read(new File("assets/sprites/bashful-right.png")),
				ImageIO.read(new File("assets/bashful-target-tile.png"))
			},
		
			{
				ImageIO.read(new File("assets/sprites/pokey-up.png")),
				ImageIO.read(new File("assets/sprites/pokey-left.png")),
				ImageIO.read(new File("assets/sprites/pokey-down.png")),
				ImageIO.read(new File("assets/sprites/pokey-right.png")),
				ImageIO.read(new File("assets/pokey-target-tile.png"))
			},
		
			{
				ImageIO.read(new File("assets/sprites/eyes-up.png")),
				ImageIO.read(new File("assets/sprites/eyes-left.png")),
				ImageIO.read(new File("assets/sprites/eyes-down.png")),
				ImageIO.read(new File("assets/sprites/eyes-right.png"))
			},
		
			{
				ImageIO.read(new File("assets/sprites/frightened.png")),
				ImageIO.read(new File("assets/sprites/frightened-flash.png"))
			}
		
		};
		
		NAMCO_LOGO = ImageIO.read(new File("assets/namco-logo.png"));
	}
	
	public static void loadHighscore()
	{
		try 
		{
			Scanner hsRead = new Scanner(new File("saves/highscore.txt"));
			HIGH_SCORE = Integer.parseInt(hsRead.nextLine());
			hsRead.close();
		} 
		catch (FileNotFoundException e) 
		{
			HIGH_SCORE = 0;
		}
	}
	
	public static void loadFonts() throws FontFormatException, IOException
	{
		FONT = Font.createFont(Font.TRUETYPE_FONT, new File("assets/ingame-font.ttf"));
		FONT = FONT.deriveFont(17.0f);
		
		LOGO_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("assets/logo-font.ttf"));
		LOGO_FONT = LOGO_FONT.deriveFont(64.0f);
	}

}
