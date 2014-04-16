package com.nixit.myearthobserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MenuActivity extends Activity {
	
	ImageButton imgButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_menu);
		setButtonListeners();
	}
	
	private void setButtonListeners(){
		
		int[] btnIds = {R.id.btnAtmosphere,R.id.btnEnergy,R.id.btnLand,R.id.btnLife,R.id.btnOcean}; 
		
		ImageButton button;
		
		for(int i = 0; i < btnIds.length;i++){
			
			button = (ImageButton) findViewById(btnIds[i]);
			setButtonListener(button);
		}
		
	}
	
	private void setButtonListener(View imgButton){
		
		imgButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View button) {
				// TODO Auto-generated method stub
				Intent startMain = new Intent("com.nixit.myearthobserver.MainActivity");
				startMain.putExtra("Action", button.getId());
		    	startActivity(startMain);
			}
			}
		);
	}
	
}
