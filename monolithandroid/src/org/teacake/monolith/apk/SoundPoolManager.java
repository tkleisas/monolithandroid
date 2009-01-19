package org.teacake.monolith.apk;
import android.media.SoundPool;
import android.util.Log;
class SoundPoolEvent
{
	public SoundPoolEvent(int eventType,int eventSound)
	{
		this.eventType = eventType;
		this.eventSound = eventSound;
	}
	public int eventType;
	public int eventSound;
	
	public static final int SOUND_PLAY=0;
	public static final int SOUND_STOP=1;
}
class SoundPoolInfo
{
	public SoundPoolInfo()
	{
		
	}
	public static final int STATUS_NOT_PLAYING=0;
	public static final int STATUS_PLAYING=1;
	
}
public class SoundPoolManager extends Thread implements Sound
{
	SoundPoolManager(android.content.Context context)
	{
		this.context = context;
		soundEvents = new java.util.LinkedList<SoundEvent>();
		sounds = new java.util.HashMap<Integer, Boolean>();
		handles = new java.util.HashMap<Integer, Integer>();
		mediaPlayers = new java.util.HashMap<Integer, android.media.MediaPlayer>();
		isRunning = false;
		
	}
	public void addSound(int resid, boolean isLooping)
	{
		
		sounds.put(resid, new Boolean(isLooping));
		if(isLooping)
		{
			try
			{
				android.media.MediaPlayer mp = android.media.MediaPlayer.create(context, resid);
				//mp.setLooping(true);
				mp.seekTo(0);
				//mp.prepare();
				mediaPlayers.put(resid, mp);
				
			
				//mp.seekTo(0);
				//mp.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
			}
			catch(Exception e)
			{
				Log.d("ERROR",e.getMessage());
			}
				

			
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
							if(sounds.get(currentPlayer).booleanValue())
							{
								android.media.MediaPlayer mp = mediaPlayers.get(currentPlayer);
								if(!mp.isPlaying())
								{
									mp.seekTo(0);
									mp.start();
								}
							}
							else
							{
								android.media.AudioManager mgr = (android.media.AudioManager) context.getSystemService(android.content.Context.AUDIO_SERVICE); 
								int streamVolume = mgr.getStreamVolume(android.media.AudioManager.STREAM_MUSIC); 
								soundPool.play(handles.get( event.eventSound).intValue(), streamVolume, streamVolume, 1, 0, 1.0f);
						
							}
						}
						else 
						if(event.eventType == SoundEvent.SOUND_STOP)
						{
							if(sounds.get(currentPlayer).booleanValue())
							{
								currentPlayer = event.eventSound;
							
								android.media.MediaPlayer mp = mediaPlayers.get(currentPlayer);
								//if(mp.isPlaying())
								//{
								
									mp.pause();
								
								//}
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				//Log.d("Error",e.getMessage());
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
		this.soundPool = new android.media.SoundPool(this.sounds.size(),android.media.AudioManager.STREAM_MUSIC,100);
		java.util.Iterator <Integer> iterator = sounds.keySet().iterator();
		
		while(iterator.hasNext())
		{
			int soundid = iterator.next().intValue();
			int soundhandle = this.soundPool.load(this.context, soundid, 1);
			handles.put(new Integer(soundid), new Integer(soundhandle));
		}
				
		isRunning=true;
		this.start();
	}
	public void stopSound()
	{
		java.util.Iterator <Integer> iterator = sounds.keySet().iterator();
		
		while(iterator.hasNext())
		{
			
			int soundid = iterator.next().intValue();
			if(this.sounds.get(soundid).booleanValue())
			{
				android.media.MediaPlayer mp = mediaPlayers.get(soundid);
				mp.stop();
				mp.release();
				mp=null;
			}
			else
			{
				this.soundPool.pause( this.handles.get(soundid).intValue());
				this.soundPool.stop(this.handles.get(soundid).intValue());
				
			}
			
			
		}		
		
		isRunning=false;
		this.soundPool.release();
	}

	public int currentPlayer;
	private boolean isRunning;
	private java.util.HashMap<Integer, Boolean> sounds;
	private java.util.HashMap<Integer, Integer> handles;
	private android.content.Context context;
	private java.util.LinkedList<SoundEvent > soundEvents;
	private java.util.HashMap<Integer, android.media.MediaPlayer> mediaPlayers;
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
	SoundPool soundPool;
	
	
}
