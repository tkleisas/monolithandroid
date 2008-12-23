package org.teacake.monolith.apk;
import android.media.MediaPlayer;

public class SoundManager extends Thread
{
	SoundManager(android.content.Context context)
	{
		this.context = context;
		soundEvents = new java.util.LinkedList<Integer>();
		players = new java.util.HashMap<Integer, MediaPlayer>();
		isRunning = false;
	}
	public void addSound(int resid, boolean isLooping)
	{
		try
		{
			MediaPlayer mediaPlayer = MediaPlayer.create(context, resid);
			
			
			mediaPlayer.setLooping(isLooping);			
			players.put(new Integer(resid), mediaPlayer);

			
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void run()
	{
	
		while(isRunning)
		{
			try
			{
				while(soundEvents.size()>0)
				{
				
					
					Integer resid = soundEvents.remove();
					
					if(resid!=null)
					{
						MediaPlayer mediaPlayer=players.get(resid);
						if(!mediaPlayer.isPlaying())
						{
							mediaPlayer.seekTo(0);
							mediaPlayer.start();
						}
						
					}
					
				}
			}
			catch(Exception e)
			{
				
			}

			try
			{
				java.lang.Thread.currentThread().sleep(200);
			}
			catch(Exception e)
			{
				
			}			
		}
	}


	public void startSound()
	{
		isRunning=true;
		this.start();
	}
	public void stopSound()
	{
		if(players==null)
			return;
		if(players.keySet()==null)
			return;
		if(players.keySet().iterator()==null)
		{
			return;
		}
		java.util.Iterator <Integer> iterator = players.keySet().iterator();
		
		while(iterator.hasNext())
		{
			MediaPlayer mp = players.get(iterator.next());
			mp.stop();
			
		}
		
		isRunning=false;
		
	}
	public void stopSound(int resid)
	{
		try
		{
			MediaPlayer mediaPlayer = players.get(new Integer(resid));
			mediaPlayer.stop();
		}
		catch(Exception e)
		{
			
		}
	}
	private boolean isRunning;
	private java.util.HashMap<Integer, MediaPlayer> players;
	private android.content.Context context;
	public static java.util.LinkedList<Integer > soundEvents;
	public static void playSound(int resid)
	{
		if(soundEvents!=null)
		{
			try
			{
				soundEvents.add(new Integer(resid));
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	
	
}
