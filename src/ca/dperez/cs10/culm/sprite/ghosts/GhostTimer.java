package ca.dperez.cs10.culm.sprite.ghosts;

import ca.dperez.cs10.culm.TickEvents;

public class GhostTimer 
{

	private int dotsEaten;
	private int limit;
	private boolean active;
	
	public GhostTimer(int limit)
	{
		this.limit = limit;
		this.active = false;
	}
	
	protected int getDotsEaten() {
		return dotsEaten;
	}

	public boolean isActive() {
		return active;
	}

	public void reset()
	{
		dotsEaten = 0;
	}
	
	public void pause()
	{
		this.active = false;
	}
	
	public void activate()
	{
		this.active = true;
	}
	
	public boolean count()
	{
		if(active && !TickEvents.isGhostFrightened()) this.dotsEaten++;
		return active;
	}
	
	public boolean exceedsLimit()
	{
		if(dotsEaten >= limit)
			return true;
		return false;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}
	
}
