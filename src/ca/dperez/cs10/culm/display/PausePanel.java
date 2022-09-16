package ca.dperez.cs10.culm.display;

import java.awt.AlphaComposite;
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


public class PausePanel extends JPanel 
{
	private int state;
	private int activeButton;
	private int maxButton = 2;
	
	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D g = (Graphics2D) graphics;
		PacmanGame.gamePanel.paintComponent(g); // Draws game in background
		// Draw Background 
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Transparent background
		g.setColor(Color.BLACK);
		g.fillRect(-1, -1, getWidth() + 3, getHeight() + 3);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Continue to draw opaque
		
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

		switch(state)
		{
		case 0:
			drawButton(g, getWidth() / 2 - 200, 295 + PMConstants.VERTICAL_MARGIN, "Resume", 0);
			drawButton(g, getWidth() / 2 - 200, 415 + PMConstants.VERTICAL_MARGIN, "Volume", 1);
			drawButton(g, getWidth() / 2 - 200, 535 + PMConstants.VERTICAL_MARGIN, "Quit", 2);
			break;
		case 1:
			g.setColor(Color.WHITE);
			g.setFont(PMConstants.FONT.deriveFont(55.0f));
			Utility.drawCenteredString(g, "Volume", getWidth() / 2, 350 + PMConstants.VERTICAL_MARGIN);
			g.setColor(Color.BLACK);
			g.fillRoundRect(getWidth() / 2 - 300, 415 + PMConstants.VERTICAL_MARGIN, 600, 150, 50, 50);
			g.setColor(new Color(72, 0, 255));
			g.drawRoundRect(getWidth() / 2 - 300, 415 + PMConstants.VERTICAL_MARGIN, 600, 150, 50, 50);
			g.setColor(Color.YELLOW);
			g.fillArc(getWidth() / 2 - 285 + 100 * Volume.slider, 440 + PMConstants.VERTICAL_MARGIN, 100, 105, Volume.slider == 5 ? 55 : 35, Volume.slider == 5 ? 250 : 290);
			if(Volume.slider < 1) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 - 185, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 2) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 - 85, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 3) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 + 15, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 4) g.drawImage(PMConstants.SPRITES[5][0], getWidth() / 2 + 115, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			if(Volume.slider < 5) g.drawImage(PMConstants.FRUITS[0], getWidth() / 2 + 215, 460 + PMConstants.VERTICAL_MARGIN, 60, 60, null);
			break;
		}
		
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
	}
	
	private void drawButton(Graphics2D g, int x, int y, String str, int buttonNum)
	{
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, 400, 90, 50, 50);
		g.setColor(activeButton == buttonNum ? Color.YELLOW : Color.WHITE);
		g.drawRoundRect(x, y, 400, 90, 50, 50);
		g.setFont(PMConstants.FONT.deriveFont(28.0f));
		Utility.drawCenteredString(g, str, x + 200, y + 40 + (g.getFontMetrics().getHeight() / 2));
	}
	
	public void keyListener(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch (keyCode) 
		{
		case KeyEvent.VK_UP:	case KeyEvent.VK_W:
			if(state == 0 && activeButton > 0)
				activeButton--;
			break;
		case KeyEvent.VK_DOWN:	case KeyEvent.VK_S:
			if(state == 0 && activeButton < maxButton)
				activeButton++;
			break;
		case KeyEvent.VK_ESCAPE:
			if(state == 0)
				PacmanGame.setScreen(FrameState.GAME);
			else if(state == 1)
				state = 0;
			break;
		case KeyEvent.VK_ENTER:
			if(activeButton == 0)
				PacmanGame.setScreen(FrameState.GAME);
			else if(activeButton == 1)
				state = 1;
			else if(activeButton == 2)
				PacmanGame.setScreen(FrameState.START);
			break;
		case KeyEvent.VK_RIGHT:	case KeyEvent.VK_D:
			if(state == 1)
				Volume.up();
			break;
		case KeyEvent.VK_LEFT:	case KeyEvent.VK_A:
			if(state == 1)
				Volume.down();
			break;
		}
	}
}
