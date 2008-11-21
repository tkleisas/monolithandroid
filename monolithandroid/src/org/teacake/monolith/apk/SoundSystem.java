package org.teacake.monolith.apk;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class SoundSystem extends Thread
{
	public SoundSystem(android.content.Context context)
	{
		action = SOUND_DO_NOTHING;
		lastEventTime= SystemClock.uptimeMillis();
		this.context = context;
		try
		{
			musicPlayer = android.media.MediaPlayer.create(context, R.raw.monolith);
			soundPlayerRotateBlock = android.media.MediaPlayer.create(context, R.raw.pluck);
			soundPlayerExplosion = android.media.MediaPlayer.create(context, R.raw.explosion);
			soundPlayerPlaceBlock = android.media.MediaPlayer.create(context, R.raw.place);
			musicPlayer.prepareAsync();
			musicPlayer.setLooping(true);
			musicPlayer.seekTo(0);
			soundPlayerRotateBlock.prepareAsync();
			soundPlayerRotateBlock.seekTo(0);
			soundPlayerExplosion.prepareAsync();
			soundPlayerExplosion.seekTo(0);
			soundPlayerPlaceBlock.prepareAsync();
			soundPlayerPlaceBlock.seekTo(0);
		}
		catch(Exception e)
		{
		}
	}
	private void startMusic()
	{
		if(musicPlayer==null)
		{
			return;
		}
		try
		{
			if(musicPlayer.isPlaying())
			{
				return;
			}
			else
			{
				musicPlayer.start();
			}
		}
		catch(Exception e)
		{
		}		
	}
	private void stopMusic()
	{
		if(soundPlayerExplosion==null)
		{
			return;
		}
		try
		{
			if(soundPlayerExplosion.isPlaying())
			{
				return;
			}
			else
			{
				soundPlayerExplosion.start();

			}
		}
		catch(Exception e)
		{
				
		}
		
		
	}
	private void exitSoundSystem()
	{
		try
		{
			if(this.musicPlayer!=null)
			{
				this.musicPlayer.stop();
			}
		}
		catch(Exception e)
		{
		}
		try
		{
			if(this.soundPlayerExplosion!=null)
			{
				this.soundPlayerExplosion.stop();
			}
		}
		catch(Exception e)
		{
		}	
		try
		{
			if(this.soundPlayerPlaceBlock!=null)
			{
				this.soundPlayerPlaceBlock.stop();
			}
		}
		catch(Exception e)
		{
		}		
		try
		{
			if(this.soundPlayerRotateBlock!=null)
			{
				this.soundPlayerRotateBlock.stop();
			}
		}
		catch(Exception e)
		{
		}			
	}
	
	private void playRotateBlock()
	{
		int duration=0;
		int currentPosition=0;
		if(soundPlayerRotateBlock==null)
		{
			return;
		}
		try
		{

			duration=soundPlayerRotateBlock.getDuration();
			currentPosition=soundPlayerRotateBlock.getCurrentPosition();
			//if(currentPosition==duration-1)
			{
				soundPlayerRotateBlock.seekTo(0);
				soundPlayerRotateBlock.start();
			}

		}
		catch(Exception e)
		{
				
		}
		

			
	}
	private void playExplosion()
	{
		int duration=0;
		int currentPosition=0;
		if(soundPlayerExplosion==null)
		{
			return;
		}
		try
		{

			duration=soundPlayerExplosion.getDuration();
			currentPosition=soundPlayerExplosion.getCurrentPosition();
			//if(currentPosition==duration-1)
			{
				soundPlayerExplosion.seekTo(0);
				soundPlayerExplosion.start();
			}


		}
		catch(Exception e)
		{
				
		}
				
	}
	private void playPlaceBlock()
	{
		int duration=0;
		int currentPosition=0;
		if(soundPlayerPlaceBlock==null)
		{
			return;
		}
		try
		{

			duration=soundPlayerPlaceBlock.getDuration();
			currentPosition=soundPlayerPlaceBlock.getCurrentPosition();
			//if(currentPosition==duration-1)
			{
				soundPlayerPlaceBlock.seekTo(0);
				soundPlayerPlaceBlock.start();
			}


		}
		catch(Exception e)
		{
				
		}
				
	}
	
	@Override
	public void run()
	{
		while(!done)
		{
			switch(action)
			{
				case SOUND_START_MUSIC:
				startMusic();
				break;
				case SOUND_STOP_MUSIC:
				stopMusic();
				break;
				case SOUND_PLAY_ROTATE_BLOCK:
				playRotateBlock();
				break;
				case SOUND_PLAY_EXPLOSION:
				playExplosion();
				
				break;
				case SOUND_PLAY_PLACE_BLOCK:
				playPlaceBlock();	
				break;
				case SOUND_EXIT:
					exitSoundSystem();
					done = true;
				break;
				
			}
			action=SOUND_DO_NOTHING;
			try
			{
				java.lang.Thread.currentThread().sleep(100);
			}
			catch(Exception e)
			{
				
			}
		}
	}
	public boolean done;
	public final Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
        	try
        	{
        		action=msg.what;
        		
        		
        	}
        	catch(Exception e)
        	{
        		
        	}
        }
    };
    private android.content.Context context;
    public long lastEventTime; 
    public int action;
    public static final int SOUND_EXIT=-2;
    public static final int SOUND_DO_NOTHING=-1;
    public static final int SOUND_START_MUSIC=0;
    public static final int SOUND_STOP_MUSIC=1;
    public static final int SOUND_PLAY_ROTATE_BLOCK=2;
    public static final int SOUND_PLAY_EXPLOSION=3;
    public static final int SOUND_PLAY_PLACE_BLOCK=4;
    
    private android.media.MediaPlayer musicPlayer;
    private android.media.MediaPlayer soundPlayerRotateBlock;
    private android.media.MediaPlayer soundPlayerExplosion;
    private android.media.MediaPlayer soundPlayerPlaceBlock;
    
}
