package com.nixit.myearthobserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;

public class MapActivity extends FragmentActivity {

private GoogleMap mMap;
private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(-35.244141, -21.357422), new LatLng(39.232253,67.939453));
private final int MAX_ZOOM = 18;
private final int MIN_ZOOM = 3;
private OverscrollHandler mOverscrollHandler = new OverscrollHandler();
private DrawerLayout mDrawerLayout;
private AlertDialog dateDialog;
private AlertDialog animationDialog;
private AlertDialog mappingDialog;
private  AlertDialog.Builder dateGetBuilder;
private  AlertDialog.Builder animationGetBuilder;
private  AlertDialog.Builder mappingGetBuilder;
private BitmapDrawable groundOverlayImage;
private ActionBarDrawerToggle mDrawerToggle;
private BitmapDrawable measureImage;
private boolean isMappingLine = false;
private boolean isMappingPoly = false;
private boolean isAnimating = false;
private ProgressDialog progressDialog;
private String year = "2011";
private String month = "april";
private String category = "active_fires";
private String constUrl = "http://nixit.wiredgist.com/satelliteimagery";
Projection projection;
public double latitude;
public double longitude;
private PolygonOptions polyOptions = new PolygonOptions().fillColor(Color.GRAY).geodesic(true);
private PolylineOptions lineOptions = new PolylineOptions().width(5).color(Color.GRAY).geodesic(true);
private Menu genMenu;
private GroundOverlay overlay;
private TextView txtCatDateStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		category = getIntent().getStringExtra("Map");
        if(category != null){
		getActionBar().setTitle(category);
        }
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        txtCatDateStatus = (TextView)findViewById(R.id.txtCatDate);
        
        dateGetBuilder = new AlertDialog.Builder(this);
        animationGetBuilder = new AlertDialog.Builder(this);
        mappingGetBuilder = new AlertDialog.Builder(this);
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Map Image...");
        progressDialog.setMessage("Please wait, loading map image data.");
        progressDialog.setCancelable(false);
        
        LayoutInflater inflater = (LayoutInflater)getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
        
        final View dateDialgView = inflater.inflate(R.layout.dialog, null);
        final View animateDialogView = inflater.inflate(R.layout.animation_dialog, null);
        final View mappingDialogView = inflater.inflate(R.layout.mapping_dialog, null);
        
        Button btnOk = (Button)dateDialgView.findViewById(R.id.btnanimOk);
        Button btnCancel = (Button)dateDialgView.findViewById(R.id.btnanimCancel);
        
        btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Spinner spnMonth = (Spinner)dateDialgView.findViewById(R.id.spnstrMonth);
				Spinner spnYear = (Spinner)dateDialgView.findViewById(R.id.spnstrYear);
				
				month = spnMonth.getSelectedItem().toString();
				year = spnYear.getSelectedItem().toString();
				
				getData();
				dateDialog.dismiss();
				
			}});
        
        btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				dateDialog.dismiss();
				
			}});
        
        btnOk = (Button)animateDialogView.findViewById(R.id.btnanimOk);
        btnCancel = (Button)animateDialogView.findViewById(R.id.btnanimCancel);
        
        
        btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Spinner spnStartMonth = (Spinner)animateDialogView.findViewById(R.id.spnbegMonth);
				Spinner spnStartYear = (Spinner)animateDialogView.findViewById(R.id.spnbegYear);
				
				Spinner spnEndMonth = (Spinner)animateDialogView.findViewById(R.id.spnstrMonth);
				Spinner spnEndYear = (Spinner)animateDialogView.findViewById(R.id.spnstrYear);
				
				String startMonth = spnStartMonth.getSelectedItem().toString();
				String startYear = spnStartYear.getSelectedItem().toString();
				String endMonth = spnEndMonth.getSelectedItem().toString();
				String endYear = spnEndYear.getSelectedItem().toString();
				
				AnimationLoop animeLoop = new AnimationLoop(startMonth,endMonth,startYear,endYear);
				
				animeLoop.start();
				
				animationDialog.dismiss();
				
			}});
        
        btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				animationDialog.dismiss();
				
			}});
        
        btnOk = (Button)mappingDialogView.findViewById(R.id.mpbtnOk);
        btnCancel = (Button)mappingDialogView.findViewById(R.id.mpbtnCancel);
        
        
        btnOk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				RadioButton radBtnPoly = (RadioButton)mappingDialogView.findViewById(R.id.radio0);
				
				if(radBtnPoly.isChecked()){
				toggleMenuMappingStatus(true);
				}else{
				toggleMenuMappingStatus(false);	
				}
				
				mappingDialog.dismiss();
				
			}});
        
        btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				mappingDialog.dismiss();
				
			}});
        
        dateGetBuilder.setView(dateDialgView);
        animationGetBuilder.setView(animateDialogView);
        mappingGetBuilder.setView(mappingDialogView);
        
        dateGetBuilder.setCancelable(false);
        animationGetBuilder.setCancelable(false);
        mappingGetBuilder.setCancelable(false);
        
        if(category != null){
        getData();
        }
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView mDrawerList = (ListView) findViewById(R.id.drwList);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_opened,  /* "open drawer" description for accessibility */
                R.string.drawer_closed  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            
            @Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
            	invalidateOptionsMenu();
				super.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				// TODO Auto-generated method stub
				invalidateOptionsMenu();
				super.onDrawerStateChanged(newState);
			}



			public void onDrawerOpened(View drawerView) {
                
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        ListAdapter listAdapter = new DrawerAdapter(this);
        
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(listAdapter);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        
        mDrawerList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				TextView txtTitle = (TextView)arg1.findViewById(R.id.txtTitle);
				
				if(txtTitle != null){
				category = txtTitle.getText().toString();
				getData();
				 mDrawerLayout.closeDrawer(mDrawerList);
				}
			}
        	
        });
        
		MySupportMapFragment mapView = (MySupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView);
		mapView.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		mMap = mapView.getMap();
		
		mMap.setMyLocationEnabled(true);
    	mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		
		mMap.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		mapView.setOnDragListener(new MapWrapperLayout.OnDragListener() {
			
			@Override
            public void onDrag(MotionEvent motionEvent) {
               
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                int x_co = Integer.parseInt(String.valueOf(Math.round(x)));
                int y_co = Integer.parseInt(String.valueOf(Math.round(y)));

                projection = mMap.getProjection();
                Point x_y_points = new Point(x_co, y_co);
                LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                if(isMappingPoly){
                polyOptions.add(new LatLng(latitude,longitude));
                mMap.addPolygon(polyOptions);
                }else if(isMappingLine){
                	lineOptions.add(new LatLng(latitude,longitude));
                    mMap.addPolyline(lineOptions);	
                }
                
                Log.i("ON_DRAG", "lat:" + latitude);
                Log.i("ON_DRAG", "long:" + longitude);
				}
                // Handle motion event:
            
        });
		
	}
	
	
	
	private void getData(){
		
		ImageLoader imgLoader = new ImageLoader();
		
		
		
		String imgUrl = constUrl + "/" + normalizeImageName(category) + "/" + year + "/"+ 
				month + "/" + normalizeImageName(category) + "_" + year +"_" + month + ".png";
		
		String imgMeasure = constUrl + "/"+ "satelliteimagery_icons"  + "/" + normalizeImageName(category) + 
						"/" + normalizeImageName(category) + ".png";
		
		Log.i("URL", imgUrl);
		
		String[] imgUrls = {imgUrl, imgMeasure};
		
		progressDialog.show();
		imgLoader.execute(imgUrls);

		
	}
	
	class AnimationLoop extends Thread{

		String startMonth = "";
		String endMonth = "";
		String startYear = "";
		String endYear = "";
		String curMonth = "";
		String curYear = "";
		
		AnimationLoop(String startMonth, String endMonth, String startYear, String endYear){
			
			this.startMonth = startMonth;
			this.endMonth = endMonth;
			this.startYear = startYear;
			this.endYear = endYear;
			
			curMonth = startMonth;
			curYear = startYear;
		
		}
		
		@Override
		public void run() {
			
			while(!(curMonth == endMonth && curYear == endYear)){
				
				ImageLoader imgLoader = new ImageLoader();
				
				String imgUrl = constUrl + "/" + normalizeImageName(category) + "/" + curYear + "/"+ 
						curMonth + "/" + normalizeImageName(category) + "_" + curYear +"_" + curMonth + ".png";
				
				String imgMeasure = constUrl + "/"+ "satelliteimagery_icons"  + "/" + normalizeImageName(category) + 
								"/" + normalizeImageName(category) + ".png";
				
				Log.i("URL", imgUrl);
				
				String[] imgUrls = {imgUrl, imgMeasure};
				
				imgLoader.execute(imgUrls);
				
				if(getNextMonth(curMonth) == "end"){
					curMonth = "january";
					
					if(getNextYear(curYear) == "end"){
						
						curMonth = endMonth;
						curYear = endYear;
						
					}else{
						
						curYear = getNextYear(curYear);
						
					};
				}else{
					
					curMonth = getNextMonth(curMonth);
					
				}
				
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			super.run();
		}
		
	}
	
	
	private String getNextMonth(String month){
		
		String[] months = {"january","february","march","april",
							"june","july","august","september",
							"october","november","december"};
		
		String result = "end";
		
		for(int i = 0; i < months.length;i++){
			
			if(month.equalsIgnoreCase(months[i])){
				
				if(i == months.length-1){
					
					result = "end";
					break;
				}else{
					result = months[i+1];
					break;
				}
				
				
			}
			
		}
		
		return result;
	}
	
	private String getNextYear(String year){
		
		String[] years = {"2011", "2012", "2013","2014"};
		
		String result = "end";
		
		for(int i = 0; i < years.length;i++){
			
			if(month.equalsIgnoreCase(years[i])){
				
				if(i == years.length){
					
					result = "end";
					break;
				}else{
					result = years[i+1];
					break;
				}
				
				
			}
			
		}
		
		return result;
	}
	
	private class ImageLoader extends AsyncTask<String, Object, BitmapDrawable[]>{

		@Override
		protected BitmapDrawable[] doInBackground(String... arg0) {
			
			 BitmapDrawable[] results = {setBitmap(arg0[0],MapActivity.this.getResources()),
					 					 setBitmap(arg0[1],MapActivity.this.getResources())};
			
			return results;
		}

		@Override
		protected void onPostExecute(BitmapDrawable[] result) {
			groundOverlayImage = result[0];
			measureImage = result[1];
			
			txtCatDateStatus.setText(month.toUpperCase(Locale.ENGLISH) + " " + year);
			
			progressDialog.dismiss();
			getActionBar().setTitle(category);
			
			setGroundOverlay(groundOverlayImage);
			setMeasureImage(measureImage);
			
			super.onPostExecute(result);
		}
		
	}
	
private void setMeasureImage(BitmapDrawable image){
		
		if(image != null){
			
			ImageView imgMeasure = (ImageView)findViewById(R.id.imgMeasure);
			imgMeasure.setImageDrawable(image);
		}else{
			
		}
		
	}
	
	private void setGroundOverlay(BitmapDrawable image){
		
		if(image != null){
			
			GroundOverlayOptions newarkMap = new GroundOverlayOptions()
			        .image(BitmapDescriptorFactory.fromBitmap(image.getBitmap()))
			        .position(new LatLng(0,0), (12770075.427617017f) *3.4f)
			        .transparency(0.0f);
			
			if(overlay != null){
				overlay.remove();
			}
			overlay = mMap.addGroundOverlay(newarkMap);
			
			
		}else{
			Toast.makeText(this, "Image not available", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	private String normalizeImageName(String Item){
		
		return Item.replaceAll(" ", "_").toLowerCase(Locale.ENGLISH);
	}
	
	@Override
	protected void onStart() {
		
	    CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(9.081999, 8.675277), 3.5f);
	    mMap.moveCamera(upd);
		mOverscrollHandler.sendEmptyMessageDelayed(0,100);
		super.onStart();
	}

	private static BitmapDrawable setBitmap(String url, Resources res) {
     	try {
     	HttpURLConnection connection =
     	(HttpURLConnection)new URL(url).openConnection();
     	connection.setDoInput(true);
     	connection.connect();
     	
     	InputStream input = connection.getInputStream();
     	
     	Bitmap bitmap = BitmapFactory.decodeStream(input);
     	
     	BitmapDrawable returnBitmap = new BitmapDrawable(res,bitmap);
     	input.close();
     	return returnBitmap;
     	} catch (IOException ioe) { return null; }
     	}
	
	private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
	    double latitude=0, longitude=0;
	    
	    if(cameraBounds.southwest.latitude < BOUNDS.southwest.latitude) {
	        latitude = BOUNDS.southwest.latitude - cameraBounds.southwest.latitude;
	    }
	    if(cameraBounds.southwest.longitude < BOUNDS.southwest.longitude) {
	        longitude = BOUNDS.southwest.longitude - cameraBounds.southwest.longitude;
	    }
	    if(cameraBounds.northeast.latitude > BOUNDS.northeast.latitude) {
	        latitude = BOUNDS.northeast.latitude - cameraBounds.northeast.latitude;
	    }
	    if(cameraBounds.northeast.longitude > BOUNDS.northeast.longitude) {
	        longitude = BOUNDS.northeast.longitude - cameraBounds.northeast.longitude;
	    }
	    return new LatLng(latitude, longitude);
	}
	
	private class OverscrollHandler extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	        CameraPosition position = mMap.getCameraPosition();
	        VisibleRegion region = mMap.getProjection().getVisibleRegion();
	        float zoom = 0;
	        if(position.zoom < MIN_ZOOM) zoom = MIN_ZOOM;
	        if(position.zoom > MAX_ZOOM) zoom = MAX_ZOOM;
	        LatLng correction = getLatLngCorrection(region.latLngBounds);
	        if(zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
	        	zoom = (zoom==0)?position.zoom:zoom;
	            double lat = position.target.latitude + correction.latitude;
	            double lon = position.target.longitude + correction.longitude;
	            CameraPosition newPosition = new CameraPosition(new LatLng(lat,lon), zoom, position.tilt, position.bearing);
	            CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
	            mMap.moveCamera(update);
	        }
	        /* Recursively call handler every 100ms */
	        sendEmptyMessageDelayed(0,100);
	    }
	}
	
	
	private void toggleMapStates(){
		MenuItem mapLockItem = genMenu.findItem(R.id.mapLoc);
		
		if(mMap.getUiSettings().isScrollGesturesEnabled()){
			mapLockItem.setIcon(R.drawable.ic_maplocation_no);
		}else{
			mapLockItem.setIcon(R.drawable.ic_maplocation);
		}
		
		mMap.getUiSettings().setAllGesturesEnabled(!mMap.getUiSettings().isScrollGesturesEnabled());
	}
	
	private void toggleMenuMappingStatus(boolean isPoly){
		
		if(isPoly){
		isMappingPoly = !isMappingPoly;
		}else{
		isMappingLine = !isMappingLine;
		}
		
		toggleMapStates();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_maps, menu);
		
		genMenu = menu;
		
		MenuItem dateItem = menu.findItem(R.id.getDate);
		
		dateItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			 @Override
				public boolean onMenuItemClick(MenuItem arg0) {
			
				 if(dateDialog == null){
				 dateDialog = dateGetBuilder.show();
				 }else{
					 dateDialog.show();
				 }
				 
			return true;
		}
	}
	);
	
		final MenuItem mapLockItem = menu.findItem(R.id.mapLoc);
		
		mapLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				if(!isMappingPoly && !isMappingLine){
				if(mappingDialog == null){
					 mappingDialog = mappingGetBuilder.show();
					 }else{ 
						 mappingDialog.show();
					 }
				}else{
					isMappingPoly = false;
					isMappingLine = false;
					toggleMapStates();
				}
				return true;
			}
			
		});
	
		MenuItem clrItem = menu.findItem(R.id.clearMap);
		
		clrItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			 @Override
				public boolean onMenuItemClick(MenuItem arg0) {
				 
				 polyOptions = new PolygonOptions().fillColor(Color.GRAY).geodesic(true);
				 lineOptions = new PolylineOptions().width(5).color(Color.GRAY).geodesic(true);
				 
				 mMap.clear();
				 
			return true;
		}
	}
	);
		
		MenuItem strtAnimItem = menu.findItem(R.id.start_anim);
		
		strtAnimItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			 @Override
				public boolean onMenuItemClick(MenuItem arg0) {
				 
				 if(!isAnimating){
					 if(animationDialog ==null){
				 animationDialog = animationGetBuilder.show();
					 }else{
						 animationDialog.show(); 
					 }
				 }else{
					 
				 }
				 
			return true;
		}
	}
	);
		
	MenuItem helpItem = menu.findItem(R.id.help);
		
	helpItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			 @Override
				public boolean onMenuItemClick(MenuItem arg0) {
			
				 Intent startHelp = new Intent("com.nixit.myearthobserver.HelpActivity");
				 startActivity(startHelp);
				 
			return true;
		}
	}
	);
		
		return true;
	}
	
}

