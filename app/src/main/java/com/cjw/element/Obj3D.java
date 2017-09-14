package com.cjw.element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Vector;

import android.opengl.GLES20;
import android.util.Log;


public class Obj3D {
	
	public int[] texture;
	private FloatBuffer vertexBuffer;
	private FloatBuffer texCoordBuffer;
	private int mProgram;
	private float[] mMatrix = new float[16];
	
	public Obj3D(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			loadObj(br);
		} catch (FileNotFoundException e) {
			Log.e("ObjLoad", "文件读取错误"+file);
			e.printStackTrace();
		}
	}
	public Obj3D(InputStream is){
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		loadObj(br);
	}
	
	private void loadObj(BufferedReader br) {
		String str=null;
		float[] vertices	= null;
		float[] normals	= null;
		float[] texCoord	= null;
		
		Vector <Float> vVertex		=	new Vector<Float>();
		Vector <Float> vTexCoord	=	new Vector<Float>();
		Vector <Float> vNormal		=	new Vector<Float>();
		Vector <Short> fv			=	new Vector<Short>();
		Vector <Short> ft			=	new Vector<Short>();
		Vector <Short> fn			=	new Vector<Short>();
		 try {
			while((str	=	br.readLine())!=null){
				 if(str.startsWith("v ")){
					float[] v=getfloat(str.split("\\s+"));
					for(float t: v){
						vVertex.add(t);
					}
				 }else if(str.startsWith("vt ")){
					 float[] v=getfloat(str.split("\\s+"));
					  vTexCoord.add(v[0]);
					  vTexCoord.add(1-v[1]);
				 } else if(str.startsWith("vn ")){
					 float[] v=getfloat(str.split("\\s+"));
					 for(float t: v){
							vNormal.add(t);
						}
				 } else if(str.startsWith("f ")){
					 String[] strings = str.split("\\s+");
					 
					 for(int i=1;i<strings.length;i++){
						 short[] f=getFace(strings[i]);
						 fv.add(f[0]);
						 ft.add(f[1]);
						 fn.add(f[2]);
					 }
				 }
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		vertices 	= new float[fv.size()*3];
		normals 	= new float[fv.size()*3];
		texCoord 	= new float[fv.size()*2];
		
		for(int i=0;i<fv.size();i++){
			vertices[i*3]	= vVertex.get(fv.get(i)*3);
			vertices[i*3+1]	= vVertex.get(fv.get(i)*3+1);
			vertices[i*3+2]	= vVertex.get(fv.get(i)*3+2);
			
			normals[i*3]	= vNormal.get(fn.get(i)*3);
			normals[i*3+1]	= vNormal.get(fn.get(i)*3+1);
			normals[i*3+2]	= vNormal.get(fn.get(i)*3+2);
			
			texCoord[i*2]	= vTexCoord.get(ft.get(i)*2);
			texCoord[i*2+1]	= vTexCoord.get(ft.get(i)*2+1);
		}
		
	}
		 
	private float[] getfloat(String str[]){
		float v[] = new float[str.length-1];
		for(int i=1;i<str.length;i++){
			v[i-1]	= Float.parseFloat(str[i]);
		}
		return v;
	}
	
	private short[] getFace(String str){
		String[] strings=str.split("/");
		short[] f=new short[3];
		for(int i=0;i<strings.length;i++){
			f[i]	= (short) (Short.parseShort(strings[i])-1);
		}
		return f;
	}
	
	public void draw(float[] mMVPMatrix){
		
		GLES20.glUseProgram(mProgram);
		if(texture!=null){
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
			GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "texture"), texture[0]);
		}
		//提交顶点
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "Vertex");
	        GLES20.glEnableVertexAttribArray(mPositionHandle);
	        GLES20.glVertexAttribPointer(mPositionHandle, 3,
	        		GLES20.GL_FLOAT, false, 0, vertexBuffer);
		
		//纹理坐标
		int texCoord = GLES20.glGetAttribLocation(mProgram, "texCoordIn");
		GLES20.glEnableVertexAttribArray(texCoord);
		GLES20.glVertexAttribPointer(texCoord, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer);
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mProgram, "uMatrix"), 1, false, mMatrix, 0);
		//画图元
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
		GLES20.glDisableVertexAttribArray(mPositionHandle );
		GLES20.glDisableVertexAttribArray(texCoord);
	}
	
	

}
 