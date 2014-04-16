package com.nixit.myearthobserver;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public final class AppListAdapter extends BaseAdapter {
	
	//This represents the list items in the list
	ArrayList<imageListItem> ListItems = new ArrayList<imageListItem>();
	
	//Class constructor, Takes in list items and images
	public AppListAdapter( ArrayList<Drawable>listImages,ArrayList<String> listItems){
		
		Drawable listImage;
		
		for(int i =0; i < listItems.size(); i++){
			
			if(i < listImages.size()){
			
				 listImage = listImages.get(i);
			
			}else{
				listImage = null;
			}
			
			ListItems.add(new imageListItem(listImage,listItems.get(i)));
			
		}
		
	}
	
	
	//adds an item
	public void addItem(Drawable Image , String Item){
		
		ListItems.add(new imageListItem(Image,Item));
		
	}
	
	//adds an item at an index
public void addItem(int index, Drawable Image , String Item){
		
		ListItems.add(index, new imageListItem(Image,Item));
		
	}
	

// removes an item
public void removeItem(imageListItem Item){
		
		ListItems.remove(Item);
		
	}

//removes the item at an index
public void removeItem(int index){
	
	ListItems.remove(index);
	
}
	
// gives a count of how many items are in the list
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ListItems.size();
	}

	
	//Gets a item string
	@Override
	public imageListItem getItem(int index) {
		// TODO Auto-generated method stub
		return ListItems.get(index);
	}
	
	//
	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (view == null) {
		
			LayoutInflater inflater =
					LayoutInflater.from(parent.getContext());
		
			view = inflater.inflate(
					R.layout.listitem, parent, false);
			
		}
		
		imageListItem item = ListItems.get(index);
		
		TextView titleTextView = (TextView)
				view.findViewById(R.id.txtTitle);
		

		ImageView image = (ImageView)view.findViewById(R.id.imgImage);
		
		image.setImageDrawable(item.getImage());
		
		titleTextView.setText(item.getTitle());
		
		TextView descriptionTextView = (TextView)
				view.findViewById(R.id.txtDescription);
		
		descriptionTextView.setText(item.getDescription(parent.getContext(),item.getTitle()));
		
		return view;
	}

	public class imageListItem{
		
		Drawable image;
		String title;
		
		public String getDescription(Context context, String item) {
				
			String result = "";
				
				if(item.equalsIgnoreCase("Rainfall")){
					
					result = context.getResources().getString(R.string.rainfall);
					
				}else if(item.equalsIgnoreCase("Solar Insolation")){
					
					result = context.getResources().getString(R.string.solar_insolation);
					
				}else if(item.equalsIgnoreCase("Active Fires")){
					
					result = context.getResources().getString(R.string.active_fires);
					
				}else if(item.equalsIgnoreCase("Carbon Monoxide")){
					
					result = context.getResources().getString(R.string.carbon_monoxide);
					
				}else if(item.equalsIgnoreCase("Chlorophyll Concentration")){
					
					result = context.getResources().getString(R.string.chlorophyll_concentration);
					
				}else if(item.equalsIgnoreCase("Land Surface Temperature")){
					
					result = context.getResources().getString(R.string.land_surface_temperature);
					
				}else if(item.equalsIgnoreCase("Night Lights")){
					
					result = context.getResources().getString(R.string.night_lights);
					
				}else if(item.equalsIgnoreCase("Ozone")){
					
					result = context.getResources().getString(R.string.ozone);
					
				}else if(item.equalsIgnoreCase("Population")){
					
					result = context.getResources().getString(R.string.population);
					
				}else if(item.equalsIgnoreCase("Sea Surface Temperature")){
					
					result = context.getResources().getString(R.string.sea_surface_temperature);
					
				}else if(item.equalsIgnoreCase("Snow Cover")){
					
					result = context.getResources().getString(R.string.snow_cover);
					
				}else if(item.equalsIgnoreCase("Topography")){
					
					result = context.getResources().getString(R.string.topography);
					
				}else if(item.equalsIgnoreCase("Vegetation Index")){
					
					result = context.getResources().getString(R.string.vegetation_index);
					
				}else if(item.equalsIgnoreCase("Water Vapour")){
					
					result = context.getResources().getString(R.string.water_vapour);
					
				}
				
				return result;
			}
		

		imageListItem(Drawable image, String title){
			this.image = image;
			this.title = title;
		}
		
		String getTitle(){
			
			return title;
		}
		
		Drawable getImage(){
			
			return image;
		}
		
		void setTitle(String newVal){
			
			title = newVal;
		}
		
		void setImage(Drawable newImg){
			
			image  = newImg;
		}
		
	}
	
	
}
