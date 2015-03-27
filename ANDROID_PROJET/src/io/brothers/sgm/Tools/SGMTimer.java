package io.brothers.sgm.Tools;

import android.os.AsyncTask;
import android.util.Log;

public class SGMTimer extends AsyncTask<Object, Float, Void>{
	private float duration = 0.0f;
	private float timer = 0.0f;
	private long previousTime = 0;
	private boolean stop = false;
	private boolean repeat = false;
	private Runnable task = null;
	
	@Override
	protected Void doInBackground(Object... params) {
		duration = (Float) params[0];
		repeat = (Boolean) params[1];
		task = (Runnable) params[2];
		previousTime = System.currentTimeMillis();
		
		do{
			resetTimer();
			while(timer > 0.0f && !stop)
			{
				long actualTime = System.currentTimeMillis();
				long timeFrame = actualTime - previousTime;
				previousTime = actualTime;
				removeTime((timeFrame / 1000f) % 60f);
				onProgressUpdate();
			}
			if(!stop){
				task.run();
			}
		}while(repeat && !stop);
		
		return null;
	}
		
	@Override
	protected void onProgressUpdate(Float... values) {
		super.onProgressUpdate(values);
	}

	public float getTimer() {
		return timer;
	}
	
	public void setTimer(float a_timer) {
		timer = a_timer;
	}
	
	public void resetTimer(){
        setTimer(duration);
	}
	
	public void stop(){
		stop = true;
	}
	
	public void removeTime(float second){
		timer -= second;
	}
}
