package org.teacake.monolith;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class Monolith extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        view = new GLView( getApplication() );
        //setContentView(R.layout.main);
        setContentView(view);
    }
    GLView view;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        boolean handled = false;
        if(view.game!=null)
        {
        	if (view.game.getStatus()!= SimpleGameData.STATUS_PLAYING)
        	{
        		return handled;
        	}
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
        {
        	view.doMoveDown();
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
        {
        	view.doMoveLeft();
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
        	view.doMoveRight();
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_SPACE)
        {
        	view.doRotateBlock();
        	handled = true;
        }
        if(keyCode == KeyEvent.KEYCODE_ENDCALL)
        {
        	this.finish();
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
    

    
     
}