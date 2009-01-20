package org.teacake.monolith.apk;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class Monolith extends Activity
{
	
	private static final int ID_PLAY_GAME = Menu.FIRST;
	private static final int ID_OPTIONS = Menu.FIRST + 1;
	private static final int ID_EXIT = Menu.FIRST+2;
    

    private GameSurfaceView gsf;
    private GameOverlay overlay;
    
    private HighScoreTable hsTable;
    private Options options;
    private Sound soundManager;
    private Game game;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        
        this.soundManager = new SoundPoolManager(this);
        hsTable = new HighScoreTable(this,10);
        game = new MonolithGameData();
        
        options = new Options(game);
        overlay = new GameOverlay(this,hsTable,options);
        overlay.setVisibility(View.VISIBLE);
        overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        
        
        gsf = new GameSurfaceView(this,overlay,this.soundManager);

        setContentView(gsf);
        gsf.setVisibility(View.VISIBLE);   
        
        this.addContentView(overlay,new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
		
		
		soundManager.addSound(R.raw.explosion2, false);
		soundManager.addSound(R.raw.place, false);
		soundManager.addSound(R.raw.rotate,false);
		soundManager.addSound(R.raw.pluck, false);
		soundManager.startSound();
		soundManager.addSound(R.raw.monolithogg2, true);
		//try
		//{
		//	Thread.currentThread().sleep(10000);
		//}
		//catch(Exception e)
		//{
		//	
		//}
	
		soundManager.playSound(R.raw.monolithogg2); 
        
         
    }
    @Override
    public void onPause()
    {
    	super.onPause();
    	this.soundManager.stopSound();
    	//gsf.stopMusic();
    }
    @Override
    public void onStop()
    {
    	super.onStop();
    	this.soundManager.stopSound();
    }

    
    public void playGame()
    {
    	
		gsf.setGameType(overlay.getOptions().getGameType());
	
		gsf.initGame(GLThread.VIEW_GAME);
		
    	
    }
    public void showOptions()
    {
    	gsf.setGameType(game.getGameType());
    	
    	gsf.initGame(GLThread.VIEW_OPTIONS);
    	
    	
    }

    

    public void exitApplication()
    {
    		this.soundManager.stopSound();
    	
			finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case ID_PLAY_GAME:
            playGame();
            return true;
        case ID_OPTIONS:
        	showOptions();
        	return true;
        case ID_EXIT:
        	exitApplication();
        	return true;
        
        }
       
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, ID_PLAY_GAME,0 ,R.string.s_play);
        menu.add(0, ID_OPTIONS,0,R.string.s_options);
        menu.add(0, ID_EXIT,0,R.string.s_exit);
        return true;
    }   
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        boolean handled = false;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
        {
        	try
        	{
        		android.os.Message message = android.os.Message.obtain(gsf.getHandler(), GLThread.MSG_MOVE_DOWN);
        		message.sendToTarget();
        	}
        	catch(Exception e)
        	{
        		
        	}
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        {
        	try
        	{
        		android.os.Message message = android.os.Message.obtain(gsf.getHandler(), GLThread.MSG_MOVE_LEFT);
        		message.sendToTarget();
        		
        	}
        	catch(Exception e)
        	{
        		
        	}
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
        	try
        	{
        		android.os.Message message = android.os.Message.obtain(gsf.getHandler(), GLThread.MSG_MOVE_RIGHT);
        		message.sendToTarget();
        	}
        	catch(Exception e)
        	{
        		
        	}
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
        	try
        	{
        		android.os.Message message = android.os.Message.obtain(gsf.getHandler(), GLThread.MSG_ROTATE);
        		message.sendToTarget();
        	}
        	catch(Exception e)
        	{
        		
        	}
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
        	this.exitApplication();
        }
        

        
        return handled;
    }

    /**
     * Standard override for key-up. We actually care about these,
     * so we can turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        boolean handled = false;
        return handled;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	int action = event.getAction();
    	boolean handled = false;
    	if(action==MotionEvent.ACTION_DOWN)
    	{
    		xval=(int)event.getX();
    		yval=(int)event.getY();

    		handled = true;
    	}
    	
    	if(action==MotionEvent.ACTION_UP)
    	{
    		int xnow = (int)event.getX();
    		int ynow = (int)event.getY();
    		if(xnow<20 && ynow<20)
    		{
    			zx =0 ;
    			zy =0 ;
        		try
        		{
        			android.os.Message message = android.os.Message.obtain(gsf.getHandler(), GLThread.MSG_ROTATE_PLAYFIELD);
        			message.arg1 = zx;
        			message.arg2 = zy;
        			message.sendToTarget();
        		}
        		catch(Exception e)
        		{
        			
        		}
    		}
    		handled=true;
    	}
    	if(action==MotionEvent.ACTION_MOVE)
    	{
            zx = zx+((int)event.getX()-xval);
            zy = zy+((int)event.getY()-yval);
      	  	xval=(int)event.getX();
      	  	yval=(int)event.getY();
    		try
    		{
    			android.os.Message message = android.os.Message.obtain(gsf.getHandler(), GLThread.MSG_ROTATE_PLAYFIELD);
    			message.arg1 = zx;
    			message.arg2 = zy;
    			message.sendToTarget();
    		}
    		catch(Exception e)
    		{
    			
    		}      	  	
      	  	handled = true;
    	}

        return handled;
    } 

    private int xval;
    private int yval;
    private int zx;
    private int zy;
    
     
}