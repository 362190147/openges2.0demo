package com.cjw.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class GLESU {
	
	
	/**
	 * 读取并编译着色器
	 * @param type 着色器类型 GLES20.GL_VERTEX_SHADER或GLES20.GL_FRAGMENT_SHADER)
	 * @param shaderCode 着色器代码
	 * @return 着色器句柄
	 */
	public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        
        int [] compile = new int[1];
        GLES20.glGetShaderiv (shader, GLES20.GL_COMPILE_STATUS, compile, 0);
        //如果编译失败打印错误信息
        if(GLES20.GL_FALSE==compile[0]){
        	if(type==GLES20.GL_VERTEX_SHADER)
        		Log.e("VERTEX_SHADER", GLES20.glGetShaderInfoLog(shader));
        	if(type==GLES20.GL_FRAGMENT_SHADER)
        		Log.e("FRAGMENT_SHADER", GLES20.glGetShaderInfoLog(shader));	
        }

        return shader;
    }

	
	 
	/**
	 * 将float数组转换成 FloatBuffer
	 * @param data float数组
	 * @return FloatBuffer
	 * */
	public static FloatBuffer toFloatBuffer(float[] data){
			if(data == null) 
				return null;
			FloatBuffer dataBuffer = ByteBuffer.allocateDirect(data.length
		                * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
			dataBuffer.put(data);
			dataBuffer.position(0);
			return dataBuffer;
	}
	 
	/**
	 * 通过三个顶点获得法线
	 * @param t1 顶点1
	 * @param t2 顶点2
	 * @param t3 顶点3
	 * @return 归一化后的法线
	 */
	public static float[] getNormal(float[] t1, float[] t2, float[] t3){
		float[] normal = new float[3]; 
		float[] temp1 = new float[3]; 
		float[] temp2 = new float[3];
		for(int i=0;i<3;i++){
			temp1[i]=t2[i]-t1[i];
			temp2[i]=t3[i]-t1[i];
		}
		
		normal[0]=temp1[1]*temp2[2]-temp1[2]*temp2[1] ;
		normal[1]=temp1[2]*temp2[0]-temp1[0]*temp2[2] ;
		normal[2]=temp1[0]*temp2[1]-temp1[1]*temp2[0] ;
		
		normalize(normal);
		return normal;
	}
	
	public static float[] normalize(float[] n){
		float t=0;
		t=(float) Math.sqrt(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);
		n[0]/=t;
		n[1]/=t;
		n[2]/=t;
		return n;
	}
	
	public static float dot(float []v1,float v2[]){
		float result=0;
		for(int i=0;i<v1.length;i++)
			result+=v1[i]*v2[i];
		return result;
	}
	
	
}
