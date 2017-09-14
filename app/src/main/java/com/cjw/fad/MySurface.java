package com.cjw.fad;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RemoteViews.RemoteView;

import com.cjw.game.Game;
import com.cjw.util.GLObj;

import com.cjw.util.Log;
import com.cjw.util.Sound;

@RemoteView
public class MySurface extends GLSurfaceView implements GLSurfaceView.Renderer{

	/**投影矩阵*/
	public float[] mProjectionMatrix	= new float[16];
	/**视图矩阵*/
	public float[] mViewMatrix		 	= new float[16];
	/**投影乘于视图矩阵*/
	public float[] mMVPMatrix			= new float[16];
	/**摄像机位置*/
	public float[] eye					= new float[3];
	/**摄像机目标*/
	public float[] center			 	= new float[3];
	/**摄像机目标*/
	public float[] up					= new float[3];
	
	/**触屏的模式*/
	int MotionMode=0;
	float sx[] = new float[10];
	float sy[] = new float[10];
	float t;
	
	float[] xyz=new float[4];
	
	//声音
	Sound sound;
	private Game game;
	
	public MySurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		setEGLContextClientVersion(2);
		setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		
		setRenderer(this);
	}
	

	public void onPause() {
		super.onPause();
		//game.gamePause();
	}
	
	public void gamePause(){
		game.gamePause();
	}
	
	public void Resume() {
		super.onResume();
		if(game!=null)
			game.Resume();
	}
	
	public void Stop() {
		game.gameEnd();	
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		eye[0]	= 0.0f;
		eye[1]	= 0.0f;
		eye[2]	= 5.0f;
		center[0] = 0.0f;
		center[1] = 0.0f;
		center[2] = 0.0f;
		up[0]=0.0f;
		up[1]=1.0f;
		up[2]=0.0f;
		game = new Game(getContext());
		game.initTitle();		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLObj.setViewport(0, 0, width, height);
		GLObj.Frustumf(mProjectionMatrix, 1.0f, 1.0f, 10f);
	}

	
	@Override
	public void onDrawFrame(GL10 gl) {
		//清理颜色和深度
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		//设置视图矩阵
	    GLObj.setLookat(mViewMatrix, eye, center, up);
	    //获得MVP矩阵
	    GLObj.getMVPmatrix(mMVPMatrix);
	    game.drawFrame();
	    
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x[] = new float[10];
		float y[] = new float[10];
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 sx[0]	=	event.getX(0);
			 sy[0]	=	event.getY(0);
			
			float[] r1 = GLObj.get(sx[0],sy[0],0.5f);
			game.bf.setPosition(r1[0], r1[1],r1[2]);
			
			r1 = GLObj.get(sx[0], sy[0], 0.0f);
			game.text.hantei(sx[0], sy[0]);
			Log.d("tt", ""+r1[0]+r1[1]+r1[2]);
			break;
			
		case MotionEvent.ACTION_UP:
			x[0]	= event.getX(0);
			y[0]	= event.getY(0);
			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			
			 MotionMode=1;
			 sx[0]	=	event.getX(0);
			 sy[0]	=	event.getY(0);
			 
			break;
		case MotionEvent.ACTION_POINTER_UP:
			MotionMode=0;
			break;
		default:
			return super.onTouchEvent(event);
		}
		return true;
	}
}
