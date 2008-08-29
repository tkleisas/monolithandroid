package org.teacake.monolith.apk;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;
//import android.graphics.glutils.*;
//import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
public class GLThread extends Thread
{

	private final org.teacake.monolith.apk.GameSurfaceView view;
	private boolean done;
	public GLThread(org.teacake.monolith.apk.GameSurfaceView view, GameOverlay overlay, android.content.Context context)
	{
		done=false;
		this.view = view;
		this.overlay = overlay;
		this.context = context;
		soundSystem = new SoundSystem(context);
		soundSystem.start();
        mCube = new Cube[8];
        mCube[0] = new Cube(0xff00,0,0,0x10000);
        mCube[1] = new Cube(0,0xff00,0,0x10000);
        mCube[2] = new Cube(0,0,0xff00,0x10000);
        mCube[3] = new Cube(0xff00,0xff00,0,0x10000);
        mCube[4] = new Cube(0xff00,0,0xff00,0x10000);
        mCube[5] = new Cube(0,0xff00,0xff00,0x10000);
        mCube[6] = new Cube(0xf000,0xf0000,0,0x10000);
        mCube[7] = new Cube(0xffff,0x0ffff,0xffff,0x00ff);
        mStarfield = new Starfield(200,90.0f);	
        mMoon = new Square(0xffff,0x0ffff,0xffff,0xffff);
        mEarth = new Square(0xffff,0x0ffff,0xffff,0xffff);
        this.mPlayfieldCube = new Cube(0x8000,0x8000,0x8000,0x0);
        running=false;
        
        this.explodingCubes = new java.util.LinkedList<ExplodingCube>();
        randomgen = new java.util.Random(SystemClock.uptimeMillis());
        
        
	}
	
    public void onWindowResize(int w, int h) {
        synchronized(this) {
            mWidth = w;
            mHeight = h;
            mSizeChanged = true;
        }
    }

	public void setViewType(int viewtype)
	{
		this.viewType = viewtype;
	}
	public void setGameType(int gametype)
	{
		this.gametype = gametype;
	}
	
	

	public void requestExitAndWait()
	{
		// Tell the thread to quit
		done = true;
		try
		{
			join();
		}
		catch (InterruptedException ex)
		{
		// Ignore
		}
	}
	
	

	
	
	@Override
	public void run()
	{
		//Initialize OpenGL...
		/*
         * Get an EGL instance
         */
        EGL10 egl = (EGL10)EGLContext.getEGL();

        /*
         * Get to the default display.
         */
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        /*
         * We can now initialize EGL for that display
         */
        int[] version = new int[2];
        egl.eglInitialize(dpy, version);

        /*
         * Specify a configuration for our opengl session
         * and grab the first configuration that matches is
         */
        int[] configSpec = {
                EGL10.EGL_RED_SIZE,      5,
                EGL10.EGL_GREEN_SIZE,    6,
                EGL10.EGL_BLUE_SIZE,     5,
                EGL10.EGL_DEPTH_SIZE,   16,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] num_config = new int[1];
        egl.eglChooseConfig(dpy, configSpec, configs, 1, num_config);
        EGLConfig config = configs[0];

		EGLContext glc = egl.eglCreateContext(dpy, config,
                EGL10.EGL_NO_CONTEXT, null);
		EGLSurface surface = null;
		GL10 gl =null;

		while (!done)
		{
	        int w, h;
	        boolean changed;
	        synchronized(this) {
	            changed = mSizeChanged;
	            w = mWidth;
	            h = mHeight;
	            mSizeChanged = false;
	        }

	        if (changed)
	        {
	        	if(surface!=null)
	        	{
                    egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE,
                            EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                    egl.eglDestroySurface(dpy, surface);

	        	}
				surface = egl.eglCreateWindowSurface(dpy, config, view.getHolder(),
		                null);
		
				// Bind context to current thread and surface
				
				//glc.makeCurrent(view.getHolder());
				egl.eglMakeCurrent(dpy, surface, surface, glc);
		
				gl = (GL10) (glc.getGL());
				this.textures = new GLTextures(gl,this.context);
				this.textures.add(R.drawable.moon);
				this.textures.add(R.drawable.earth);
				this.textures.loadTextures();
				mMoon.setTextureId(R.drawable.moon);
				mMoon.setTextures(this.textures);
				mEarth.setTextureId(R.drawable.earth);
				mEarth.setTextures(textures);
				init(gl);
	        }			
		// Draw a single frame here...
			drawFrame(gl);
			egl.eglSwapBuffers(dpy, surface);
			if (egl.eglGetError() == EGL11.EGL_CONTEXT_LOST) {
                // we lost the gpu, quit immediately
                Context c = this.context;
                if (c instanceof Activity) {
                    ((Activity)c).finish();
                }
            }

			//glc.post();
			
		}
		
		// Free OpenGL resources
			//glc.destroy();
	}
	
