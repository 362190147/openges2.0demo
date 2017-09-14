package com.cjw.element;


import java.util.Random;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.cjw.util.GLObj;

public class Butterflies extends GLObj implements GLObj.Render{
	private float[] mMatrix;
	private float[] mMatrix2;
	private float[][] aMatrix=new float[2][];

	private float angle=0;
	public boolean turn;
	
	float[] colorBrue={0.0f,0.0f,1.0f,0.0f};
	
	private Random random=new Random();
	
	private Butterfly[] butterflies;
	public class Butterfly{
		private float x;
		private float y;
		private float z;
		private float targetX;
		private float targetY;
		private float targetZ;
		private float[] color;
		private float life;
		private float f;//方向
		
		Butterfly(float x, float y, float z){
			this.x	= x;
			this.y	= y;
			this.z	= z;
			this.life=100.0f;
			color=new float[4];
			color[0]=random.nextFloat();
			color[1]=random.nextFloat();
			color[2]=random.nextFloat();
			color[3]=0.0f;
		}
		
		public void setTarget( float x, float y, float z){
			targetX=x+random.nextFloat()*2-1;
			targetY=y+random.nextFloat()*2-1;
			targetZ=z+random.nextFloat()*2-1;;
		}
		
		public float moveC(float position,float target){
			if(Math.abs(position-target)>0.001f){
				if(position>target)
					position-=0.03f;
				else
					position+=0.03f;
				if(Math.abs(position-target)<0.03f)
					position=target;
			}
			return position;
		}
		
		public void move(){
			x=moveC(x,targetX);
			y=moveC(y,targetY);
			z=moveC(z,targetZ);
			
			if(turn){
				angle++;
				if(angle>50.0f) turn=false;
			}else{
				angle--;
				if(angle<0.0f) turn=true;
			}
			Matrix.translateM(mMatrix, 0, mMVPMatrix, 0, x, y, z);
			if(Math.abs(targetX-x)>0.1)
			if(targetX<x){
				if(f<180)
					f+=4;
			}else {
				if(f>0)
					f-=4;
			}
			Matrix.rotateM(mMatrix, 0, f, 0f, 1.0f, 0f);
			Matrix.rotateM(mMatrix, 0, -20-f/4.5f, 0, 0, 1.0f);
				
			Matrix.rotateM(mMatrix, 0, angle, 0.0f, 1.0f, 0.0f);
			Matrix.rotateM(mMatrix2, 0, mMatrix, 0, 2.0f*angle, 0.0f, -1.0f, 0.0f);
			
	        if(f>90f){
	        	aMatrix[0]=mMatrix;
	        	aMatrix[1]=mMatrix2;
	        }else{
	        	aMatrix[0]=mMatrix2;
	        	aMatrix[1]=mMatrix;
	        }
		}
		public void draw(float[] mMVPMatrix){
			//位置移动
			move();
			// 绑定纹理
			GLES20.glUseProgram(mProgram);
			glColor=color;
			EnableVertex();
			// 绑定纹理
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0].id);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "Texture"), 0);
			//利用两个矩阵分别画蝴蝶的两翼
	        for(float[] m:aMatrix){
	        	GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mProgram, "uMatrix"), 1, false, m, 0);
	        	GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	        }
	        // Disable vertex array
	       DisableVertex();
		}
		
		public void setPosition( float x, float y, float z){
			this.x	= x;
			this.y	= y;
			this.z	= z;
		}
		
	}
	
	
	public Butterflies(int MaxNum){
		final float [] ver1={
			-0.5f, -0.5f, 0.0f, 
			 0.0f, -0.5f, 0.0f, 
			-0.5f, 	0.5f, 0.0f, 
			 0.0f,  0.5f, 0.0f};
	
		final float [] tex1={
			0.0f, 1.0f,
			0.5f, 1.0f,
			0.0f, 0.0f, 
			0.5f, 0.0f};
		
		setVertices(ver1, 3, 0);
		setTexCoord(tex1, 0);
		mProgram = initShader(vertexCode, fragCode);
		getHandle(mProgram);
		mMatrix  = new float [16];//为矩阵分配空间
		mMatrix2 = new float [16];
		//初始化最大蝴蝶数目
		butterflies	=	new Butterfly[MaxNum];
		
	}
	
	/**
	 * 添加
	 * @param x 坐标
	 * @param y 坐标
	 * @param z 坐标
	 */
	public void add(float x,float y,float z){
		for(int i=0; i<butterflies.length; i++){
			if(butterflies[i] != null){
				if(butterflies[i].life>0)
					continue;
				else{
					butterflies[i]	= new Butterfly(x, y, z);
					break;
				}
			}else{
				butterflies[i]	= new Butterfly(x, y, z);
				break;
			}	
		}			
	}
	
	@Override
	public void draw(){
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glDepthMask(false);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
        
		for(int i=0; i<butterflies.length; i++){
			if(butterflies[i] != null){
				if(butterflies[i].life>0)
					butterflies[i].draw(mMVPMatrix);	
				}
			}
		//GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthMask(true);
		 GLES20.glDisable(GLES20.GL_BLEND);
	}
	
	public void setPosition( float x, float y, float z){
		for(int i=0; i<butterflies.length; i++){
			if(butterflies[i] != null){
				if(butterflies[i].life>0)
					butterflies[i].setTarget(x, y, z);	
				}
			}
	}
	
}
