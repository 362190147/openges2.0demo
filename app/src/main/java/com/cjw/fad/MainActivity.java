package com.cjw.fad;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	/**游戏表面*/
	MySurface mySurface	=	null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置为全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//
		setContentView(R.layout.activity_main);
		
		mySurface =(MySurface)findViewById(R.id.mySurface);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mySurface.gamePause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mySurface!=null)
			mySurface.Resume();
	}
	
	@Override
	protected void onDestroy() {
		mySurface.Stop();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}
