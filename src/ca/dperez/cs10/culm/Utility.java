package ca.dperez.cs10.culm;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Utility 
{

	public static boolean equalsWithError(double i, int j, double error)
	{
		if((i == j)
		|| (i < j && i + error >= j)
		|| (i > j && i - error <= j))
			return true;
		return false;
	}
	
	public static void drawCenteredString(Graphics2D g, String str, int x, int y)
	{
		FontMetrics metrics = g.getFontMetrics();
		int stringLength = metrics.stringWidth(str);
		g.drawString(str, x - stringLength / 2, y);
	}
	

	public static String scoreFormatter(int score) 
	{
		String str = String.valueOf(score);
		for(int i = str.length(); i < 6; i++)
			str = "0" + str;
		return str;
	}
	
}
