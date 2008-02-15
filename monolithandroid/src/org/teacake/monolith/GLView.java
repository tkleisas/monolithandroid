package org.teacake.monolith;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.OpenGLContext;
import android.graphics.Paint;
//import android.os.Bundle;
import android.content.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;


import javax.microedition.khronos.opengles.GL10;
class ExplodingCube
{
	public static final int EXPLOSION_TYPE_NORMAL=0;
	public static final int EXPLOSION_TYPE_SINGLE=1;
	public static final int EXPLOSION_TYPE_SPIRAL=2;
	public static final int EXPLOSION_TYPE_SPHERE=3;
	public ExplodingCube(float x,float y,float z,float ux,float uy, float uz, int blocktype,int frame)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		this.ux = ux;
		this.uy = uy;
		this.uz = uz;
		this.blocktype = blocktype;
		this.frame = frame;
		this.anglex = 0.0f;
		this.angley = 0.0f;
		this.anglez = 0.0f;
		this.uax = 0.0f;
		this.uay = 0.0f;
		this.uaz = 0.0f;
		this.explosionType = EXPLOSION_TYPE_NORMAL;
	}
	public ExplodingCube(float x,float y,float z,float ux,float uy, float uz, float anglex,float angley, float anglez, float uax, float uay, float uaz, int blocktype,int frame)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.ux = ux;
		this.uy = uy;
		this.uz = uz;
		this.blocktype = blocktype;
		this.frame = frame;
		this.anglex = 0.0f;
		this.angley = 0.0f;
		this.anglez = 0.0f;
		this.uax = 0.0f;
		this.uay = 0.0f;
		this.uaz = 0.0f;		
	}
	public float x;
	public float y;
	public float z;
	public float ux;
	public float uy;
	public float uz;
	public float anglex; //rotation around x axis;
	public float angley; //rotation around y axis;
	public float anglez; //rotation around z axis;
	public float uax; //angular velocity around x
	public float uay; //angular velocity around y
	public float uaz; //angular velocity around z
	public int blocktype;
	public int frame;
	public int explosionType;
	
}
public class GLView extends View
{
	public static final int VIEW_INTRO=0;
	public static final int VIEW_GAME=1;
    /**
     * Standard override to get key events.
     */
    /**
     * The View constructor is a good place to allocate our OpenGL context
     */
    public GLView(Context context,int gametype)
    {
        super(context);

        /* 
         * Create an OpenGL|ES context. This must be done only once, an
         * OpenGL context is a somewhat heavy object.
         */
        mGLContext = new OpenGLContext(OpenGLContext.DEPTH_BUFFER );
        //mCube = new Cube(0x10000,0,0,0x10000);
        mCube = new Cube[8];
        mCube[0] = new Cube(0xff00,0,0,0x10000);
        mCube[1] = new Cube(0,0xff00,0,0x10000);
        mCube[2] = new Cube(0,0,0xff00,0x10000);
        mCube[3] = new Cube(0xff00,0xff00,0,0x10000);
        mCube[4] = new Cube(0xff00,0,0xff00,0x10000);
        mCube[5] = new Cube(0,0xff00,0xff00,0x10000);
        mCube[6] = new Cube(0xf000,0xf0000,0,0x10000);
        mCube[7] = new Cube(0xffff,0x0ffff,0xffff,0x00ff);
        	
        res = context.getResources();
        this.mPlayfieldCube = new Cube(0x8000,0x8000,0x8000,0x0);
        running=false;
        this.gametype = gametype;
        this.setViewType(VIEW_INTRO);
        this.explodingCubes = new java.util.LinkedList<ExplodingCube>();
        randomgen = new java.util.Random(SystemClock.uptimeMillis());
        this.background = android.graphics.BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        this.backgroundInitialized = false;
        
        
        
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
    public void performCleanup()
    {
    	
    	try
    	{
    		this.mediaPlayer.stop();
    	}
    	catch(Exception e)
    	{
    		
    	}
    	this.running = false;
    }
    public void doInit()
    {
    	if(viewType==VIEW_INTRO)
    	{
    		try
    		{
        	//soundEngine = javax.sound.sampled.AndroidPlayBackEngine.getInstance();
        	
    			mediaPlayer = android.media.MediaPlayer.create(this.getContext(), R.raw.intro);
        	
    			mediaPlayer.prepare();
    			mediaPlayer.setLooping(1);
    			mediaPlayer.start();
    		}
    		catch(Exception e)
    		{
    			Log.e("music","error"+e.getMessage());
    		} 
    	}
    	if(viewType==VIEW_GAME)
    	{
    		try
    		{
    			
    		}
    		catch(Exception e)
    		{
    			mediaPlayer.stop();
    		}
    	}
    	
    	xval = 0;
    	yval =0;
        zx=0.0f;
        zy=0.0f;
        xoff = -10.0f;
        //-10.0f+x*2.0f, 21.0f-y*2.0f, zoff
    	yoff = 21.0f;
    	zoff = -53.0f;

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
    	
    	

    	
        this.gameOverXPos = getTextWidth(res.getString( R.string.s_game_over),gameOverPaint);
        this.evolvingXPos = getTextWidth(res.getString( R.string.s_evolving ),gameOverPaint);
    	
    }
    public int getTextWidth(String str,Paint paint)
    {
    	float widths[] =new float[str.length()];
    	paint.getTextWidths(str,widths); 
    	int totalwidth=0;
    	for(int i=0;i<widths.length;i++)
    	{
    		totalwidth+=widths[i];
    	}
    	return totalwidth;
    }
    public void drawString(Canvas canvas,String str,int x,int y)
    {
    	canvas.drawText(str, x+1, y+1, paint2);
    	canvas.drawText(str, x, y, paint);
    }
    public void doMoveDown()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}
    	game.moveBlockDown();
    
