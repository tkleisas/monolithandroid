package org.teacake.monolith.apk;

import android.content.Context;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	

	public GameSurfaceView(Context context,GameOverlay overlay,Sound soundManager)
	{
		super(context);
		this.soundManager = soundManager;
		this.context = context;
		this.overlay = overlay;
		//this.overlay.setCurtain(100);
		this.viewType = GLThread.VIEW_INTRO;
		this.gameType = Game.GAME_MONOLITH;
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
	
	public void initGame(int gameType)
	{
		switch(gameType)
		{
			case GLThread.VIEW_INTRO:
				glThread.setGameType(Game.GAME_MONOLITH);
				glThread.setViewType(GLThread.VIEW_INTRO);
				overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
		    	
		    	overlay.setDrawType(GameOverlay.DRAW_NORMAL);
		    	
				break;
			case GLThread.VIEW_GAME:
				glThread.setGameType(overlay.getOptions().getGameType());
				glThread.setViewType(GLThread.VIEW_GAME);
				overlay.setOverlayType(overlay.getOptions().getGameType());
				overlay.setDrawType(GameOverlay.DRAW_NORMAL);
				break;
			case GLThread.VIEW_OPTIONS:
				glThread.setGameType(Game.GAME_MONOLITH);
				glThread.setViewType(GLThread.VIEW_OPTIONS);
				overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_OPTIONS);
				overlay.setDrawType(GameOverlay.DRAW_GAME_OPTIONS);
				overlay.getOptions().setSelectionStatus(Options.STATUS_SELECTING);
				break;
			
		}
		glThread.reinit();
	}
	public void surfaceCreated(SurfaceHolder holder)
	{
		
		// The Surface has been created so start our drawing thread
		glThread =new org.teacake.monolith.apk.GLThread(this,this.overlay,context,this.soundManager);
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
	private Sound soundManager;
	public GameOverlay getOverlay()
	{
		return overlay;
	}
	private android.content.Context context;
	
}
