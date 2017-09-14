package com.cjw.util;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.view.Surface;

public class Recorder {

	MediaRecorder recorder;
	public void record(Surface surface){
		try {
			File videoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+ ".amr");
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setVideoSize(320, 240);
			recorder.setVideoFrameRate(5);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
			recorder.setOutputFile(videoFile.getAbsolutePath());
			recorder.setPreviewDisplay(surface);
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shop(){
		if(recorder!=null){
			recorder.stop();
		 	recorder.release();
		 	recorder=null;
		 }
	}
}