	public void reinit()
	{
    	xval = 0;
    	yval =0;
        zx=0.0f;
        zy=0.0f;
        xoff = -10.0f;
        //-10.0f+x*2.0f, 21.0f-y*2.0f, zoff
    	yoff = 21.0f;
    	zoff = -50.0f;

        mAnimate = false;
        if(gametype==Monolith.GAME_CLASSIC)
        {
        	game = new SimpleGameData();
        }
        if(gametype==Monolith.GAME_MONOLITH)
        {
        	game = new MonolithGameData();
        }
        
       
        
        game.initGame(1);
        game.setScore(0);
        game.setLines(0);
        game.setTimerEnabled(true);
        game.setStatus(SimpleGameData.STATUS_PLAYING);
        demogame = new MonolithGameData();
        demogame.initGame(1);
        demogame.setScore(0);
        demogame.setLines(0);
        demogame.setEnergy(100);
        //demogame.setTimer(5000);
        
        demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
        this.setupDemoGrid();
        this.lastcalltime = SystemClock.uptimeMillis();
        rangle=0;	
        this.running = true;
        this.mSizeChanged = true;
	}
	
	private void init(GL10 gl)
	{

    	
    	xval = 0;
    	yval =0;
        zx=0.0f;
        zy=0.0f;
        xoff = -10.0f;
        //-10.0f+x*2.0f, 21.0f-y*2.0f, zoff
    	yoff = 21.0f;
    	zoff = -63.0f;

        mAnimate = false;
        if(gametype==Monolith.GAME_CLASSIC)
        {
        	game = new SimpleGameData();
        }
        if(gametype==Monolith.GAME_MONOLITH)
        {
        	game = new MonolithGameData();
        }
        
        if(this.viewType==VIEW_INTRO)
        {
        	try
        	{
        		android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_START_MUSIC);
        		message.sendToTarget();
        		
        	}
        	catch(Exception e)
        	{
        		
        	}
        }
        else
        {
        	try
        	{
        		android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_STOP_MUSIC);
        		message.sendToTarget();
        	}
        	catch(Exception e)
        	{
        		
        	}
        	
        }
        
        
        game.initGame(1);
        game.setScore(0);
        game.setLines(0);
        game.setTimerEnabled(true);
        game.setStatus(SimpleGameData.STATUS_PLAYING);
        demogame = new MonolithGameData();
        demogame.initGame(1);
        demogame.setScore(0);
        demogame.setLines(0);
        demogame.setEnergy(100);
        //demogame.setTimer(5000);
        
        demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
        this.setupDemoGrid();
        this.lastcalltime = SystemClock.uptimeMillis();
        rangle=0;
        paint = new Paint();
        paint2 = new Paint();
        bgpaint = new Paint();
        paint.setARGB(200, 255, 0, 0);
        //paint.setFakeBoldText(true);
        paint.setTextSize(14);
        paint2.setTextSize(14);
        paint2.setARGB(255, 128, 128, 128);
        
        
        gameOverPaint = new Paint();
        gameOverPaint.setARGB(128, 20, 20, 20);
        gameOverPaint.setTextSize(30);
    	
    	this.running = true;
    	//gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	//gl.glEnable(GL10.GL_TEXTURE_2D);
    	
    	//gl.glDisable(GL10.GL_TEXTURE_2D);
    	
        //this.gameOverXPos = getTextWidth(res.getString( R.string.s_game_over),gameOverPaint);
        //this.evolvingXPos = getTextWidth(res.getString( R.string.s_evolving ),gameOverPaint);
    	
    }		
    public synchronized void doMoveDown()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}
    	game.moveBlockDown();
    
    	game.gameLoop();
    	if(game.isBlockPlaced())
    	{
    		android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_PLACE_BLOCK);
    		message.sendToTarget();    		
    	}
    	game.flagCompletedLines();
    	this.createExplosions(game);
    }
    public synchronized void doMoveLeft()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

    	game.moveBlockLeft();
    }
    public synchronized void doMoveRight()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

    	game.moveBlockRight();
    
    }
    public synchronized void doRotatePlayfield(int zx,int zy)
    {
    	this.zx = zx;
    	this.zy = zy;
    }
    public synchronized void doRotateBlock()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}
		android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_ROTATE_BLOCK);
		message.sendToTarget();
    	game.rotateCurrentBlockClockwise();
    }
    protected void drawNextPiece(GL10 gl)
    {
    	
    	//float ystart=10.0f;
    	
    	if(game.getNextBlock().color>=0 && game.getNextBlock().color<this.mCube.length)
    	{
    		gl.glLoadIdentity();
    		Cube c = this.mCube[game.getNextBlock().color];
    		//gl.glTranslatef(10.0f, ystart, zoff);
    		gl.glTranslatef(18.0f, 0.0f, 0.0f);
    		gl.glRotatef(rangle, 0.0f, 0.0f, 1.0f);
    		gl.glTranslatef(-8.0f,-2.0f ,0.0f );
    		for(int i=0;i<4;i++)
    		{
    			gl.glPushMatrix();
    			c.setPosition((game.getNextBlock().xPos+game.getNextBlock().subblocks[i].xpos)*2.0f,-(game.getNextBlock().yPos+game.getNextBlock().subblocks[i].ypos)*2.0f,zoff);
    			c.draw(gl);
    			gl.glPopMatrix();
    		}
    	}
    }
    protected void drawStarfield(GL10 gl)
    {
    	
    }
    protected void drawExplodingCube(GL10 gl, ExplodingCube c)
    {
    	Cube dc = this.mCube[c.blocktype];
    	gl.glLoadIdentity();
    	dc.setPosition(c.x, c.y, c.z);
    	dc.draw(gl);
    }
    protected void drawBlocks(GL10 gl,Game thegame)
    {
    	for(int y=0;y<20;y++)
    	{
    		for(int x=0;x<10;x++)
    		{
    			if(thegame.getGridValue(x, y)!=-1)
    			{
    				Cube c = this.mCube[thegame.getGridValue(x, y)];
    				gl.glLoadIdentity();
    				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
    				c.draw(gl);
    			}
    		}
    	}
    }
    protected void drawBlocks(GL10 gl,Game thegame, int currentFrame,int maxFrame)
    {
    	float upscalef = (float)currentFrame/(float)maxFrame;
    	float downscalef = 1.0f-upscalef;
    	for(int y=0;y<20;y++)
    	{
    		for(int x=0;x<10;x++)
    		{
    			
    			if(thegame.getGridValue(x, y)!=-1)
    			{
    				if(thegame.getPreviousGridValue(x, y)==-1)
    				{
        				Cube c = this.mCube[thegame.getGridValue(x, y)];
        				gl.glLoadIdentity();
        				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
        				c.draw(gl,upscalef);     					
    				}
    				else
    				{
    					Cube c = this.mCube[thegame.getGridValue(x, y)];
    					gl.glLoadIdentity();
    				
    					c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
    					c.draw(gl);
    				}
    			}
    			else
    			{
    				if(thegame.getPreviousGridValue(x, y)!=-1)
    				{
        				Cube c = this.mCube[thegame.getPreviousGridValue(x, y)];
        				gl.glLoadIdentity();
        				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
        				c.draw(gl,downscalef);   					
    				}
    				
    			}
    		}
    	}
    } 
    protected void drawBlocks(GL10 gl)
    {
    	int[] clearedlines = game.getClearedLines();
    	
    	for(int y=0;y<20;y++)
    	{
    		boolean skipline=false;
    		if(clearedlines[y]==1)
    		{
    			skipline=true;
    		}
    		if(!skipline)
    		{
	    		for(int x=0;x<10;x++)
	    		{
	    			if(game.getGridValue(x, y)!=-1)
	    			{
	    				Cube c = this.mCube[game.getGridValue(x, y)];
	    				gl.glLoadIdentity();
	    				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
	    				c.draw(gl);
	    			}
	    		}
    		}
    	}
    }
    protected void drawFallingBlock(GL10 gl)
    {
    	int result = 0;
    	if(now-lastcalltime>demogame.getTimer() || game.getStatus()==SimpleGameData.STATUS_EVOLVING)
    	{
    		result=0;
    	}
    	else
    	{
    		result=(int)((now-lastcalltime)%demogame.getTimer());
    		result = (result*10)/demogame.getTimer();
    	}
    	float ystart=21.0f;
    	if(game.getCurrentBlock().color>=0 && game.getCurrentBlock().color<this.mCube.length)
    	{
    		Cube c = this.mCube[game.getCurrentBlock().color];
    	
    		for(int i=0;i<4;i++)
    		{
    			gl.glLoadIdentity();
    			if(result!=0 && game.canMoveBlockDown())
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart-(((float)result/10.0f)*2.0f),zoff);
    			}
    			else
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart,zoff);
    			}
    			c.draw(gl);
    		}
    	}
    }
    
    protected void drawCubeExplosion(GL10 gl)
    {
    	
    	java.util.ListIterator<ExplodingCube > iter=this.explodingCubes.listIterator();
    	ExplodingCube c = null;
    	long now = System.currentTimeMillis();
    	if(iter!=null)
    	{
    		
    		while(iter.hasNext())
    		{
    			
    			c=iter.next();
    			if(c.frame>MAX_EXPLOSION_FRAME)
    			{
    				
    				iter.remove();
    			}
    			else
    			{
    				float elapsedTime = (now- c.startTime)/1000.0f;
    				drawExplodingCube(gl, c);
    				switch(c.explosionType)
    				{
    				case 1:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;

    					break;
    				case 2:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;
    					
    					break;
    				case 3:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;
    					break;
    				case 4:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;
    					break;
    					default:
        				c.x = c.x + c.ux*elapsedTime;
        				c.y = c.y + c.uy*elapsedTime;
        				c.z = c.z + c.uz*elapsedTime;
        				c.uy = c.uy+elapsedTime*Y_ACCELERATION;

    						break;
    				}
    				c.frame++;
    			}
    		}
    	}
    }
    public void createExplosions(Game game)
    {
    	int[] clearedLines = game.getClearedLines();
    	int linecount=game.getClearedLineCount();
    	for (int y=0;y<clearedLines.length;y++)
    	{
    		if (clearedLines[y]==1)
    		{
    			for(int x=0;x<10;x++)
    			{
    				ExplodingCube c = new ExplodingCube
    				(
    						xoff+x*2.0f,
    						yoff-y*2.0f,
    						zoff,
    						(randomgen.nextFloat()-0.5f)/4.0f,
    						(randomgen.nextFloat()-0.5f)*2,
    						(randomgen.nextFloat()-0.5f)/4.0f,
    						game.getGridValue(x, y),
    						0
    						
    				);
    				switch(linecount)
    				{
    				case 1:
    					c.explosionType=0;
    					break;
    				case 2:
    					c.explosionType=1;
    					break;
    				case 3:
    					c.explosionType=2;
    					break;
    					
    				case 4:
    					c.explosionType=3;
    					break;
    				default:
    					c.explosionType=0;
    					break;
    				}
    				c.explosionType=0;
    				this.explodingCubes.add(c);
    			}
        		android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_EXPLOSION);
        		message.sendToTarget();
    		}
    	}
    }
    /*
     * Draw the playfield for the tetris field
     * 
     */
    protected void drawPlayfield(GL10 gl)
    {

    	/*
    	for(int i=0;i<20;i++)
    	{
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(xoff-2.0f,yoff-i*2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(xoff+2.0f*10,yoff-i*2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    	}
    	for(int i=0;i<10;i++)
    	{
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(xoff+i*2.0f,yoff-(20*2.0f), zoff);
    		mPlayfieldCube.draw(gl);
    		
    	}
    	*/
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff-2.0f,yoff-9.5f*2.0f, zoff);
		mPlayfieldCube.draw(gl,1.0f, 20.0f,1.0f);
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff+2.0f*10,yoff-9.5f*2.0f, zoff);
		mPlayfieldCube.draw(gl,1.0f,20.0f,1.0f);
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff+4.5f*2.0f,yoff-(20*2.0f), zoff);
		mPlayfieldCube.draw(gl,10.0f,1.0f,1.0f);
		
		
		
    }
    public void setupDemoGrid()
    {
    	java.util.Random randomgen = new java.util.Random();
    	int result;
    	for (int y=0;y<20;y++)
    	{
    		for (int x=0;x<10;x++)
    		{
    			result = randomgen.nextInt(21);
    			if(result<7)
    			{
    			
    				this.demogame.setGridValue(x, y, result % 6);
    			}
    			else
    			{
    				this.demogame.setGridValue(x, y, -1);
    			}
    		}
    	}
    }    
    protected void drawIntroScreen(GL10 gl,Canvas canvas, int w, int h)
    {
    	long now = SystemClock.uptimeMillis();
    	if(demogame.getStatus()!=SimpleGameData.STATUS_EVOLVING)
    	{
    		demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
    	}
    	if(demogame.getCurrentStep()>50)
    	{
    		setupDemoGrid();
    		demogame.setEnergy(100);
    		demogame.setStep(0);
    	}
    	int result = 0;
    	if(now-lastcalltime>demogame.getTimer())
    	{
    		result=10;
    	}
    	else
    	{
    		result=(int)((now-lastcalltime)%demogame.getTimer());
    		result = (result*10)/demogame.getTimer();
    	}
    	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);
    	//canvas.drawText("energy="+demogame.getEnergy()+" result="+org.teacake.util.FixedPointFloat.floatToFixedPoint(0.25f), 10, 10, paint);
    	this.drawBlocks(gl,demogame,result,10);

    	//lastdrawtime = System.currentTimeMillis();
    	//this.drawBlocks(gl, demogame);
    	
    	
    }
    protected void drawIntroScreen(GL10 gl,int w, int h)
    {
    	long now = SystemClock.uptimeMillis();
    	if(demogame.getStatus()!=SimpleGameData.STATUS_EVOLVING)
    	{
    		demogame.setStatus(SimpleGameData.STATUS_EVOLVING);
    	}
    	if(demogame.getCurrentStep()>50)
    	{
    		setupDemoGrid();
    		demogame.setEnergy(100);
    		demogame.setStep(0);
    	}
    	int result = (int)((now-lastcalltime)%demogame.getTimer());
    	result = (result*10)/demogame.getTimer();
    	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);
    	//canvas.drawText("energy="+demogame.getEnergy()+" result="+org.teacake.util.FixedPointFloat.floatToFixedPoint(0.25f), 10, 10, paint);
		mMoon.setPosition(xoff+13,yoff-22, zoff-50);
		mMoon.draw(gl,28.0f,28.0f,1.0f);
    	this.drawBlocks(gl,demogame,result,10);
    	
    	//lastdrawtime = System.currentTimeMillis();
    	//this.drawBlocks(gl, demogame);
    	
    	
    }
    private void drawFrame(GL10 gl)
	{
		
	    
		if (running)
		{
			/*
			long current = SystemClock.uptimeMillis();
             
			if (mNextTime < current && action==MSG_DO_NOTHING)
			{
             	
                 mNextTime = current + 20;
                 return;
            }
			else
			{
				overlay.postInvalidate();
	         	rangle=rangle+2.0f;
	         	if(rangle>360.0f)
	         	{
	         		rangle=0.0f;
	         	}			
				
			}
			overlay.postInvalidate();
			*/
			long current = SystemClock.uptimeMillis();

			rangle=rangle+2.0f;
         	if(rangle>360.0f)
         	{
         		rangle=0.0f;
         	}			
         	overlay.postInvalidate(); 
            
        /*
         * First, we need to get to the appropriate GL interface.
         * This is simply done by casting the GL context to either
         * GL10 or GL11.
         */
         	//gl = (GL10)(mGLContext.getGL());

        /*
         * Before we can issue GL commands, we need to make sure all
         * native drawing commands are completed. Simply call
         * waitNative() to accomplish this. Once this is done, no native
         * calls should be issued.
         */
        //mGLContext.waitNative(canvas, this);
        //mGLContext.waitNative();
        //mGLContext.makeCurrent(view.getHolder());
            int w = view.getWidth();
            int h = view.getHeight();

            /*
             * Set the viewport. This doesn't have to be done each time
             * draw() is called. Typically this is called when the view
             * is resized.
             */

            
            gl.glViewport(0, 0, w, h);

            /*
             * Set our projection matrix. This doesn't have to be done
             * each time we draw, but usually a new projection needs to be set
             * when the viewport is resized.
             */

            float ratio = (float)w / h;
            /*
            if(w<h)
            {
            	ratio = (float)w/h;
            }
            else
            {
            	ratio = (float)(h/w);
            }
            */
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            //gl.glFrustumf(-ratio, ratio, -ratio, ratio, 2, 60);
            android.opengl.GLU.gluPerspective(gl, 60, ratio, 1, 100);
            /*
             * dithering is enabled by default in OpenGL, unfortunately
             * it has a significant impact on performance in software
             * implementation. Often, it's better to just turn it off.
             */
             gl.glDisable(GL10.GL_DITHER);

            /*
             * Usually, the first thing one might want to do is to clear
             * the screen. The most efficient way of doing this is to use
             * glClear(). However we must make sure to set the scissor
             * correctly first. The scissor is always specified in window
             * coordinates:
             */
            gl.glClearColor(0,0,0,0);
            
            
            gl.glEnable(GL10.GL_SCISSOR_TEST);
            gl.glScissor(0, 0, w, h);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glEnable(GL10.GL_CULL_FACE);
            //gl.glCullFace(GL10.GL_CW);
            gl.glShadeModel(GL10.GL_SMOOTH);
            //gl.glDepthFunc(GL10.GL_GREATER);
            //gl.glDepthRangef(1, 100);
            //gl.glDepthMask(false);
            gl.glEnable(GL10.GL_DEPTH_TEST);            

            /*
             * Now we're ready to draw some 3D object
             */
            
            
            gl.glScalef(0.5f, 0.5f, 0.5f);

            //gl.glRotatef(zy, 1, 0, 0);
            gl.glTranslatef(0, 0, zoff);
            if(this.viewType==VIEW_INTRO)
            {
            	gl.glRotatef(rangle, 0.0f, 0.0f, 1.0f);
            }
            gl.glRotatef(zx, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(zy, 1.0f, 0.0f, 0.0f);
            gl.glTranslatef(0,0,-zoff);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            

            
            gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            //int bg_width = background.width();
            //int bg_height = background.height();
            //int bg_new_width = 0;
            //int bg_new_height = 0;

            if(!this.backgroundInitialized)
            {
                //if(w>h)
                //{
                //	bg_new_width = bg_width;
                //	bg_new_height = (bg_height*h)/w;
                //}
                //else
                //{
                //	bg_new_height = bg_height;
                //	bg_new_width = (bg_width*w)/h;
                //}
            	//this.drawableBackground = android.graphics.Bitmap.createBitmap(this.background,(int)(bg_width-bg_new_width)/2,(int)(bg_height-bg_new_height)/2,(int)(bg_width-(bg_width-bg_new_width)/2)-1,(int)(bg_height-(bg_height-bg_new_height)/2-1),new android.graphics.Matrix(),false);
            	//this.drawableBackground = android.graphics.Bitmap.createBitmap(this.drawableBackground,(this.drawableBackground.width()-w-1)/2,(this.drawableBackground.height()-h-1)/2,w,h);
            	//this.drawableBackground = android.graphics.Bitmap.createBitmap(this.background,(bg_width-w)/2-1, (bg_height-h)/2-1,w+1,h+1,new android.graphics.Matrix(),false);
            	this.backgroundInitialized = true;
            }
            //android.graphics.Rect rect = new android.graphics.Rect(0,0,drawableBackground.width()-1,drawableBackground.height()-1);
            //canvas.drawBitmap(drawableBackground, rect, rect, bgpaint);
            //canvas.drawText("width ="+drawableBackground.width()+" height="+this.drawableBackground.height()+" new_w="+bg_new_width+" new_h="+bg_new_height,10,300,  paint);
            //canvas.drawText(message, 10, 10, paint);
            if (this.viewType==VIEW_INTRO)
            {
	            now = SystemClock.uptimeMillis();
	            if(now>lastcalltime+demogame.getTimer())
	            {

	            		lastcalltime = now;
	            		this.demogame.gameLoop();
	            }
	            this.drawIntroScreen(gl,w,h);
            	//this.drawIntroScreen(gl, canvas, w, h);
            	String logo="MonolithAndroid";

            	//int textxpos = this.getTextWidth(logo,this.gameOverPaint);
            	//canvas.drawText(logo, (getWidth()-textxpos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
            	
            }
            if (this.viewType==VIEW_GAME)
            {
            	if (action == MSG_ROTATE)
        		{
        			action=MSG_DO_NOTHING;
        			doRotateBlock();
        		}
        		if (action == MSG_MOVE_LEFT)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveLeft();
            	
        		}
        		if (action == MSG_MOVE_RIGHT)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveRight();
        		}
        		if (action == MSG_MOVE_DOWN)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveDown();
        		}            	

        		
        		gl.glLoadIdentity();
            	
            	
            	//gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        		mMoon.setPosition(xoff+13,yoff-22, zoff-50);
        		
        		mMoon.draw(gl,40.0f,40.0f,1.0f);
        		gl.glLoadIdentity();
        		//gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        		mEarth.setPosition(xoff+13,yoff-2, zoff+60);
        		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        		mEarth.draw(gl,10.0f,10.0f,1.0f);
            	gl.glLoadIdentity();
            	mStarfield.draw(gl,0,rangle);        		
        		drawPlayfield(gl);
                drawFallingBlock(gl);

                drawNextPiece(gl);

        		//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        		
        		//gl.glPopMatrix();
                //this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
                //this.drawString(canvas,""+game.getScore(), 10, 34);
                //this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
                //this.drawString(canvas,""+game.getLevel(), 10, 74);
                //this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
                //this.drawString(canvas,""+game.getLines(),10,114);
        		this.overlay.setLevel(""+game.getLevel());
        		this.overlay.setScore(""+game.getScore());
        		this.overlay.setLines(""+game.getLines());
	            if(this.gametype==Monolith.GAME_MONOLITH)
	            {
	            	
	            	this.overlay.setEnergy(""+game.getEnergy());
	            	//this.drawString(canvas,res.getString(R.string.s_energy),10,134);
	            	//this.drawString(canvas,""+game.getEnergy(),10,154);

	            }
	            //canvas.drawText("zx="+zx+" zy="+zy,10,134,paint);
	            switch (game.getStatus())
	            {
	            case SimpleGameData.STATUS_GAME_OVER:
	            	this.overlay.setMessage("Game Over");
	            	break;
	            case SimpleGameData.STATUS_EVOLVING:
	            	this.overlay.setMessage("Evolving...");
	            	break;
	            	default:
	            		this.overlay.setMessage("");	
	            		break;
	            }

	            
	            Cube c = this.mCube[0];
	            c.setPosition(0.0f, 0.0f, -30.0f);
	            mAngle += 1.2f;
	            
	            now = SystemClock.uptimeMillis();
	            if(game.getStatus()==SimpleGameData.STATUS_PLAYING || game.getStatus()==SimpleGameData.STATUS_EVOLVING)
	            {
	            	this.drawCubeExplosion(gl);
	            }
	            if(now>lastcalltime+game.getTimer())
	            {
	            	if(game.getStatus()==SimpleGameData.STATUS_PLAYING || game.getStatus()==SimpleGameData.STATUS_EVOLVING)
	            	{
	            		game.gameLoop();
	            		if(game.isBlockPlaced())
	            		{
	                		android.os.Message message = android.os.Message.obtain(soundSystem.messageHandler, SoundSystem.SOUND_PLAY_PLACE_BLOCK);
	                		message.sendToTarget();   	            			
	            		}
	            		game.flagCompletedLines();
	            		this.createExplosions(game);
	            		lastcalltime = now;
	            	}
	            }
                if(game.getStatus()==SimpleGameData.STATUS_EVOLVING)
                {
                	int result=10;
                	long now = SystemClock.uptimeMillis();;
                	if(now-lastcalltime>game.getTimer())
                	{
                		result=10;
                	}
                	else
                	{
                		result = (int)((now-lastcalltime)%game.getTimer());
                		result = (result*10)/game.getTimer();
                	}
            	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);

            		this.drawBlocks(gl,game,result,10);
                }
                else
                {
                 	drawBlocks(gl);
                }	            
	            gl.glPopMatrix();
	            //game.gameLoop();
            }

        /*
         * Once we're done with GL, we need to flush all GL commands and
         * make sure they complete before we can issue more native
         * drawing commands. This is done by calling waitGL().
         */
        //mGLContext.waitGL();
        }
    }


    // ------------------------------------------------------------------------

    private static final int INVALIDATE = 1;


    
	public static final int VIEW_INTRO=0;
	public static final int VIEW_GAME=1;
	public static final int MSG_DO_NOTHING=-1;
	public static final int MSG_ROTATE=0;
	public static final int MSG_MOVE_LEFT=1;
	public static final int MSG_MOVE_RIGHT=2;
	public static final int MSG_MOVE_DOWN=3;
	public static final int MSG_ROTATE_PLAYFIELD=4;
    public Game	game;
    public Game demogame;
    private EGLContext      mGLContext;
    private Starfield		mStarfield;
    private Cube[]          mCube;
    private Cube 			mPlayfieldCube;
    private Square			mMoon;
    private Square			mEarth;
    private float           mAngle;
    private long            mNextTime;
    private boolean         mAnimate;
    private float			rangle;
    
    private int xval;
    private int yval;
    public float zx=0.0f;
    public float zy=0.0f;
    private float xoff;
    private float yoff;
    private float zoff;
    private long now;
    private long lastcalltime;
    private Paint paint;
    private Paint paint2;
    private Paint bgpaint;
    private Paint gameOverPaint;
    private int gameOverXPos;
    private int evolvingXPos;
    public boolean running;
    private Resources res;
    int gametype;
    private int viewType;
    //private javax.sound.midi.AndroidMIDIPlayBackEngine soundEngine;
    private android.media.MediaPlayer mediaPlayer;
    public String message;
    private java.util.LinkedList<ExplodingCube> explodingCubes;
    public static final float Y_ACCELERATION=-0.3f;
    public static final float Z_ACCELERATION=0.3f;
    public static final int MAX_EXPLOSION_FRAME=100;
    public int mWidth ;
    public int mHeight ;
    public boolean mSizeChanged = true;
    private long lastdrawtime;
    private java.util.Random randomgen;
    private android.graphics.Bitmap background;
    private android.graphics.Bitmap drawableBackground;
    private boolean backgroundInitialized;
    private int steps;
    private GameOverlay overlay;
    public int action;
    private SoundSystem soundSystem;
    private android.content.Context context;
    public final Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
        	try
        	{
        		action=msg.what;
        		
        		if (msg.what == MSG_ROTATE_PLAYFIELD)
        		{
        			doRotatePlayfield(msg.arg1,msg.arg2);
        				//zx=msg.arg1;
        				//zy=msg.arg2;
        			
        		}
        	}
        	catch(Exception e)
        	{
        		
        	}
        }
    };
	
	private GLTextures textures;
}