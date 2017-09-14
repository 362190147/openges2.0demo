package com.cjw.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;


public class FileTool {

	public static String getStoragePath(){
		
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)){
			
			return Environment.getExternalStorageDirectory().getPath();

		} else {
			Log.e("SD", "找不到SD卡");
			return null;
		}
		
	}
	
	public static String[] getFileList(File file){
		String[] fileList	= null;
		
		if(file.exists())
			return null;
		if(file.isDirectory()){
			fileList=file.list();
		}
		return fileList;
	}
	
	public static String[] getFileList(String path){
		File file	= new File(path);
		return getFileList(file);
	}
	
	public static void savePng(Bitmap bitmap,String filePath){
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(filePath);
			
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.flush();
			fos.close();
			Log.e("savePng", filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void saveJpg(Bitmap bitmap,String filePath){
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(filePath);
			
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			Log.d("savePng", filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
