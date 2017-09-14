package com.cjw.game;

import android.content.Context;
import android.graphics.BitmapFactory;


import com.cjw.element.BackGround;
import com.cjw.element.Butterflies;
import com.cjw.element.TextGL;
import com.cjw.fad.R;
import com.cjw.util.Sound;

public class Game {
	
	enum GameState{
		game_InitTitle,
		game_Title,
		game_load,
		game_Start,
		game_Run,
		game_Pause,
		game_End}
	
	GameState gameState		= GameState.game_Title;
	public Butterflies bf	= null;
	public TextGL	text	= null;
	public BackGround 	bg 	= null;
	private Context context;
	private Sound sound;
	public Game( Context context) {
		this.context=context;
	}
	
	public void drawFrame(){
    switch (gameState) {
    	case game_InitTitle:
    		initTitle();
    		break;
		case game_Title:
			Title();	
			break;
		case game_load:
			;
		case game_Run:
			Run();
			break;
		case game_Pause:
			gamePause();
			break;
		case game_End:
			gameEnd();
			break;
		default:
			break;
		}
	}

	
	public void initTitle(){
		bf=new Butterflies(10);
		bf.loadTexture(0,BitmapFactory.decodeResource(context.getResources(), R.raw.fly));
		bf.add(-5, -5, 0);
		bf.add(5, 5, 0);
		bf.add(-5, 5, 0);
		bf.add(5, -5, 0);
		
		text=new TextGL();
		//text.add("游戏开始", 0.0f, 2.0f, 0.0f);
		//text.add("游戏继续", 0.0f, 0.0f, 0.0f);
		//text.add("游戏结束", 0.0f, -2.0f, 0.0f);
		bg = new BackGround();
		bg.loadTexture(0,BitmapFactory.decodeResource(context.getResources(), R.raw.bg2));
		sound=new Sound(context);
		sound.bgmPlay();
	}
	
	public void Run(){
	}
	
	public void Title(){
		bg.draw();
		//text.draw();
		bf.draw();
	}
	
	public void gamePause(){
		sound.bgmPause();
	}
	
	public void gameEnd() {
		
		//context=null;
		sound.bgmStop();
		//sound=null;
	}

	public void Resume() {
		// TODO Auto-generated method stub
		if(sound!=null);
			sound.bgmPlay();
	}
}
