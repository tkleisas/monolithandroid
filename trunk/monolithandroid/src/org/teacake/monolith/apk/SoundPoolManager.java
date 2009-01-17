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
		
		sounds = new java.util.HashMap<Integer, Boolean>();
		handles = new java.util.HashMap<Integer, Integer>();
		isRunning = false;
		
	}
	public void addSound(int resid, boolean isLooping)
	{
		
		sounds.put(resid, new Boolean(isLooping));
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
										android.media.AudioManager mgr = (android.media.AudioManager) context.getSystemService(android.content.Context.AUDIO_SERVICE); 
										int streamVolume = mgr.getStreamVolume(android.media.AudioManager.STREAM_MUSIC); 
										
										soundPool.play(handles.get( event.eventSound).intValue(), streamVolume, streamVolume, 1, 0, 1.0f);
										
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
					
					
							//Thread t = new Thread()
							//{
							//	public void run()
							//	{
							//		try
							//		{
										soundPool.pause(handles.get( event.eventSound).intValue());
										//soundPool.stop(handles.get( event.eventSound).intValue());
							//		}
							//		catch(Exception e2)
							//		{
									
							//		}
								
							//	}
							//};
							//t.start();
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
			this.soundPool.pause(soundid);
			
		}		
		
		isRunning=false;
		
	}

	public int currentPlayer;
	private boolean isRunning;
	private java.util.HashMap<Integer, Boolean> sounds;
	private java.util.HashMap<Integer, Integer> handles;
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
	SoundPool soundPool;
	
	
}
