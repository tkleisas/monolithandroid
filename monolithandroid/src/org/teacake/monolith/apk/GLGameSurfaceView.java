package org.teacake.monolith.apk;
import android.content.Context;
import android.opengl.GLSurfaceView;

import android.content.SharedPreferences;


public class GLGameSurfaceView extends GLSurfaceView
{

	public GLGameSurfaceView(Context context,GameOverlay overlay, Sound manager) {
		super(context);
		// TODO Auto-generated constructor stub
        this.prefs = prefs;
        this.soundManager = soundManager;
        this.context = context;
        this.overlay = overlay;
        //this.overlay.setCurtain(100);
        this.viewType = GameRenderer.VIEW_INTRO;
        this.gameType = Game.GAME_MONOLITH;
		this.mRenderer = new GameRenderer(context,overlay,manager);
		this.setRenderer(mRenderer);
	}
	
	private GameRenderer mRenderer;
	
	
	


    

    @Override
    public android.os.Handler getHandler()
    {
            try
            {
                    return this.mRenderer.messageHandler;
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
                    case GameRenderer.VIEW_INTRO:
                    	this.mRenderer.setGameType(Game.GAME_MONOLITH);
                    	this.mRenderer.setViewType(GameRenderer.VIEW_INTRO);
                            overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
                    
                    overlay.setDrawType(GameOverlay.DRAW_NORMAL);
                    
                            break;
                    case GameRenderer.VIEW_GAME:
                    	this.mRenderer.setGameType(overlay.getOptions().getGameType());
                    	this.mRenderer.setViewType(GameRenderer.VIEW_GAME);
                            switch(overlay.getOptions().getGameType())
                            {
                            case Game.GAME_CLASSIC:
                                    overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME_CLASSIC);
                                    overlay.getOptions().setGame(new SimpleGameData());
                                    overlay.getOptions().getGame().setLevel(overlay.getOptions().getStartingLevel());
                                    overlay.setLevel(overlay.getOptions().getGame().getLevelName());
                                    overlay.getOptions().getGame().initGame(overlay.getOptions().getStartingLevel());
                                    break;
                            case Game.GAME_MONOLITH:
                                    overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_GAME_MONOLITH);
                                    overlay.getOptions().setGame(new MonolithGameData());
                                    overlay.getOptions().getGame().setLevel(overlay.getOptions().getStartingLevel());
                                    overlay.setLevel(overlay.getOptions().getGame().getLevelName());
                                    overlay.getOptions().getGame().initGame(overlay.getOptions().getStartingLevel());
                                    break;
                                    
                            }
                            overlay.setDrawType(GameOverlay.DRAW_NORMAL);
                            break;
                    case GameRenderer.VIEW_OPTIONS:
                    		this.mRenderer.setGameType(Game.GAME_MONOLITH);
                            this.mRenderer.setViewType(GameRenderer.VIEW_OPTIONS);
                            overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_OPTIONS);
                            overlay.setDrawType(GameOverlay.DRAW_GAME_OPTIONS);
                            overlay.getOptions().setSelectionStatus(Options.STATUS_SELECTING);
                            
                            break;
                    
            }
            this.mRenderer.reinit();
    }

    public void stopMusic()
    {
    	this.mRenderer.stopMusic();
            //glThread.requestExitAndWait();
            //glThread = null;
    }

    public void setViewType(int viewType)
    {
            this.viewType = viewType;
            
            
    }
    public void setGameType(int gameType)
    {
            this.gameType = gameType;
            
    }
    
    private SharedPreferences prefs;
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
