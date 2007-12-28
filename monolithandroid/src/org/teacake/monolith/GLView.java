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



import javax.microedition.khronos.opengles.GL10;
public class GLView extends View
{

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
        mCube[7] = new Cube(0,0x000,0x000,0x0000);
        	
        res = context.getResources();
        this.mPlayfieldCube = new Cube(0x8000,0x8000,0x8000,0x0,true);
        running=false;
        this.gametype = gametype;
    }
    /*
     * Draw the playfield for the tetris field
     * 
     */
    public void doInit()
    {
    	xval = 0;
    	yval =0;
        zx=0.0f;
        zy=0.0f;
    	yoff = -17.0f;
    	zoff = -48.0f;

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
        this.lastcalltime = SystemClock.uptimeMillis();
        rangle=0;
        paint = new Paint();
        paint.setARGB(200, 0, 0, 0);
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
    public void doMoveDown()
    {
    	if(game.getStatus()!=SimpleGameData.STATUS_PLAYING)
    	{
    		return;
    	}
    	game.moveBlockDown();
    	game.gameLoop();
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
    		gl.glTranslatef(16.0f, 0.0f, 0.0f);
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
    				c.setPosition(-10.0f+x*2.0f, 21.0f-y*2.0f, zoff);
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
    protected void drawPlayfield(GL10 gl)
    {

    	for(int i=0;i<20;i++)
    	{
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(-12.0f,yoff+i*2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(10.0f,yoff+i*2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    	}
    	for(int i=0;i<10;i++)
    	{
    		gl.glLoadIdentity();
    		mPlayfieldCube.setPosition(-10.0f+i*2.0f,yoff-2.0f, zoff);
    		mPlayfieldCube.draw(gl);
    		
    	}
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
        mGLContext.waitNative(canvas, this);

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
            //gl.glRotatef(rangle, 0, 1, 0);
            gl.glTranslatef(0,0,-zoff);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glLoadIdentity();
            

            
            gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            drawPlayfield(gl);
            drawFallingBlock(gl);
            drawBlocks(gl);
            drawNextPiece(gl);
            gl.glPopMatrix();
            
            canvas.drawText(res.getString(R.string.s_score), 10, 14,paint);
            canvas.drawText(""+game.getScore(), 10, 34, paint);
            canvas.drawText(res.getString(R.string.s_level), 10, 54, paint);
            canvas.drawText(""+game.getLevel(), 10, 74, paint);
            canvas.drawText(res.getString(R.string.s_lines), 10, 94, paint);
            canvas.drawText(""+game.getLines(),10,114,paint);
            if(this.gametype==Monolith.GAME_MONOLITH)
            {
            	canvas.drawText(res.getString(R.string.s_energy),10,134,paint);
            	canvas.drawText(""+game.getEnergy(),10,154,paint);
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
            if(now>lastcalltime+game.getTimer())
            {
            	if(game.getStatus()==SimpleGameData.STATUS_PLAYING || game.getStatus()==SimpleGameData.STATUS_EVOLVING)
            	{
            		game.gameLoop();
            		lastcalltime = now;
            	}
            }
            //gl.glPopMatrix();
            //game.gameLoop();

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
    public boolean onMotionEvent(MotionEvent event)
    {
    	int action = event.getAction();
    	if(action==MotionEvent.ACTION_DOWN)
    	{
    	  xval=(int)event.getX();
    	  yval=(int)event.getY();
    	}
    	if(action==MotionEvent.ACTION_UP)
    	{
    		
    	}
    	if(action==MotionEvent.ACTION_MOVE)
    	{
            zx = (zx-xval)/10.0f;
            zy = (zy-yval)/10.0f;
      	  	xval=(int)event.getX();
      	  	yval=(int)event.getY();
    	}

        return true;
    }   
    public Game	game;
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
    //private float xoff;
    private float yoff;
    private float zoff;
    private long now;
    private long lastcalltime;
    private Paint paint;
    private Paint gameOverPaint;
    private int gameOverXPos;
    private int evolvingXPos;
    public boolean running;
    private Resources res;
    int gametype;
    
}


