package org.teacake.monolith;

import android.content.Context;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	
	public GameSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}
	public GameSurfaceView(Context context,GameOverlay overlay)
	{
		super(context);
		getHolder().addCallback(this);
		this.overlay = overlay;
	}

	public void surfaceChanged(SurfaceHolder holder, int format,
	int w, int h) {
	// TODO: handle window size changes
	}
	
	private org.teacake.monolith.GLThread glThread;
	@Override
	public android.os.Handler getHandler()
	{
		try
		{
			return glThread.messageHandler;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	public void initGame()
	{
		glThread.setGameType(this.gameType);
		glThread.setViewType(viewType);
        if(this.viewType==GLThread.VIEW_INTRO)
        {
        	overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        }
        else
        {
        	switch(this.gameType)
        	{
        	case Monolith.GAME_CLASSIC:
        		overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME_CLASSIC);
        		break;
        	case Monolith.GAME_MONOLITH:
        		overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME_MONOLITH);
        		break;
        	}
        	
        }
		
		glThread.reinit();
	}
	public void surfaceCreated(SurfaceHolder holder)
	{
		// The Surface has been created so start our drawing thread
		glThread =new org.teacake.monolith.GLThread(this,this.overlay);
		glThread.setViewType(viewType);
		glThread.setGameType(gameType);
		glThread.start();
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// Stop our drawing thread. The Surface will be destroyed
		// when we return
		glThread.requestExitAndWait();
		glThread = null;
	}
	public void setViewType(int viewType)
	{
		this.viewType = viewType;
		
		
	}
	public void setGameType(int gameType)
	{
		this.gameType = gameType;
		
	}
	
	
	private int viewType;
	private int gameType;
	private GameOverlay overlay;
}
