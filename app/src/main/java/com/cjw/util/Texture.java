package com.cjw.util;


import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture{
	public int id		= 0;	//纹理的引用
	public int width	= 0;	//纹理高度
	public int height	= 0; 	//纹理宽度
	
	public Texture(Bitmap bitmap){
		if(this.id==0){
			int[] textureId = new int[1];
			// Generate a texture object
			GLES20.glGenTextures(1, textureId, 0);
			if (textureId[0] != 0){
				this.id= textureId[0]; // TEXTURE_ID
			}else{
				throw new RuntimeException("Error loading texture.");
			}
		}
		this.width = bitmap.getWidth(); // TEXTURE_WIDTH
		this.height = bitmap.getHeight(); // TEXTURE_HEIGHT
		// Bind to the texture in OpenGL
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.id);
		// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		// Load the bitmap into the bound texture.
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		// Recycle the bitmap, since its data has been loaded into OpenGL.
		bitmap.recycle();
	}
	
	public void Change(Bitmap bitmap){
		this.width = bitmap.getWidth(); // TEXTURE_WIDTH
		this.height = bitmap.getHeight(); // TEXTURE_HEIGHT
		// Bind to the texture in OpenGL
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.id);
		// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		// Load the bitmap into the bound texture.
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		// Recycle the bitmap, since its data has been loaded into OpenGL.
		bitmap.recycle();
	}
	
	/**
	 * 删除纹理
	 */
	public void Delete(){
		if(id!=0){
			int t[]=new int[1];
			t[0]=id;
			GLES20.glDeleteTextures(1, t, 0);
			id=0;
		}
		
	}
	
	@Override
    protected void finalize() throws Throwable {
        try {
        	if(id!=0){
    			int t[]=new int[1];
    			t[0]=id;
    			GLES20.glDeleteTextures(1, t, 0);
    			id=0;
    		}
        } finally {
            super.finalize();
        }
    }
}