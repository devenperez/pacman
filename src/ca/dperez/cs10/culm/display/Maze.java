package ca.dperez.cs10.culm.display;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import ca.dperez.cs10.culm.GameState;
import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.sprite.Fruit;
import ca.dperez.cs10.culm.sprite.Ghost;
import ca.dperez.cs10.culm.sprite.Pacman;
import ca.dperez.cs10.culm.sprite.ghosts.Bashful;
import ca.dperez.cs10.culm.sprite.ghosts.GhostTimer;
import ca.dperez.cs10.culm.sprite.ghosts.GlobalGhostTimer;
import ca.dperez.cs10.culm.sprite.ghosts.Pokey;
import ca.dperez.cs10.culm.sprite.ghosts.Shadow;
import ca.dperez.cs10.culm.sprite.ghosts.Speedy;
import ca.dperez.cs10.culm.tile.FoodType;
import ca.dperez.cs10.culm.tile.Tile;
import ca.dperez.cs10.culm.tile.TileType;

public class Maze
{
	public Tile ghostStartTile;
	public Tile ghostExitTile;
	public Tile[][] tiles;
	public Pacman player;
	public Ghost[] ghosts;
	public GhostTimer globalTimer;
	public int level;
	public int dotsEaten;
	public Fruit fruit;
	
	
	public Maze(File mazeFile)
	{
		level = 1;
		
		checkMazeFile(mazeFile);
		setMaze(mazeFile);
		
		// Setting ghosts
		ghosts = new Ghost[] {
			new Shadow(PMConstants.SPRITES[0], ghostStartTile),
			new Speedy(PMConstants.SPRITES[1], tiles[12][14]),
			new Bashful(PMConstants.SPRITES[2], tiles[13][15]),
			new Pokey(PMConstants.SPRITES[3], tiles[14][14])
		};
		
		globalTimer = new GlobalGhostTimer();
		
	}
	
