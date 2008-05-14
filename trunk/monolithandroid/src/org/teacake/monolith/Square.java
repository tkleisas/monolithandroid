package org.teacake.monolith;


	import java.nio.ByteBuffer;
	import java.nio.ByteOrder;
	import java.nio.IntBuffer;
	import android.graphics.*;
	import android.content.*;

	import javax.microedition.khronos.opengles.GL10;

	public class Square
	{
	    public Square()
	    {
	        
	    	int one = 0x10000;
	    	int texCoords[] = {
	    			
	    			//0, one, one, one, 0, 0, one, 0
	    			one, 0, one, one, 0, 0, 0, one
	    	};
	        
	        int vertices[] = {
	        		-one, -one, one, one, -one, one,
	        		-one, one, one, one, one, one
	               

	            };

	        int colors[] = {
	                one,    one,    one,  one,
	                one,    one,    one,  one,
	                one,    one,    one,  one,
	                one,    one,    one,  one,
	                  
	            };

	        byte indices[] = {
	                0, 1, 2, 1, 2, 3
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	    vbb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = vbb.asIntBuffer();
	        mVertexBuffer.put(vertices);
	        mVertexBuffer.position(0);

	    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
	    cbb.order(ByteOrder.nativeOrder());
	        mColorBuffer = cbb.asIntBuffer();
	        mColorBuffer.put(colors);
	        mColorBuffer.position(0);

	        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	        mIndexBuffer.put(indices);
	        mIndexBuffer.position(0);
	        
	    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
	    	tbb.order(ByteOrder.nativeOrder());
	    	mTextureBuffer = tbb.asIntBuffer();
	    	mTextureBuffer.put(texCoords);
	    	mTextureBuffer.position(0);
	    
	    }
	    public Square(int r, int g, int b, int a)
	    {
	        //int one = 0x10000;
	        int one = 0x10000;
	        int texCoords[] = {
	    			
	        		0, one, one, one, 0, 0, one, 0
	    	};
	        int vertices[] = {
	        		-one, -one, one, one, -one, one,
	        		-one, one, one, one, one, one


	    		            };
	        int colors[] = {
	                  r,      g,    b,  a,
	                  r >> 1, g >> 1,   b >> 1,  a,
	                  r >> 2, g >> 2,    b >> 2,  a,
	                  r,    g,    b,  a

	            };

	        byte indices[] = {
	        		0, 1, 2, 0, 2, 3
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

		    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
		    vbb.order(ByteOrder.nativeOrder());
		    mVertexBuffer = vbb.asIntBuffer();
		        mVertexBuffer.put(vertices);
		        mVertexBuffer.position(0);

		    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
		    cbb.order(ByteOrder.nativeOrder());
		        mColorBuffer = cbb.asIntBuffer();
		        mColorBuffer.put(colors);
		        mColorBuffer.position(0);

		        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		        mIndexBuffer.put(indices);
		        mIndexBuffer.position(0);
		        
			    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		    	tbb.order(ByteOrder.nativeOrder());
		    	mTextureBuffer = tbb.asIntBuffer();
		    	mTextureBuffer.put(texCoords);
		    	mTextureBuffer.position(0);       
	    }

	    
	    public Square(int r, int g, int b, int a, boolean color)
	    {
	        //int one = 0x10000;
	        int one = 0x10000;
	        int texCoords[] = {
	    			
	        		0, one, one, one, 0, 0, one, 0
	    	};
	        int vertices[] = {
	        		-one, -one, one, one, -one, one,
	        		-one, one, one, one, one, one


	    		            };


	        int colors[] = {
	                  r, g, b, a,
	                  r, g, b, a,
	                  r, g, b, a,
	                  r, g, b, a,

	            };

	        byte indices[] = {
	        		0, 1, 2, 2, 1, 3
	        };

	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	    //
	    // Buffers with multi-byte datatypes (e.g., short, int, float)
	    // must have their byte order set to native order

	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	    vbb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = vbb.asIntBuffer();
	        mVertexBuffer.put(vertices);
	        mVertexBuffer.position(0);

	    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
	    cbb.order(ByteOrder.nativeOrder());
	        mColorBuffer = cbb.asIntBuffer();
	        mColorBuffer.put(colors);
	        mColorBuffer.position(0);

	        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	        mIndexBuffer.put(indices);
	        mIndexBuffer.position(0);
	        
		    ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
	    	tbb.order(ByteOrder.nativeOrder());
	    	mTextureBuffer = tbb.asIntBuffer();
	    	mTextureBuffer.put(texCoords);
	    	mTextureBuffer.position(0);
	    	
	    }    
	    
	    static void loadTexture(GL10 gl, Context context, int resource)
	    {
	    	Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource);
	    	ByteBuffer bb = extract(bmp);
	    	load(gl, bb, bmp.width(), bmp.height());
	    			
	    }
	    private static ByteBuffer extract(Bitmap bmp)
	    {
	    	ByteBuffer bb = ByteBuffer.allocateDirect(bmp.height() * bmp.width() * 4);
	    	bb.order(ByteOrder.BIG_ENDIAN);
	    	IntBuffer ib = bb.asIntBuffer();
	    	// Convert ARGB -> RGBA
	    	for (int y = bmp.height() - 1; y > -1; y--)
	    	{
	    		
	    		for (int x = 0; x < bmp.width(); x++)
	    		{
	    			int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);
	    			int alpha = ((pix >> 24) & 0xFF);
	    			int red = ((pix >> 16) & 0xFF);
	    			int green = ((pix >> 8) & 0xFF);
	    			int blue = ((pix) & 0xFF);
	    			
	    			// Make up alpha for interesting effect
	    			
	    			//ib.put(red << 24 | green << 16 | blue << 8 | ((red + blue + green) / 3));
	    			ib.put(red << 24 | green << 16 | blue << 8 | alpha);
	    		}
	    	}
	    	bb.position(0);
	    	return bb;
	    }
	    private static void load(GL10 gl, ByteBuffer bb, int width, int height)
	    {
	    	// Get a new texture name
	    	int[] tmp_tex = new int[1];
	    	gl.glGenTextures(1, tmp_tex, 0);
	    	int tex = tmp_tex[0];
	    	// Load it up
	    	gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
	    	gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA,width, height, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
	    	gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
	    	gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	    }	    	
	    		
	    public void setPosition(float x, float y, float z)
	    {
	    	xpos = x;
	    	ypos = y;
	    	zpos = z;
	    }
	    public void draw(GL10 gl)
	    {
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glTranslatef(xpos, ypos, zpos);
	        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 4);
	        
	    }
	    public void draw(GL10 gl, float scale)
	    {
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glTranslatef(xpos, ypos, zpos);
	        gl.glScalef(scale, scale, scale);
	        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);    	
	    }
	    public void draw(GL10 gl, float scalex, float scaley, float scalez)
	    {
	        gl.glFrontFace(GL10.GL_CCW);
	    	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    	gl.glEnable(GL10.GL_TEXTURE_2D);
	        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	        gl.glNormal3f(0, 0, 1);
	        gl.glTranslatef(xpos, ypos, zpos);
	        
	        gl.glScalef(scalex, scaley, scalez);
	        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
	        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	       
	        gl.glDisable(GL10.GL_TEXTURE_2D);
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
	        //gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        //gl.glEnable(GL10.GL_TEXTURE_2D);
	        
	        //gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	        //gl.glDrawElements(GL10.GL_TRIANGLES,6 , GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	        //gl.glDisable(GL10.GL_TEXTURE_2D);
	        //gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    }
	    private IntBuffer   mVertexBuffer;
	    private IntBuffer   mColorBuffer;
	    private ByteBuffer  mIndexBuffer;
	    private IntBuffer  mTextureBuffer;
	    private float xpos;
	    private float ypos;
	    private float zpos;
	

}
