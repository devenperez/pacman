package ca.dperez.cs10.culm.sprite;

import java.awt.Color;
import java.awt.Graphics2D;

import ca.dperez.cs10.culm.PMConstants;
import ca.dperez.cs10.culm.PacmanGame;
import ca.dperez.cs10.culm.TickEvents;

public class PointIndicator 
{
	private int x;
	private int y;
	private int score;
	private int timer;
	
	public PointIndicator(int x, int y, int score)
	{
		this.x = x;
		this.y = y;
		this.score = score;
		this.timer = (int) (TickEvents.TICKS_PER_SECOND);
		PacmanGame.gamePanel.indicators.add(this);
	}
	
	public int tick()
	{
		return --timer;
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.setFont(PMConstants.FONT.deriveFont(12.0f));
		g.drawString(String.valueOf(score), x, y);
	}
	
	
}
