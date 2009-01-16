package org.teacake.monolith.apk;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class Monolith extends Activity
{
	
	//private static final int MONOLITH_ID = Menu.FIRST;
    //private static final int CLASSIC_ID = Menu.FIRST + 1;
    //private static final int EXIT_ID = Menu.FIRST + 2;
	private static final int ID_PLAY_GAME = Menu.FIRST;
	private static final int ID_OPTIONS = Menu.FIRST + 1;
	private static final int ID_EXIT = Menu.FIRST+2;
    
	public static final int GAME_CLASSIC = 0;
	public static final int GAME_MONOLITH = 1;
	public static final int GAME_PUZZLE = 2;
    //GLView view;
    GameSurfaceView gsf;
    GameOverlay overlay;
    OptionsView optionsView;
    View highscoreNameEntry;
    HighScoreTable hsTable;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        hsTable = new HighScoreTable(this,10);
        
        //view = new GLView( getApplication(),GAME_MONOLITH );
        //view.setViewType(GLView.VIEW_INTRO);
        //view.doInit();
        //view.running=true;
        
        
        overlay = new GameOverlay(this,hsTable);
        overlay.setVisibility(View.VISIBLE);
        overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        optionsView = new OptionsView(getApplication());
        android.content.res.AssetManager mgr = getApplication().getAssets();
        gsf = new GameSurfaceView(this,overlay);
        gsf.setViewType(GLThread.VIEW_INTRO);
        gsf.setGameType(Monolith.GAME_MONOLITH);
        
        //overlay.setVisibility(View.VISIBLE);
        
        
        //String[] assetlist=null;
        /*
        String message = "->";
        try
        {
        	assetlist = mgr.list("/");
        	if (assetlist!=null)
        	{
        		for(int i=0;i<assetlist.length;i++)
        		{
        			message+=assetlist[i]+"{";
        		}
        		view.message = message;
        	}
        }
        catch(Exception e)
        {
        	
        }
        */
        //this.addContentView(gsf,new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
        //this.addContentView(overlay,new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
        setContentView(gsf);
        gsf.setVisibility(View.VISIBLE);        
        this.addContentView(overlay,new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
         
        
         
    }
    @Override
    public void onPause()
    {
    	super.onPause();
    	gsf.stopMusic();
    }
    @Override
    public void onStop()
    {
    	super.onStop();
    	gsf.stopMusic();
    }

    
    public void playGame()
    {
    	
		gsf.setGameType(overlay.getOptions().getGameType());
		gsf.setViewType(GLThread.VIEW_GAME);
		gsf.initGame();
    	
    }
    public void showOptions()
    {
    	gsf.setGameType(Monolith.GAME_MONOLITH);
    	gsf.setViewType(GLThread.VIEW_OPTIONS);
    	
    	gsf.initGame();
    	gsf.getOverlay().getOptions().setStatus(Options.STATUS_SELECTING);
    	gsf.getOverlay().setDrawType(GameOverlay.DRAW_GAME_OPTIONS);
    	gsf.getOverlay().setOverlayType(GameOverlay.OVERLAY_TYPE_OPTIONS);
    	
    }
    public void playMonolithGame()
    {
    	
		gsf.setGameType(Monolith.GAME_MONOLITH);
		gsf.setViewType(GLThread.VIEW_GAME);
		gsf.initGame();

    	
    }
    public void playClassicGame()
    {
    	gsf.setGameType(Monolith.GAME_CLASSIC);
    	gsf.setViewType(GLThread.VIEW_GAME);
    	gsf.initGame();   	
    }
    

    public void exitApplication()
    {
    		gsf.stopMusic();
			finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        /*
        case MONOLITH_ID:
            playMonolithGame();
            return true;
        case CLASSIC_ID:
        	playClassicGame();
        	return true;
        case EXIT_ID:
        	exitApplication();
        	return true;
        	*/
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

        /*
        menu.add(0, MONOLITH_ID,0 ,R.string.m_new_monolith);
        menu.add(0, CLASSIC_ID,0,R.string.m_new_classic);
        menu.add(0, EXIT_ID,0,R.string.s_exit_game );
        */
        menu.add(0, ID_PLAY_GAME,0 ,R.string.s_play);
        menu.add(0, ID_OPTIONS,0,R.string.s_options);
        menu.add(0, ID_EXIT,0,R.string.s_exit);
        
                
   
        return true;
    }   
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        boolean handled = false;
        /*
        if(view.game!=null)
        {
        	if (view.game.getStatus()!= SimpleGameData.STATUS_PLAYING)
        	{
        		return handled;
        	}
        }
        */
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
        

        
        return handled;
    }

    /**
     * Standard override for key-up. We actually care about these,
     * so we can turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        boolean handled = false;
        /*
        if (mMode == RUNNING) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                keyCode == KeyEvent.KEYCODE_SPACE) {
                setFiring(false);
                handled = true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                       keyCode == KeyEvent.KEYCODE_Q || 
                       keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                       keyCode == KeyEvent.KEYCODE_W) {
                mRotating = 0;
                handled = true;
            }
        }
		*/
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