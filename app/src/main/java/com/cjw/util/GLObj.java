package com.cjw.util;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;



public class GLObj {
	
	public float x=0;
	public float y=0;
	public float z=0;
	
	public static GL10 gl=null;
	/**摄像头参数 眼睛位置*/
	public static float[] mEye		= null;
	/**摄像头参数 目标位置*/
	public static float[] mCenter	= null;
	/**摄像头参数  上方向*/
	public static float[] mUp		= null;
	
	/** 屏幕高度*/	
	public static int width  		= 1;
	/** 屏幕宽度*/	
	public static int height 		= 1;
	/**视图*/
	public static int[] viewport	= new int[4];
	
	/**最近距离中高度坐标的最大值*/
	public static float size = 1.0f;
	/**最近处*/
	public static float near = 3.0f;
	/**最远处*/
	public static float far	 = 7.0f;
	
	/**投影矩阵*/
	public static float[] mProjectionMatrix;
	/**视图矩阵*/
	public static float[] mViewMatrix;
	/**投影视图矩阵*/
	public static float[] mMVPMatrix;

	/**纹理*/
	public Texture[] 	texture			= null;
	/**顶点数据*/
	public FloatBuffer vertexBuffer		= null;
	/**纹理坐标*/
	public FloatBuffer texCoordBuffer	= null;
	/**顶点法线*/
	public FloatBuffer normalBuffer		= null;
	/**顶点颜色*/
	public FloatBuffer colorBuffer		= null;
	
	public float[]		glColor			= null; 
	
	public float[] mMatrix = new float[16];
	
	//着色器相关
	/**着色器程序*/
	public int mProgram	= 0;
	/**顶点引用*/
	public int hVertex 	= 0;
	/**纹理引用*/
	public int hTexCoord= 0;
	/**法线引用*/
	public int hNormal	= 0;
	/**颜色引用*/
	public int hColor	= 0;
	/**矩阵引用*/
	public int hMatrix	= 0;
	
	/** 使能旗帜，0为顶点 1为纹理坐标 2为法线  3顶点颜色颜色  4画笔颜色*/
	public int[] Enableflag = new int[5];
	
	public int vertexSize		= 3;
	public int vertexStride		= 0;
	public int colorSize		= 4;
	public int colorStride 		= 0;
	public int texCoordStride	= 0;
	public int normalStride		= 0;
	/**顶点数*/
	public int count			= 0;
	
	/**默认顶点数组*/
	public static final float [] ver={
			-0.5f, -0.5f, 0.0f, 
			 0.5f, -0.5f, 0.0f,
			-0.5f, 	0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f};
	
	/**默认纹理顶点数组*/
	public static final float [] tex={
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f, 
			1.0f, 0.0f};
	
	/**默认纹理颜色*/
	public static final float [] colors={
			 0.0f,  0.0f, 1.0f,
			 0.0f, 	1.0f, 0.0f,
			 1.0f, 	0.0f, 0.0f,
			 0.5f,  0.5f, 0.5f};
	
	/**默认顶点着色器代码*/
	public final static String vertexCode =
		 	"attribute vec4 aVertex;\n" +
    	    "attribute vec2 aTexCoord;\n" +
    	    "attribute vec3 aNormal;\n" +
    	    "attribute vec4 aColor;" +
    	    "uniform mat4 uMatrix;" +
    	    "varying vec2 vTexCoord;" +
    	    "varying vec4 vColor;" +
    	    "void main(){" +
    	    "	gl_Position = uMatrix*aVertex;" +
    	    "	vTexCoord = aTexCoord;" +
    	    "	vColor = aColor;" +
    	    "}";

	/**默认片段着色器代码*/
	public final static String fragCode =
	    "precision mediump float;" +
	    "uniform int flag[5];" +
	    "uniform sampler2D Texture;" +
	    "uniform vec4 uColor;" +
	    //"uniform sampler2D Texture2;" +
	    "varying vec2 vTexCoord;" +
	    "varying vec4 vColor;" +
	    "void main() {" +
	    "	vec4 color =vec4(1.0);" +
	    "	if(1==flag[1])" +
	    "	color= texture2D(Texture, vTexCoord);" +
	    "	if(1==flag[3])color=color+vColor;" +
	    "	if(flag[4]==1)" +
	    "		color=color*0.5+uColor*0.5;" +
	    "	gl_FragColor = color;" +
	    " " +
				"}";
	
