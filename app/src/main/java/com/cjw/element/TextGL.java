package com.cjw.element;



import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import com.cjw.util.GLObj;

public class TextGL extends GLObj{
	private int num = 0;
	float[] colorBrue={0.0f,0.0f,1.0f,0.0f};
	//private int current;
	Paint p=new Paint();
	
	public class TextAttribute{
		private String text = null;
		private float x	= 0f;
		private float y	= 0f;
		private float z = 0f;
		int mode		= 0;
		float[] color   = null;
		
		TextAttribute(String text, float x, float y, float z){
			setText(text, x, y, z);
		}
		
		public void setText(String text, float x,float y, float z){
			this.text=text;
			this.x=x;
			this.y=y;
			this.z=z;
			Bitmap bitmap = Bitmap.createBitmap((int)p.getTextSize()*text.length(), (int)p.getTextSize(), Config.ARGB_8888);
			
			Canvas c= new Canvas(bitmap);
			
			
			p.setColor(0xffff0000);
			c.drawText(text, 1, p.getTextSize()-1, p);
			
			p.setColor(0xffffffff);
			c.drawText(text, 0, p.getTextSize()-2, p);
			
			loadTexture(num, bitmap);
			num++;
		}
		
		public boolean hantei(float x, float y){
			float win[]=new float[3];
			float win2[]=new float[4];
			GLU.gluProject(this.x, this.y, this.z, mViewMatrix, 0, mProjectionMatrix, 0, viewport, 0, win, 0);
			GLU.gluUnProject(x, viewport[3]-y, win[2], mViewMatrix, 0, mProjectionMatrix, 0, viewport, 0, win2, 0);
			win2[0]/=win2[3];
			win2[1]/=win2[3];
			win2[2]/=win2[3];
			
			win[1]=height-win[1];
			if(Math.abs(this.x-win2[0])<1.0f 
			   && Math.abs(this.y-win2[1])<1.0f 
			   && Math.abs(this.z-win2[2])<1.0f
			   ){
				if(color==null)
					color=colorBrue;
				else
					color=null;
				return true;
			}else
				return false;
			
		}
		
		public void draw(){
			GLES20.glUseProgram(mProgram);
			//移动到适合的位置
			Matrix.translateM(mMatrix, 0, mMVPMatrix, 0, x, y, z);
			Matrix.scaleM(mMatrix, 0, text.length()/2.0f, 1.0f, 1.0f);
			glColor=color;
			//提交顶点属性
			EnableVertex();
			
			GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mProgram, "uMatrix")
					, 1, false, mMatrix, 0);
			//画图元
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
			//禁用顶点属性
			DisableVertex();
			
		}
	}
	ArrayList<TextAttribute> textList=new ArrayList<TextGL.TextAttribute>();
	
	public TextGL(){
		//设置默认顶点
		setVertices(ver, 3, 0);
		//设置默认纹理坐标
		setTexCoord(tex, 0);
		mProgram=defaultShader;
		getHandle(mProgram);
		
		setPaint();
	}
	
	public void setPaint() {
		p.setAntiAlias(true);
		p.setTextSize(32);
	}
	public void draw(){
		//使用混合
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glDepthMask(false);
		for(int i=0;i<textList.size();i++){
			//纹理
			if(texture!=null){
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[i].id);
				GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "texture"), 0);
			}
			textList.get(i).draw();
			
		}
		//禁用混合
		GLES20.glDepthMask(true);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
	
	public void add(String text, float x, float y, float z ) {
		textList.add(new TextAttribute(text, x, y, z));
	}
	
	public void hantei(float x, float y){
		for(int i=0;i<textList.size();i++){
			if(textList.get(i).hantei(x, y));
				//textList.remove(i);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		
		super.finalize();
	}
	
}
