package ca.dperez.cs10.culm.sound;

public class Volume 
{
	public static final float MUTE = -80.0f;
	public static final float MIN = -35.0f;
	public static final float LOW = -25.0f;
	public static final float NORMAL = -15.0f;
	public static final float LOUD = -5.0f;
	public static final float MAX = 5.0f;
 	
	private static float volume = MUTE;
	public static int slider = 0;

	public static float get() {
		return volume;
	}

	public static void set(float volume) {
		Volume.volume = volume;
	}
	
	public static void up()
	{
		if(slider < 5)
		{
			switch(++slider)
			{
			case 0:
				set(MUTE);
				break;
			case 1:
				set(MIN);
				break;
			case 2:
				set(LOW);
				break;
			case 3:
				set(NORMAL);
				break;
			case 4:
				set(LOUD);
				break;
			case 5:
				set(MAX);
				break;
			}
			
			new PlaySound("eatfruit").play();
		}
	}
	
	public static void down()
	{
		if(slider > 0)
		{
			switch(--slider)
			{
			case 0:
				set(MUTE);
				break;
			case 1:
				set(MIN);
				break;
			case 2:
				set(LOW);
				break;
			case 3:
				set(NORMAL);
				break;
			case 4:
				set(LOUD);
				break;
			case 5:
				set(MAX);
				break;
			}
			
			new PlaySound("eatfruit").play();
		}
	}
}