	private boolean setMaze(File mazeFile)
	{
		tiles = new Tile[28][31];
		// Sets map from txt file
		try 
		{
			int powerPelletValue = 0;
			Scanner in = new Scanner(mazeFile);
			int y = -1;
			Tile[] teleportStore = new Tile[4];
			while(in.hasNextLine()) {
				y++;
				String row = in.nextLine();
				int x = -1;
				for(char tile : row.toCharArray()) {
					x++;
					switch(tile) {
					case '#':	case '@': // Walls of maze and ghost house
						tiles[x][y] = new Tile(x, y, TileType.WALL, FoodType.NONE);
						break;
					case '^': // Ghost house exit tiles
						tiles[x][y] = new Tile(x, y, TileType.GHOST_EXIT, FoodType.NONE);
						break;
					case 'E': // Ghost house exit tiles 
						tiles[x][y] = new Tile(x, y, TileType.GHOST_EXIT, FoodType.NONE);
						ghostExitTile = tiles[x][y];
						break;
					case ' ': // Normal paths (Only 2 exits)
						tiles[x][y] = new Tile(x, y, TileType.PATH, FoodType.NORMAL);
						break;
					case 'm': // Tunnel paths (Gives Ghosts speed penalty)
						tiles[x][y] = new Tile(x, y, TileType.TUNNEL_PATH, FoodType.NONE);
						break;
					case '*': // Power pellet space
						tiles[x][y] = new Tile(x, y, TileType.PATH, ++powerPelletValue);
						break;
					case ',': // Normal path w/o food pellet
						tiles[x][y] = new Tile(x, y, TileType.PATH, FoodType.NONE);
						break;
					case 'F': // Fruit Tile
						tiles[x][y] = new Tile(x, y, TileType.PATH, FoodType.NONE);
						Fruit.fruitTile = tiles[x][y];
						break;
					case 'h': // Inside ghost house
						tiles[x][y] = new Tile(x, y, TileType.GHOST_HOUSE, FoodType.NONE);
						break;
					case 'S': // Player starting position
						tiles[x][y] = new Tile(x, y, TileType.PATH, FoodType.NONE);
						player = new Pacman(tiles[x][y]);
						break;
					case 'G': // Ghost starting tile (for after they're active)
						tiles[x][y] = new Tile(x, y, TileType.PATH, FoodType.NONE);
						ghostStartTile = tiles[x][y];
						break;
					case '+': // Intersection tile (3+ exits)
						tiles[x][y] = new Tile(x, y, TileType.INTERSECTION, FoodType.NORMAL);
						break;
					case 'T': // Intersection tile w/o food pellet
						tiles[x][y] = new Tile(x, y, TileType.INTERSECTION, FoodType.NONE);
						break;
					case 'U': // Special intersection tiles where ghosts cannot travel up
						tiles[x][y] = new Tile(x, y, TileType.INTERSECTION_NO_UP, FoodType.NORMAL);
						break;
					case 'u': // Special intersection tiles where ghosts cannot travel up w/o food pellet
						tiles[x][y] = new Tile(x, y, TileType.INTERSECTION_NO_UP, FoodType.NONE);
						break;
					case '1':	case '2':	case '3': // Teleport tiles
						tiles[x][y] = new Tile(x, y, TileType.TELEPORT, FoodType.NONE);
						// Stores tile if pair has not been stored
						if(teleportStore[Integer.parseInt(tile + "")] == null) 
						{
							teleportStore[Integer.parseInt(tile + "")] = tiles[x][y];
						}
						else // Otherwise, pairs tiles together
						{
							tiles[x][y].setTeleportPortal(teleportStore[Integer.parseInt(tile + "")]);
							teleportStore[Integer.parseInt(tile + "")].setTeleportPortal(tiles[x][y]);
						}
						break;
					case '-': 	default: // Empty/Unreachable Tiles
						tiles[x][y] = new Tile(x, y, TileType.EMPTY, FoodType.NONE);
						break;
					}
				}
				
			}
			in.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return true;
	}

	private void checkMazeFile(File mazeFile)
	{
		// Valid map.txt Checks
		try
		{
		Scanner in = new Scanner(mazeFile);
		int lines = 0;
		while(in.hasNextLine()) { 
			lines++;
			int lineLength = in.nextLine().length();
			if(lineLength != 28)
				throw new InvalidMazeConfigException("width", lineLength, lines);
		}
		if(lines != 31)
			throw new InvalidMazeConfigException("length", lines);
		}
		catch(FileNotFoundException | InvalidMazeConfigException e)
		{
			// Create Default Maze
			System.err.println("Maze.txt NOT FOUND.");
			System.out.println("Default Maze.txt in creation...");
			try
			{
				FileWriter mazeWriter = new FileWriter(mazeFile);
				mazeWriter.write("############################\n#     +      ##      +     #\n# #### ##### ## ##### #### #\n#*#--# #---# ## #---# #--#*#\n# #### ##### ## ##### #### #\n#+    +     +  +     +    +#\n# #### ## ######## ## #### #\n# #### ## ######## ## #### #\n#     +##    ##    ##+     #\n###### #####,##,##### ######\n-----# #####,##,##### #-----\n-----# ##,,,u,Gu,,,## #-----\n-----# ##,@@^^^^@@,## #-----\n-##### ##,@,,,,,,@,## #####-\n1mmmmm+,,T@,,,,,,@T,,+mmmmm1\n-##### ##,@,,,,,,@,## #####-\n-----# ##,@@@@@@@@,## #-----\n-----# ##T,,,,,,,,T## #-----\n-----# ##,########,## #-----\n###### ##,########,## ######\n#     +  +   ##   +  +     #\n# #### ##### ## ##### #### #\n# #### ##### ## ##### #### #\n#*  ##+     U,SU     +##  *#\n### ## ## ######## ## ## ###\n### ## ## ######## ## ## ###\n#  +   ##    ##    ##   +  #\n# ########## ## ########## #\n# ########## ## ########## #\n#           +  +           #\n############################");
				mazeWriter.close();
			}
			catch(IOException ex)
			{
				System.err.println("Maze.txt unable to create.");
				ex.printStackTrace();
			}
		}
	}

	public void nextLevel()
	{
		level++;
		dotsEaten = 0;

		for(int i = 0; i < tiles.length; i++)
			for(int j = 0; j < tiles[i].length; j++)
				tiles[i][j].reset();
		reset();
		
		// Reset Ghosts
		for(Ghost ghost : ghosts)
		{
			if(!(ghost instanceof Shadow)) 
				ghost.getTimer().reset();
			else
				ghost.setMode(PacmanGame.gamePanel.currentGhostMode);
		}
		globalTimer.reset();
		globalTimer.pause();

		// Change difficulty settings
		switch(level)
		{
		case 1:
			Ghost.speed = PMConstants.GHOST_SPEED[0];
			player.setSpeed(PMConstants.PACMAN_SPEED[0]);
			ghosts[1].getTimer().setLimit(0);
			ghosts[2].getTimer().setLimit(30);
			ghosts[3].getTimer().setLimit(64);
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[0][0], PMConstants.FRIGHTENED[0][1]);
			break;
		case 2:
			Ghost.speed = PMConstants.GHOST_SPEED[1];
			player.setSpeed(PMConstants.PACMAN_SPEED[1]);
			ghosts[2].getTimer().setLimit(0);
			ghosts[3].getTimer().setLimit(50);
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[1][0], PMConstants.FRIGHTENED[1][1]);
			break;
		case 3:
			ghosts[3].getTimer().setLimit(0);
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[2][0], PMConstants.FRIGHTENED[2][1]);
			break;
		case 4:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[3][0], PMConstants.FRIGHTENED[3][1]);
			break;
		case 5:
			Ghost.speed = PMConstants.GHOST_SPEED[2];
			player.setSpeed(PMConstants.PACMAN_SPEED[2]);
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[4][0], PMConstants.FRIGHTENED[4][1]);
			break;
		case 6:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[5][0], PMConstants.FRIGHTENED[5][1]);
			break;
		case 7:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[6][0], PMConstants.FRIGHTENED[6][1]);
			break;
		case 8:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[7][0], PMConstants.FRIGHTENED[7][1]);
			break;
		case 9:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[8][0], PMConstants.FRIGHTENED[8][1]);
			break;
		case 10:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[9][0], PMConstants.FRIGHTENED[9][1]);
			break;
		case 11:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[10][0], PMConstants.FRIGHTENED[10][1]);
			break;
		case 12:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[11][0], PMConstants.FRIGHTENED[11][1]);
			break;
		case 13:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[12][0], PMConstants.FRIGHTENED[12][1]);
			break;
		case 14:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[13][0], PMConstants.FRIGHTENED[13][1]);
			break;
		case 15:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[14][0], PMConstants.FRIGHTENED[14][1]);
			break;
		case 16:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[15][0], PMConstants.FRIGHTENED[15][1]);
			break;
		case 17:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[16][0], PMConstants.FRIGHTENED[16][1]);
			break;
		case 18:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[17][0], PMConstants.FRIGHTENED[17][1]);
			break;
		case 19:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[18][0], PMConstants.FRIGHTENED[18][1]);
			break;
		case 20:
			Ghost.setFrightenedSettings(PMConstants.FRIGHTENED[19][0], PMConstants.FRIGHTENED[19][1]);
			break;
		case 21:
			Ghost.speed = PMConstants.GHOST_SPEED[3];
			player.setSpeed(PMConstants.PACMAN_SPEED[3]);
			break;
		}
		
		PacmanGame.gamePanel.setGameState(GameState.STARTING);
		
	}
	
	public boolean isLevelOver()
	{
		// Checks that NO tiles have food
		for(int i = 0; i < tiles.length; i++)
			for(int j = 0; j < tiles[i].length; j++)
				if(tiles[i][j].getFood() != FoodType.NONE)
					return false;
		return true;
	}

	public void reset() 
	{
		// Runs the reset method for the player and all the ghosts
		player.reset();
		for(Ghost ghost : ghosts)
			ghost.reset();
		
		fruit = null;
	}
	
	static class InvalidMazeConfigException extends Exception {
		public InvalidMazeConfigException(String cause, int... givenValues) {
			super("Invalid maze.txt file." + 
					(cause.equalsIgnoreCase("width") ? "Invalid width. Given " + givenValues[0] + " @ line " + givenValues[1] + ". Expected 28.":
					cause.equalsIgnoreCase("length") ? "Invalid length. Given " + givenValues[0] + ". Expected 31.":
					"Cause Unknown.")
					);
		}
	}
	
}


