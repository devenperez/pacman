package ca.dperez.cs10.culm.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlaySound 
{
	private Clip clip;
	private String name;
	
	public PlaySound(String name)
	{
		this.name = name;
		try 
		{
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("assets/sounds/pacman_" + name.toLowerCase() + ".wav")));
		} 
		catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void play() 
	{
		if(Volume.get() > Volume.MUTE)
		{
			clip.setFramePosition(0);
			if(name == "chomp") clip.loop(Clip.LOOP_CONTINUOUSLY);
			((FloatControl) (clip.getControl(FloatControl.Type.MASTER_GAIN))).setValue(Volume.get());
	        clip.start();
		}
	}
	
}
