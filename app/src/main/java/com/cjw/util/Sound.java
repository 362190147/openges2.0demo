/**
 * 声音管理类
 * 
 */
package com.cjw.util;

import com.cjw.fad.R;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
	MediaPlayer bgm;
	public Sound(Context context) {
		bgm=MediaPlayer.create(context, R.raw.gokukoku);
	}

	public void bgmChange(){
		;
	}
	public void bgmPlay(){
		
		bgm.start();
	}
	
	public void bgmStop(){
		bgm.stop();
	}
	
	public void bgmPause(){
		bgm.pause();
	}
}
