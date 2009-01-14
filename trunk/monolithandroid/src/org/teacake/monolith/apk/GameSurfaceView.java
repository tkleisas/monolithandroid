package org.teacake.monolith.apk;

import android.content.Context;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	
	public GameSurfaceView(Context context) {
		super(context);
		this.context = context;
		getHolder().addCallback(this);
		getHolder().setType(android.view.SurfaceHolder.SURFACE_TYPE_GPU);
	}
	public GameSurfaceView(Context context,GameOverlay overlay)
	{
		super(context);
		this.context = context;
		this.overlay = overlay;
		this.overlay.setCurtain(100);
		
		getHolder().addCallback(this);
		getHolder().setType(android.view.SurfaceHolder.SURFACE_TYPE_GPU);

		
	}

	public void surfaceChanged(SurfaceHolder holder, int format,
	int w, int h) {
	// TODO: handle window size changes
	}
	
	private org.teacake.monolith.apk.GLThread glThread;
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
		glThread =new org.teacake.monolith.apk.GLThread(this,this.overlay,context);
		glThread.setViewType(viewType);
		glThread.setGameType(gameType);
		glThread.start();
		
	}
	public void stopMusic()
	{
		glThread.stopMusic();
		//glThread.requestExitAndWait();
		//glThread = null;
	}
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
	public GameOverlay getOverlay()
	{
		return overlay;
	}
	private android.content.Context context;
	
}
