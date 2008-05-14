package org.teacake.monolith;

import android.view.View;
import android.content.Context;
import android.graphics.*;
import android.content.Resources;
public class GameOverlay extends View {
	public GameOverlay(Context context)
	{
		super(context);
		this.overlayType = OVERLAY_TYPE_GAME_MONOLITH;
		res = context.getResources();
		gameOverPaint = new Paint();
        gameOverPaint.setARGB(128, 20, 20, 20);
        gameOverPaint.setTextSize(30);
        statusTextPaint1 = new Paint();
        statusTextPaint2 = new Paint();
        statusTextPaint1.setARGB(200, 255, 0, 0);
        statusTextPaint1.setTextSize(14);
        statusTextPaint2.setTextSize(14);
        statusTextPaint2.setARGB(255, 128, 128, 128);   	
        score="0";
        level="1";
        hiscore="0";
        lines="0";
        energy="0";
        message="monolith android";
        

    	

		
	}
	@Override protected void onDraw(Canvas canvas)
	{
        this.gameOverXPos = getTextWidth(res.getString( R.string.s_game_over),gameOverPaint);
        this.evolvingXPos = getTextWidth(res.getString( R.string.s_evolving ),gameOverPaint);
	
		switch (overlayType)
		{
		case OVERLAY_TYPE_INTRO:
			drawIntroOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME_CLASSIC:
			drawClassicGameOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME_MONOLITH:
			drawMonolithGameOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME_PUZZLE:
			drawPuzzleGameOverlay(canvas);
			break;
		}
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
	private void drawIntroOverlay(Canvas canvas)
	{
		String logo="MonolithAndroid";
    	int textxpos = this.getTextWidth(logo,this.gameOverPaint);
    	canvas.drawText(logo, (getWidth()-textxpos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);

	}
    public void drawString(Canvas canvas,String str,int x,int y)
    {
    	canvas.drawText(str, x+1, y+1, statusTextPaint2);
    	canvas.drawText(str, x, y, statusTextPaint1);
    }	
	private void drawClassicGameOverlay(Canvas canvas)
	{
        this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
        this.drawString(canvas,""+score, 10, 34);
        this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
        this.drawString(canvas,""+level, 10, 74);
        this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
        this.drawString(canvas,""+lines,10,114);
        if(message.equals("Game Over"))
        {
        	canvas.drawText(res.getString(R.string.s_game_over), (getWidth()-this.gameOverXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }

    }
	private void drawMonolithGameOverlay(Canvas canvas)
	{
        this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
        this.drawString(canvas,""+score, 10, 34);
        this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
        this.drawString(canvas,""+level, 10, 74);
        this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
        this.drawString(canvas,""+lines,10,114);
    	this.drawString(canvas,res.getString(R.string.s_energy),10,134);
    	this.drawString(canvas,this.energy,10,154);		
        if(message.equals("Game Over"))
        {
        	canvas.drawText(res.getString(R.string.s_game_over), (getWidth()-this.gameOverXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }
        if(message.equals("Evolving..."))
        {
        	canvas.drawText(res.getString(R.string.s_evolving), (getWidth()-this.evolvingXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }
	}
	private void drawPuzzleGameOverlay(Canvas canvas)
	{
		
	}
	private String score;
	public void setScore(String score)
	{
		this.score = score;
	}
	private String hiscore;
	public void setHiscore(String hiscore)
	{
		this.hiscore = hiscore;
	}
	private String level;
	public void setLevel(String level)
	{
		this.level = level;
	}
	private String energy;
	public void setEnergy(String energy)
	{
		this.energy = energy;
	}
	private String lines;
	public void setLines(String lines)
	{
		this.lines = lines;
	}
	private String message;
	public void setMessage(String message)
	{
		this.message = message;
	}
	private int overlayType;
	public void setOverlayType(int overlayType)
	{
		this.overlayType = overlayType;
	}
	private Paint gameOverPaint;
	private Paint statusTextPaint1;
	private Paint statusTextPaint2;
	private int gameOverXPos;
	private int evolvingXPos;
	private Resources res;
	public static final int OVERLAY_TYPE_INTRO = 0;
	public static final int OVERLAY_TYPE_GAME_CLASSIC=1;
	public static final int OVERLAY_TYPE_GAME_MONOLITH=2;
	public static final int OVERLAY_TYPE_GAME_PUZZLE=3;
}
