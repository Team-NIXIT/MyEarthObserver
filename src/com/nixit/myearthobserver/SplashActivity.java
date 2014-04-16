package com.nixit.myearthobserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		SplashTask currentSplash = new SplashTask();
		currentSplash.start();
	}



	private class SplashTask extends Thread {

		public void run(){
			
			try{
				sleep(3000);
			}catch(Exception e){
				e.printStackTrace();
				
			}finally{
				
				Intent startMenu = new Intent("com.nixit.myearthobserver.MenuActivity");
		    	startActivity(startMenu);
		    	finish();
			}
		
	}
		
	}

	
}