    	game.gameLoop();
    	game.flagCompletedLines();
    	this.createExplosions(game);
    }
    public void doMoveLeft()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

    	game.moveBlockLeft();
    }
    public void doMoveRight()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

    	game.moveBlockRight();
    
    }
    public void doRotateBlock()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}

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
    	for(int y=0;y<20;y++)
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
    protected void drawFallingBlock(GL10 gl)
    {
    	float ystart=21.0f;
    	if(game.getCurrentBlock().color>=0 && game.getCurrentBlock().color<this.mCube.length)
    	{
    		Cube c = this.mCube[game.getCurrentBlock().color];
    	
    		for(int i=0;i<4;i++)
    		{
    			gl.glLoadIdentity();
    			c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart,zoff);
    			c.draw(gl);
    		}
    	}
    }
    
    protected void drawCubeExplosion(GL10 gl)
    {
    	
    	java.util.ListIterator<ExplodingCube > iter=this.explodingCubes.listIterator();
    	ExplodingCube c = null;
    	if(iter!=null)
    	{
    		
    		while(iter.hasNext())
    		{
    			c=iter.next();
    			if(c.frame>GLView.MAX_EXPLOSION_FRAME)
    			{
    				
    				iter.remove();
    			}
    			else
    			{
    				drawExplodingCube(gl, c);
    				c.x = c.x + c.ux*c.frame;
    				c.y = c.y + c.uy*c.frame;
    				c.z = c.z + c.uz*c.frame;
    				c.uz = c.uz+c.frame*Z_ACCELERATION;
    				c.frame++;
    			}
    		}
    	}
    }
    public void createExplosions(Game game)
    {
    	int[] clearedLines = game.getClearedLines();
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
    						(x-5.0f)/10.0f,
    						randomgen.nextFloat()*2.0f+0.2f,
    						randomgen.nextFloat()*2.0f+0.2f,
    						game.getGridValue(x, y),
    						0
    						
    				);
    				this.explodingCubes.add(c);
    			}
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
    /*
     * Start the animation only once we're attached to a window
     * @see android.view.View#onAttachedToWindow()
     */
    @Override
    protected void onAttachedToWindow() {
        mAnimate = true;
        Message msg = mHandler.obtainMessage(INVALIDATE);
        mNextTime = SystemClock.uptimeMillis();
        mHandler.sendMessageAtTime(msg, mNextTime);
        super.onAttachedToWindow();
    }

    /*
     * Make sure to stop the animation when we're no longer on screen,
     * failing to do so will cause most of the view hierarchy to be
     * leaked until the current process dies.
     * @see android.view.View#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        mAnimate = false;
        super.onDetachedFromWindow();
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
    	int result = (int)((now-lastcalltime)%demogame.getTimer());
    	result = (result*10)/demogame.getTimer();
    	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);
    	//canvas.drawText("energy="+demogame.getEnergy()+" result="+org.teacake.util.FixedPointFloat.floatToFixedPoint(0.25f), 10, 10, paint);
    	this.drawBlocks(gl,demogame,result,10);
    	
    	//lastdrawtime = System.currentTimeMillis();
    	//this.drawBlocks(gl, demogame);
    	
    	
    }
    /**
     * Draw the view content
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override

    protected void onDraw(Canvas canvas) {
        if (running) {
        /*
         * First, we need to get to the appropriate GL interface.
         * This is simply done by casting the GL context to either
         * GL10 or GL11.
         */
        GL10 gl = (GL10)(mGLContext.getGL());

        /*
         * Before we can issue GL commands, we need to make sure all
         * native drawing commands are completed. Simply call
         * waitNative() to accomplish this. Once this is done, no native
         * calls should be issued.
         */
        //mGLContext.waitNative(canvas, this);
        mGLContext.waitNative();
        mGLContext.makeCurrent(this);
            int w = getWidth();
            int h = getHeight();

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
            if(w>h)
            {
            	ratio = (float)w/h;
            }
            else
            {
            	ratio = (float)(h/w);
            }
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 2, 52);

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
            gl.glClearColor(1,1,1,1);
            
            
            gl.glEnable(GL10.GL_SCISSOR_TEST);
            gl.glScissor(0, 0, w, h);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glEnable(GL10.GL_CULL_FACE);
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
            int bg_width = background.width();
            int bg_height = background.height();
            int bg_new_width = 0;
            int bg_new_height = 0;

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
            	this.drawableBackground = android.graphics.Bitmap.createBitmap(this.background,(bg_width-w)/2-1, (bg_height-h)/2-1,w+1,h+1,new android.graphics.Matrix(),false);
            	this.backgroundInitialized = true;
            }
            android.graphics.Rect rect = new android.graphics.Rect(0,0,drawableBackground.width()-1,drawableBackground.height()-1);
            canvas.drawBitmap(drawableBackground, rect, rect, bgpaint);
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
            	this.drawIntroScreen(gl, canvas, w, h);
            	String logo="MonolithAndroid";
            	int textxpos = this.getTextWidth(logo,this.gameOverPaint);
            	canvas.drawText(logo, (getWidth()-textxpos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
            	
            }
            if (this.viewType==VIEW_GAME)
            {
            	
                drawPlayfield(gl);
                drawFallingBlock(gl);
                if(game.getStatus()==SimpleGameData.STATUS_EVOLVING)
                {
                	long now = SystemClock.uptimeMillis();;

                	int result = (int)((now-lastcalltime)%game.getTimer());
                	result = (result*10)/game.getTimer();
            	//canvas.drawText("result="+result+" now-lastcalltime="+(now-lastcalltime), 10, 10, paint);

            		this.drawBlocks(gl,game,result,10);
                }
                else
                {
                 	drawBlocks(gl);
                }
                drawNextPiece(gl);
                gl.glPopMatrix();
                this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
                this.drawString(canvas,""+game.getScore(), 10, 34);
                this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
                this.drawString(canvas,""+game.getLevel(), 10, 74);
                this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
                this.drawString(canvas,""+game.getLines(),10,114);

	            if(this.gametype==Monolith.GAME_MONOLITH)
	            {
	            	this.drawString(canvas,res.getString(R.string.s_energy),10,134);
	            	this.drawString(canvas,""+game.getEnergy(),10,154);

	            }
	            //canvas.drawText("zx="+zx+" zy="+zy,10,134,paint);
	            if(game.getStatus()==SimpleGameData.STATUS_GAME_OVER)
	            {
	
	            	canvas.drawText(res.getString(R.string.s_game_over), (getWidth()-this.gameOverXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
	            	
	            	
	            }
	            if(game.getStatus()==SimpleGameData.STATUS_EVOLVING)
	            {
	            	canvas.drawText(res.getString(R.string.s_evolving), (getWidth()-this.evolvingXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
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
	            		game.flagCompletedLines();
	            		this.createExplosions(game);
	            		lastcalltime = now;
	            	}
	            }
	            //gl.glPopMatrix();
	            //game.gameLoop();
            }

        /*
         * Once we're done with GL, we need to flush all GL commands and
         * make sure they complete before we can issue more native
         * drawing commands. This is done by calling waitGL().
         */
        mGLContext.waitGL();
        }
    }


    // ------------------------------------------------------------------------

    private static final int INVALIDATE = 1;

    private final Handler mHandler = new Handler() {
        @Override
                public void handleMessage(Message msg) {
            if (mAnimate && msg.what == INVALIDATE) {
                invalidate();
                msg = obtainMessage(INVALIDATE);
                long current = SystemClock.uptimeMillis();
                
                if (mNextTime < current) {
                	
                    mNextTime = current + 20;
                }
                
                sendMessageAtTime(msg, mNextTime);
                mNextTime += 20;
            	rangle=rangle+1.0f;
            	if(rangle>360.0f)
            	{
            		rangle=0.0f;
            	}
            }
        }
    };
    
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
    		}
    		handled=true;
    	}
    	if(action==MotionEvent.ACTION_MOVE)
    	{
            zx = zx+((int)event.getX()-xval);
            zy = zy+((int)event.getY()-yval);
      	  	xval=(int)event.getX();
      	  	yval=(int)event.getY();
      	  	handled = true;
    	}

        return handled;
    } 
    public void setViewType(int viewtype)
    {
    	this.viewType = viewtype;
    }
    public Game	game;
    public Game demogame;
    private OpenGLContext   mGLContext;
    private Cube[]          mCube;
    private Cube 			mPlayfieldCube;
    private float           mAngle;
    private long            mNextTime;
    private boolean         mAnimate;
    private float			rangle;
    private int xval;
    private int yval;
    private float zx=0.0f;
    private float zy=0.0f;
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
    public static final float Z_ACCELERATION=1.0f;
    public static final int MAX_EXPLOSION_FRAME=20;
    private javax.sound.sampled.AndroidPlayBackEngine soundEngine;
    private long lastdrawtime;
    private java.util.Random randomgen;
    private android.graphics.Bitmap background;
    private android.graphics.Bitmap drawableBackground;
    private boolean backgroundInitialized;
    private int steps;
}