	public static int defaultShader = initShader(vertexCode, fragCode);
   

	
	public static void setViewport(int x, int y,int width, int height){
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = width;
		viewport[3] = height;
		GLObj.width		= width;
		GLObj.height	= height;
		GLES20.glViewport(x, y, width, height);
	}
	
	public static float[] get(float screenX, float screenY, float depth){
		float[] r1=new float[4];
		GLU.gluUnProject(screenX,  viewport[3]-screenY, depth, mViewMatrix, 0,
				mProjectionMatrix, 0, viewport, 0, r1, 0);

		for(int i=0;i<3;i++){
			r1[i]/=r1[3];
		}
		return r1;
	}
	
	/**
	 * 设置投影矩阵
	 * @param ProjectionMatrix 投影矩阵主 必须先分配空间
	 * @param sizeH 最近距离中高度坐标的最大值
	 * @param Near 能看到的最近距离
	 * @param Far  能看到的最远距离
	 */
	public static void Frustumf(float[] ProjectionMatrix, float sizeH, float Near, float Far){
		size		= sizeH;
		GLObj.near	= Near;
		GLObj.far	= Far;
		float ratio = (float) width / (float) height;
		mProjectionMatrix=ProjectionMatrix;
		Matrix.frustumM(ProjectionMatrix, 0, -ratio*size, ratio*size, -size, size,  near, far);
	}
	
	/**
	 * 设置摄像头属性
	 * @param ViewMatrix 视图矩阵
	 * @param eye	    摄像头位置 eye[0] 为x坐标, eye[1] 为y坐标, eye[2] 为z坐标,
	 * @param center 摄像头目标 center[0]为x坐标，摄像头目标 center[1]为y坐标，摄像头目标 center[2]为z坐标，
	 * @param up	   向上的方向
	 */
	public static void setLookat(float[] ViewMatrix,  float[] eye, float[] center, float[] up){
		mViewMatrix = ViewMatrix;
		mEye=eye;
		mCenter=center;
		mUp=up;
		Matrix.setLookAtM(mViewMatrix, 0, eye[0], eye[1], eye[2], center[0],
				center[1], center[2], up[0], up[1], up[2]);
	}
	
	/**
	 * 获得 MVP矩阵
	 * @param MVPMatrix MVP矩阵
	 */
	public static void getMVPmatrix(float[] MVPMatrix){
		 // Calculate the projection and view transformation
		mMVPMatrix= MVPMatrix;
	    Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	public GLObj(){
		setVertices(ver, 3, 0);
		setTexCoord(tex, 0);
		
		//setVertexColor(colors, 3, 0);
		mProgram=defaultShader;
		getHandle(mProgram);
	}
	
	
	public void loadTexture(int index, Bitmap bitmap){
		if(texture==null) 
			texture	=	new Texture[index+1];
		if(texture.length<index+1){
			Texture[] t	=	texture;
			texture = new Texture[index+1];
			for(int i=0;i<t.length;i++){
				texture[i]=t[i];
			}
			
		}
		
		texture[index]	=	new Texture(bitmap);
	}
	/**
	 * 设置顶点 
	 * @param data 顶点数据
	 * @param Size 
	 * @param stride 
	 */
	public void setVertices(float[] data, int Size, int stride){
		vertexSize	= Size;
		vertexStride= stride;
		if(data == null)
			data=ver;
		vertexBuffer = GLESU.toFloatBuffer(data);
		if(count<=0) count = data.length/Size;
	}
	
	/**
	 * 设置顶点颜色
	 * @param data
	 * @param Size
	 * @param stride
	 */
	public void setVertexColor(float[] data, int Size, int stride){
		colorSize	= Size;
		colorStride = stride;
		if(data == null)
			data=ver;
		colorBuffer = GLESU.toFloatBuffer(data);
	}
	
	/**
	 * 设置纹理坐标
	 * @param data 纹理坐标数据
	 * @param stride
	 */
	public void setTexCoord(float[] data, int stride){
		texCoordStride= stride;
		if(data == null)
			data = tex;
		texCoordBuffer = GLESU.toFloatBuffer(data);
	}
	
	/**
	 * 设置法线
	 * @param data
	 * @param stride
	 */
	public void setNoraml(float[] data, int stride){
		normalStride = stride;
		if(data == null)
			data = ver;
		normalBuffer = GLESU.toFloatBuffer(data);
	}
	
	/**
	 * 初始化着色器
	 * @param vertexCode 顶点着色器代码
	 * @param fragCode   片段着色器代码
	 */
	public static int initShader(String  vertexCode,String fragCode){
		int verShader	= GLESU.loadShader(GLES20.GL_VERTEX_SHADER, vertexCode);
		int fragShader	= GLESU.loadShader(GLES20.GL_FRAGMENT_SHADER, fragCode);
		int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, verShader);
		GLES20.glAttachShader(program, fragShader);
		GLES20.glLinkProgram(program);
		return program;
		
	}
	
