package ca.dperez.cs10.culm.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.Utility;
import ca.dperez.cs10.culm.sound.Volume;

public class StartPanel extends JPanel 
{
	private int state = 0;
	private int maxX = 1;
	private int maxY = 2;
	private int[] activeButton = {0, 0};
	
	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;
		
		// Draw Background 
		g.setColor(Color.BLACK);
		g.fillRect(-1, -1, getWidth() + 3, getHeight() + 3);
		
		// Draw Logo
		g.setFont(PMConstants.LOGO_FONT);
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(5.0f));
		g.drawRoundRect(getWidth() / 2 - g.getFontMetrics().stringWidth("pac-man") / 2 - 60, 95 + PMConstants.VERTICAL_MARGIN, g.getFontMetrics().stringWidth("pac-man") + 120, g.getFontMetrics().getHeight() + 70, 50, 50);
		g.setColor(new Color(233, 150, 122)); // Salmon
		g.fillRoundRect(getWidth() / 2 - g.getFontMetrics().stringWidth("pac-man") / 2 - 50, 105 + PMConstants.VERTICAL_MARGIN, g.getFontMetrics().stringWidth("pac-man") + 100, g.getFontMetrics().getHeight() + 50, 50, 50);
		g.setColor(Color.WHITE);
		Utility.drawCenteredString(g, "pac-man", getWidth() / 2 + 5, 185 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.BLACK);
		Utility.drawCenteredString(g, "PAC-MAN", getWidth() / 2 + 5, 185 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.YELLOW);
		Utility.drawCenteredString(g, "pac-man", getWidth() / 2, 180 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.BLACK);
		Utility.drawCenteredString(g, "PAC-MAN", getWidth() / 2, 180 + PMConstants.VERTICAL_MARGIN);
		
		// **SCORES**
		// Prints score
		g.setFont(PMConstants.FONT);
		g.setColor(Color.RED);
		g.drawString("1UP", 32 + PMConstants.HORIZONTAL_MARGIN, 32 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.WHITE);
		g.drawString(Utility.scoreFormatter(PacmanGame.maze.player.getScore()), 32 + PMConstants.HORIZONTAL_MARGIN, 64 + PMConstants.VERTICAL_MARGIN);
		// Prints highscore
		g.setColor(Color.RED);
		Utility.drawCenteredString(g, "HIGH SCORE", PacmanGame.frame.getWidth() / 2, 32 + PMConstants.VERTICAL_MARGIN);
		g.setColor(Color.WHITE);
		Utility.drawCenteredString(g, Utility.scoreFormatter(PMConstants.HIGH_SCORE), PacmanGame.frame.getWidth() / 2, 64 + PMConstants.VERTICAL_MARGIN);
		
		switch(state)
		{
		case 0:
			// Player Selection
			g.setFont(PMConstants.FONT.deriveFont(25.0f));
			g.setColor(Color.WHITE);
			g.drawString("1 Player", getWidth() / 2 - 95, 340 + PMConstants.VERTICAL_MARGIN);
			g.setColor(Color.DARK_GRAY);
			g.drawString("2 Players", getWidth() / 2 - 95, 410 + PMConstants.VERTICAL_MARGIN);
			
			// Selector
			g.setColor(Color.WHITE);
			if(activeButton[0] == 0 && activeButton[1] == 0) g.drawString(">", getWidth() / 2 - 185, 340 + PMConstants.VERTICAL_MARGIN);
			g.setColor(Color.DARK_GRAY);
			if(activeButton[0] == 0 && activeButton[1] == 1) g.drawString(">", getWidth() / 2 - 185, 410 + PMConstants.VERTICAL_MARGIN);
	
			// Copyright info
			g.setFont((PMConstants.FONT).deriveFont(20.0f));
			g.setColor(Color.WHITE);
			g.drawImage(PMConstants.NAMCO_LOGO, getWidth() / 2 - 95, 490 + PMConstants.VERTICAL_MARGIN, 190, 25, null);
			Utility.drawCenteredString(g, "TM & © 1980 1993 NAMCO LTD.", getWidth() / 2, 570 + PMConstants.VERTICAL_MARGIN);
			Utility.drawCenteredString(g, "NAMCO HOMETEK, INC.", getWidth() / 2, 610 + PMConstants.VERTICAL_MARGIN);
			Utility.drawCenteredString(g, "LICENSED BY NINTENDO", getWidth() / 2, 650 + PMConstants.VERTICAL_MARGIN);
			
			// Volume logo
			g.drawImage(PMConstants.TEXTURES[(activeButton[0] == 1 && activeButton[1] == 2) ? 11 : 10], 565 + PMConstants.HORIZONTAL_MARGIN, 765 + PMConstants.VERTICAL_MARGIN, 75, 75, null);
			
			// Difficulty
			g.setFont(PMConstants.FONT.deriveFont(60.0f));
			switch(PacmanGame.difficulty)
			{
			case -1:
				g.setColor(Color.GREEN);
				g.drawString("E", 30 + PMConstants.HORIZONTAL_MARGIN, 830 + PMConstants.VERTICAL_MARGIN);
				break;
			case 0:
				g.setColor(Color.WHITE);
				g.drawString("N", 30 + PMConstants.HORIZONTAL_MARGIN, 830 + PMConstants.VERTICAL_MARGIN);
				break;
			case 1:
				g.setColor(Color.RED);
				g.drawString("H", 30 + PMConstants.HORIZONTAL_MARGIN, 830 + PMConstants.VERTICAL_MARGIN);
				break;
			}
			break;
		case 1:
			// Volume header
			g.setColor(Color.WHITE);
			g.setFont(PMConstants.FONT.deriveFont(55.0f));
			Utility.drawCenteredString(g, "Volume", getWidth() / 2, 350 + PMConstants.VERTICAL_MARGIN);
			
			// Slider Background
			g.setColor(Color.BLACK);
			g.fillRoundRect(getWidth() / 2 - 300, 415 + PMConstants.VERTICAL_MARGIN, 600, 150, 50, 50);
			g.setColor(new Color(72, 0, 255));
			g.drawRoundRect(getWidth() / 2 - 300, 415 + PMConstants.VERTICAL_MARGIN, 600, 150, 50, 50);
			g.setColor(Color.YELLOW);
			
			// Characters
			g.fillArc(getWidth() / 2 - 285 + 100 * Volume.slider, 440 + PMConstants.VERTICAL_MARGIN, 100, 105, Volume.slider == 5 ? 55 : 35, Volume.slider == 5 ? 250 : 290);
			if(Volume.slider < 1) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 - 185, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 2) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 - 85, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 3) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 + 15, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 4) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 + 115, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 5) g.drawImage(PMConstants.FRUITS[0], getWidth() / 2 + 215, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			break;
		}
	}
	
	public void keyListener(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch (keyCode) 
		{
		case KeyEvent.VK_UP:	case KeyEvent.VK_W:
			if(state == 0 && activeButton[1] > 0)
				activeButton[1]--;
			if(state == 0 && activeButton[0] == 1)
				activeButton[0] = 0;
			break;
		case KeyEvent.VK_DOWN:	case KeyEvent.VK_S:
			if(state == 0 && activeButton[1] < maxY)
				activeButton[1]++;
			break;
		case KeyEvent.VK_RIGHT:	case KeyEvent.VK_D:
			if(state == 0 && activeButton[1] == 2 && activeButton[0] < maxX)
				activeButton[0]++;
			else if(state == 1)
				Volume.up();
			break;
		case KeyEvent.VK_LEFT:	case KeyEvent.VK_A:
			if(state == 0 && activeButton[1] == 2 && activeButton[0] > 0)
				activeButton[0]--;
			else if(state == 1)
				Volume.down();
			break;
		case KeyEvent.VK_ESCAPE:
			if(state == 1)
				state = 0;
			break;
		case KeyEvent.VK_ENTER:
			if(state == 0 && activeButton[1] == 0)
			{
				PacmanGame.maze.player.restart();
				PacmanGame.maze.level = 0;
				PacmanGame.maze.nextLevel();
				PacmanGame.setScreen(FrameState.GAME);
			}
			else if(state == 0 && activeButton[0] == 0 && activeButton[1] == 2)
			{
				if(++PacmanGame.difficulty == 2)
					PacmanGame.difficulty = -1;
			}
			else if(state == 0 && activeButton[0] == 1 && activeButton[1] == 2)
			{
				state = 1;
			}
			break;
		}
	}
	
}
