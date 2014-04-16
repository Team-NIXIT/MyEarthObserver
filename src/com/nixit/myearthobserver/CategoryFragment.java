package com.nixit.myearthobserver;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class CategoryFragment extends Fragment {

	private View mainView;
	private ListView appList;
	public static int index = 1;
	private AppListAdapter listAdapter;
	private int category = 0;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainView =  inflater.inflate(R.layout.fragment_main_dummy, container, false);
	
		appList = (ListView)mainView.findViewById(R.id.gridListItems);
		
		setupMenuList();
		
		return mainView;
		
}
	
	private int returnArray(){
		
		int position = this.getArguments().getInt("Category");
		int array = 0;
		
		category = position;
		
		switch(position){
		
		case 0:
			array = R.array.atmosphere;
		break;
		case 1:
			array = R.array.energy;
		break;
		case 2:
			array = R.array.land;
		break;
		case 3:
			array = R.array.life;
		break;
		case 4:
			array = R.array.ocean;
		break;
		default:
		
			break;
		}
		
		return array;
	}

	
	private String normalizeImageName(String Item){
				
		return Item.replaceAll(" ", "_").toLowerCase();
	}
	
	private void setupMenuList(){
		
    	ArrayList<String> Items = new ArrayList<String>();
    	ArrayList<Drawable> Images = new ArrayList<Drawable>();
    	
    	String[] strItems = getResources().getStringArray(returnArray());
    	
    	
    	for(int i = 0; i < strItems.length;i++){
    		
    		Items.add(strItems[i]);
    		
    		Log.i("Item", strItems[i]);
    		
    		Images.add(getResources().getDrawable(
    				getResources().getIdentifier(
    						normalizeImageName(strItems[i]), "drawable", 
    						"com.nixit.myearthobserver")));
    		
    		}
    	
    	
    	appList = (ListView)mainView.findViewById(R.id.gridListItems);
    	listAdapter = new AppListAdapter(Images,Items);
    	
    	appList.setAdapter(listAdapter);
    	
    	appList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				
				TextView txtTitle = (TextView)arg1.findViewById(R.id.txtTitle);
				
				String map = txtTitle.getText().toString();
				
				Intent startMap = new Intent("com.nixit.myearthobserver.MapActivity");
				startMap.putExtra("Map", map);
				startMap.putExtra("Category", category);
				startActivity(startMap);
			}
    		
    	});
    	
    }

	
}