	/**
	 * 获得顶点引用
	 */
	public void getHandle(int program){	
		hVertex		= GLES20.glGetAttribLocation(program, "aVertex");
		hTexCoord	= GLES20.glGetAttribLocation(program, "aTexCoord");
		hNormal		= GLES20.glGetAttribLocation(program, "aNormal");
		hColor		= GLES20.glGetAttribLocation(program, "aColor");	
	}

	public interface Render{
		public abstract void draw();
	}
	
	/**
	 * 提交顶点属性
	 */
	public void EnableVertex() {
		// 传入顶点坐标
		if (vertexBuffer != null) {
			GLES20.glEnableVertexAttribArray(hVertex);
			GLES20.glVertexAttribPointer( hVertex, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
			Enableflag[0] = 1;
		}
		
		// 传入纹理坐标
		if (texCoordBuffer != null) {
			GLES20.glEnableVertexAttribArray(hTexCoord);
			GLES20.glVertexAttribPointer(hTexCoord, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer);
			Enableflag[1] = 1;
		}
		
		// 传入法线
		if (normalBuffer != null) {
			GLES20.glEnableVertexAttribArray(hNormal);
			GLES20.glVertexAttribPointer(hNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
			Enableflag[2] = 1;
		}

		// 传入顶点颜色
		if (colorBuffer != null) {
			GLES20.glEnableVertexAttribArray(hColor);
			GLES20.glVertexAttribPointer(hColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
			Enableflag[3] = 1;
		}
		if(glColor!=null){
			GLES20.glUniform4fv(GLES20.glGetUniformLocation(mProgram, "uColor"), 1, glColor, 0);
			Enableflag[4] = 1;
		}
		GLES20.glUniform1iv(GLES20.glGetUniformLocation(mProgram, "flag"), 5, Enableflag, 0);
		
	}

	/**
 	 * 禁用所有顶点属性
	 */
	public void DisableVertex(){
		GLES20.glDisableVertexAttribArray(hVertex);
		GLES20.glDisableVertexAttribArray(hTexCoord);
		GLES20.glDisableVertexAttribArray(hColor);
		GLES20.glDisableVertexAttribArray(hNormal);
		glColor=null;
		for(int i=0;i<Enableflag.length;i++)
			Enableflag[i]=0;
	}
	
	public void draw(){
		GLES20.glUseProgram(mProgram);
		EnableVertex();
		
		//矩阵转换
		Matrix.scaleM(mMatrix, 0, mMVPMatrix, 0, 
		size*2*width/height, size*2, 1.0f);
		//Matrix.translateM(mMatrix, 0, 0.0f, 0.0f, 2.0f);
		
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
	
	interface TouchListen{
		public boolean onTouch(float x, float y);
	}
	
	public boolean isTouched(float x, float y){
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
			return true;
		}else
			return false;
		
	}
	
}
