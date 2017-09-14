package com.cjw.element;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cjw.util.GLObj;

public class BackGround extends GLObj{
	
	public BackGround(){
		setVertices(ver, 3, 0);
		setTexCoord(tex, 0);
		mProgram=defaultShader;
		getHandle(mProgram);
	}
	
	
	public void move(){
		float f=far/near;
		//
		Matrix.scaleM(mMatrix, 0, mMVPMatrix, 0, 
				size*2*f*width/height, size*2*f, 1.0f);
		Matrix.translateM(mMatrix, 0, 0.0f, 0.0f, -3.0f);
	}
	
	public void draw(){
		GLES20.glUseProgram(mProgram);
		EnableVertex();
		//矩阵转换
		move();
		
		// 绑定纹理
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0].id);
		GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "Texture"), 0);
		
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mProgram, "uMatrix"), 1, false, mMatrix, 0);
		// Draw
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
		DisableVertex();
	}
}
