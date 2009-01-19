package org.teacake.monolith.apk;
import android.media.MediaPlayer;
import android.util.Log;
class SoundEvent
{
	public SoundEvent(int eventType,int eventSound)
	{
		this.eventType = eventType;
		this.eventSound = eventSound;
	}
	public int eventType;
	public int eventSound;
	
	public static final int SOUND_PLAY=0;
	public static final int SOUND_STOP=1;
}
class MediaPlayerSettings
{
	public MediaPlayerSettings(MediaPlayer player,boolean isLooping)
	{
		this.mediaPlayer = player;
		this.looping = isLooping;
	}
	public boolean looping;
	public MediaPlayer mediaPlayer;
}
public class SoundManager extends Thread implements Sound
{
	SoundManager(android.content.Context context)
	{
		this.context = context;
		soundEvents = new java.util.LinkedList<SoundEvent>();
		players = new java.util.HashMap<Integer, MediaPlayerSettings>();
		isRunning = false;
	}
	public void addSound(int resid, boolean isLooping)
	{
		try
		{
			MediaPlayer mediaPlayer = MediaPlayer.create(context, resid);
			
			
			//mediaPlayer.setLooping(isLooping);			
			players.put((resid), new MediaPlayerSettings(mediaPlayer,isLooping));

			
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
					SoundEvent event = soundEvents.remove();
					if(event!=null)
					{
						if(event.eventType == SoundEvent.SOUND_PLAY)
						{
							currentPlayer = event.eventSound;
					
					
							//Thread t = new Thread()
							//{
							//	public void run()
							//	{
							//		try
							//		{
										
										int player = currentPlayer;

										MediaPlayerSettings mediaPlayerSettings=players.get(player);
										if(mediaPlayerSettings.mediaPlayer==null)
										{
											mediaPlayerSettings.mediaPlayer=MediaPlayer.create(context, player);
										}
										mediaPlayerSettings.mediaPlayer.setLooping(mediaPlayerSettings.looping);
										mediaPlayerSettings.mediaPlayer.seekTo(0);
										mediaPlayerSettings.mediaPlayer.start();
							//		}
							//		catch(Exception e2)
							//		{
									
							//		}
								
							//	}
							//};
							//t.start();
						}else
						if(event.eventType == SoundEvent.SOUND_STOP)
						{
							currentPlayer = event.eventSound;
					

										int player = currentPlayer;
										MediaPlayerSettings mediaPlayerSettings=players.get(player);
										if(mediaPlayerSettings.mediaPlayer!=null)
										{
											mediaPlayerSettings.mediaPlayer.stop();
											if(mediaPlayerSettings.looping)
											{
												mediaPlayerSettings.mediaPlayer.release();
												mediaPlayerSettings=null;
											}
										}
						}
						
						
					}
				}
			}
			catch(Exception e)
			{
				Log.d("Error",e.getMessage());
			}

			try
			{
				java.lang.Thread.currentThread().sleep(100);
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
			MediaPlayerSettings mps = players.get(iterator.next());
			if(mps.mediaPlayer!=null)
			{

				mps.mediaPlayer.stop();
				mps.mediaPlayer.release();
				mps.mediaPlayer=null;
			}
		}
		
		isRunning=false;
		
	}

	public int currentPlayer;
	private boolean isRunning;
	private java.util.HashMap<Integer, MediaPlayerSettings> players;
	private android.content.Context context;
	private java.util.LinkedList<SoundEvent > soundEvents;
	
	public void stopSound(int resid)
	{
		if(soundEvents!=null)
		{
			try
			{
				soundEvents.add(new SoundEvent(SoundEvent.SOUND_STOP,resid));
			}
			catch(Exception e)
			{
				
			}
		}		
	}
	public void playSound(int resid)
	{
		if(soundEvents!=null)
		{
			try
			{
				soundEvents.add(new SoundEvent(SoundEvent.SOUND_PLAY,resid));
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	
	
}
