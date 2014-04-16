package com.nixit.myearthobserver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class HelpFragment extends Fragment {

	private View mainView;
	private ImageView helpImage;
	public static int index = 1;
	private int category = 0;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainView =  inflater.inflate(R.layout.fragment_about, container, false);
	
		helpImage = (ImageView)mainView.findViewById(R.id.imgHelp);
		
		int page = getArguments().getInt("item");
		
		helpImage.setImageDrawable(getResources().getDrawable(
				getResources().getIdentifier(
						"meo" + String.valueOf(page + 1), "drawable", 
						"com.nixit.myearthobserver")));

		return mainView;
		
}
	
}
