package com.nixit.myearthobserver;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {
	
	ArrayList<String[]> mapCategories = new ArrayList<String[]>();
	String[] mapNames = {"Atmosphere","Energy","Land","Life","Ocean"};
	String[] atmosphere;
	String[] energy;
	String[] land;
	String[] life;
	String[] ocean;
	
	Context context;
	
	//Class constructor, Takes in list items and images
	public DrawerAdapter(Context context){
	
		this.context = context;
		
		mapCategories.add(atmosphere = context.getResources().getStringArray(R.array.atmosphere));
		mapCategories.add(energy = context.getResources().getStringArray(R.array.energy));
		mapCategories.add(land = context.getResources().getStringArray(R.array.land));
		mapCategories.add(life = context.getResources().getStringArray(R.array.life));
		mapCategories.add(ocean = context.getResources().getStringArray(R.array.ocean));
		
	}

// gives a count of how many items are in the list
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return  (atmosphere.length + 1) + 
				(energy.length + 1) + 
				(land.length + 1) + 
				(life.length + 1) + 
				(ocean.length + 1);
	}

	
	//Gets a item string
	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return index;
	}
	
	//
	@Override
	public long getItemId(int index) {
		return index;
	}

	
	private int getCategoryIndex(int index){
		
		int length = 0;
		int length2 = mapCategories.get(0).length+1;
		
		for(int i = 1; i <= mapCategories.size();i++){
			
			if(index > length && index < length2){
				return i-1;
			}
			
			Log.i("length",String.valueOf(length));
			Log.i("length2",String.valueOf(length2));
			
			length = length2;
			length2 += mapCategories.get(i).length+1;
		}
		
		Log.i("Gotcha", "Gotcha");
		
		return -1;
		
	}

	private int getHeader(int index){
		
		int length = 0;
		
		for(int i = 0; i < mapCategories.size();i++){
			
			if(index == length){
				return i;
			}
			
			length += mapCategories.get(i).length+1;
			
		}
			
		return -1;
		}
	
private boolean isHeader(int index){
	
	int length = 0;
	
	for(int i = 0; i < mapCategories.size();i++){
		
		if(index == length){
			return true;
		}
		
		length += mapCategories.get(i).length+1;
		
	}
		
	return false;
		
	}
	
	
	
	@Override
	public View getView(int index, View view, ViewGroup parent) {
	
			if(isHeader(index)){
				
				LayoutInflater inflater =
						LayoutInflater.from(parent.getContext());
			
				view = inflater.inflate(
						R.layout.drawer_header, parent, false);
				
				TextView headerTextView = (TextView)
						view.findViewById(R.id.drawer_head);
				
				Log.i("index", String.valueOf(index));
				headerTextView.setText(mapNames[getHeader(index)]);
				
			}else if(index == -1){
				
				return view;
				
			}else{
				
				LayoutInflater inflater =
						LayoutInflater.from(parent.getContext());
			
				view = inflater.inflate(
						R.layout.drawer_item, parent, false);
				
				TextView titleTextView = (TextView)
						view.findViewById(R.id.txtTitle);

				ImageView image = (ImageView)view.findViewById(R.id.imgImage);
				
				Log.i("index",String.valueOf(index));
				
				int categoryIndex = getCategoryIndex(index);
				
				int cindex = index-1;
				
				for(int i = 0; i < categoryIndex;i++){
					cindex -= mapCategories.get(i).length+1;
				}
				
				image.setImageDrawable(context.getResources().getDrawable(
						context.getResources().getIdentifier(
	    						normalizeImageName(mapCategories.get(categoryIndex)[cindex]), "drawable", 
	    						"com.nixit.myearthobserver")));
										
				
				titleTextView.setText(mapCategories.get(getCategoryIndex(index))[cindex]);
			
		
		}
		
		return view;
	}
	
	private String normalizeImageName(String Item){
		
		return Item.replaceAll(" ", "_").toLowerCase();
	}
}
