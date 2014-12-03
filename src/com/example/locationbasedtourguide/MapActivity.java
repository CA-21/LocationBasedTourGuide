package com.example.locationbasedtourguide;

import com.example.locationbasedtourguide.ParseInterface.TourGetterAndCreator;
import com.example.locationbasedtourguide.ParseInterface.TourGetterAndCreator.TourWithSameNameExistsException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.SaveCallback;

import ParseObjects.LocationData;
import ParseObjects.Tour;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MapActivity extends Activity  {

	public final static String TAG = "MAP_ACTIVITY";
	public final static String ACTIVITY_TYPE= "activityType";
	public final static int ERROR = -1, EDIT_AND_CREATE = 0, TAKE = 1;
	private Button finishedButton;

	private GoogleMap mMap;
	private Tour currentTour;

	private boolean tourEditable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		setContentView(R.layout.test_map_layout);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		finishedButton = (Button) this.findViewById(R.id.mapActivityButton);

		if(getIntent().getIntExtra(ACTIVITY_TYPE, ERROR) == EDIT_AND_CREATE){
			tourEditable = true;
		} else {
			tourEditable = false;
		}

		//Allow for addition of location points if the tour is in edit mode
		if(this.tourEditable){
			mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

				@Override
				public void onMapLongClick(LatLng point) {
					markerPopupWindow(MapActivity.this,point);
				}
			}); 
		}
	}

	@Override
	public void onWindowFocusChanged (boolean hasFocus){
		super.onPostResume();
		Intent i = this.getIntent();
		switch(i.getIntExtra(ACTIVITY_TYPE, ERROR)){
		case TAKE:
			try {
				//No matter what, this activity will start with some tour,
				//the tour will be created by the time this activity is started
				currentTour = TourGetterAndCreator.getTour(i.getStringExtra("tour"), false);

				for(LocationData l : currentTour.getAllLocationData()){
					mMap.addMarker(new MarkerOptions()
					.position(new LatLng(l.getLatitude(), l.getLongitude()))
					.title(l.getName()));
				}
				finishedButton.setText("Start tour");
				finishedButton.setOnClickListener(new  OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(MapActivity.this, "Starting tour", Toast.LENGTH_LONG).show();
						TourGPSService.startGPSService(MapActivity.this);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						TourGPSService.service.addActiveTour(currentTour);
						finishedButton.setText("\tEnd running tour\t");
						//finish();
						return;
					}
				});

			} catch (TourWithSameNameExistsException e) {
				currentTour = null;
				e.printStackTrace();
			}
			break;
		case EDIT_AND_CREATE:
			if(currentTour == null && hasFocus)
				this.tourPopupWindow(this);

			finishedButton.setText("done editing");
			finishedButton.setOnClickListener(new  OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
					//saving occurs in the onPause method
				}
			});

			break;
		default:
			Toast.makeText(this, "SOMETHING WENT WRONG", Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	protected void onPause(){
		super.onPause();
		if(currentTour != null && tourEditable && currentTour.getNumberOfLocations() > 0){
			currentTour.asyncSave();
			currentTour.asyncSave(new SaveCallback() {

				@Override
				public void done(ParseException arg0) {
					String text = "Tour "+currentTour.getName()+" Saved";
					if(arg0 != null){
						text = "tour "+currentTour.getName()+" not saved";
					}
					//					Log.i(TAG,text);
					//					Toast.makeText(MapActivity.this, text, Toast.LENGTH_LONG).show();
				}
			});
		} else if (currentTour.getNumberOfLocations() == 0){
			currentTour.deleteInBackground();
		}
	}

	private TourPopupIntializer tourPopupWindow(Context context){
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tourPopupView = inflater.inflate(R.layout.create_tour_popup,null);
		int height =  ViewGroup.LayoutParams.WRAP_CONTENT;
		int width = ViewGroup.LayoutParams.MATCH_PARENT;
		PopupWindow tourCreationWindow = new PopupWindow(tourPopupView,
				width,height,
				true); //Focusable, if false window cannot be touched.
		tourCreationWindow.setWindowLayoutMode(width, height);

		Object j = (new TourPopupIntializer(tourPopupView, tourCreationWindow));
		tourCreationWindow.showAtLocation(findViewById(R.id.mapLayout), Gravity.CENTER , 0, 0);
		return null; 
	}


	private MarkerPopupInitializer markerPopupWindow(Context context, LatLng p)
	{   
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View markerWindowView = inflater.inflate(R.layout.set_location_data_popup,null);


		int height =  ViewGroup.LayoutParams.WRAP_CONTENT;
		int width = ViewGroup.LayoutParams.MATCH_PARENT;
		PopupWindow markerWindow = new PopupWindow(markerWindowView,
				width,height,
				true); //Focusable, if false window cannot be touched.


		markerWindow.setWindowLayoutMode(width, height);
		markerWindow.setBackgroundDrawable(new ColorDrawable());
		//Called these methods (commented out) attempting to fix the problem without success.
		//markerWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		//markerWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		//markerWindow.setOutsideTouchable(true);

		MarkerPopupInitializer views = new MarkerPopupInitializer(markerWindowView, markerWindow,p);

		markerWindow.showAtLocation(findViewById(R.id.mapLayout), Gravity.CENTER , 0, 0);

		return views;
	}

	private class TourPopupIntializer{
		private EditText tourName;
		private Button addImage, submit,cancel;
		private Bitmap image;
		private PopupWindow pu;

		public TourPopupIntializer(View v, final PopupWindow pu){
			this.pu = pu;
			tourName = (EditText) v.findViewById(R.id.create_tour_popup_tourName);
			addImage = (Button) v.findViewById(R.id.create_tour_popup_addImageButton);
			cancel = (Button) v.findViewById(R.id.create_tour_popup_cancelButton);
			submit = (Button) v.findViewById(R.id.create_tour_popup_submitButton);

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pu.dismiss();
					finish();
				}
			});

			submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					createTourAndSubmit();
				}
			});

			addImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//get the image here
					Toast.makeText(MapActivity.this, "Add image not yet implemented", Toast.LENGTH_LONG).show();
				}
			});
		}

		public void createTourAndSubmit(){
			String name = tourName.getText().toString();
			try {
				currentTour = TourGetterAndCreator.getTour(name, true);
				this.pu.dismiss();
				Toast.makeText(MapActivity.this, "Tour " + name + " created", Toast.LENGTH_LONG).show();
				return;
			} catch (TourWithSameNameExistsException e) {
				Toast.makeText(MapActivity.this, "Sorry,the tour with name " + name + " has alredy been taken", Toast.LENGTH_LONG).show();
				//just ensure this is null to avoid complications
				currentTour = null;
			}
		}
	}

	private class MarkerPopupInitializer{
		EditText locationName, locationText;
		Button cancelButton, submitButton, addImageButton;
		PopupWindow pu;
		LatLng pos;
		Bitmap image = null;

		public MarkerPopupInitializer(View v,final PopupWindow pu, LatLng pos){
			this.pu = pu;
			this.pos = pos;
			locationName = (EditText) v.findViewById(R.id.popup_locationDataName);
			locationText = (EditText) v.findViewById(R.id.popup_locationDataText);

			cancelButton = (Button) v.findViewById(R.id.cancelPopupButton);
			submitButton = (Button) v.findViewById(R.id.popup_addNewLocationButton);
			addImageButton = (Button) v.findViewById(R.id.popup_addImageButton);

			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pu.dismiss();
				}
			});

			submitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					addMarkerAndDismiss();
				}
			});
		}

		public void addMarkerAndDismiss(){
			String name = this.locationName.getText().toString(),
					text = this.locationText.getText().toString();

			mMap.addMarker(new MarkerOptions()
			.position(pos)
			.title(name)           
			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			if(image != null){

			} else {
				LocationData curLocation = new LocationData(name, pos.latitude, pos.longitude,text);
				currentTour.addLocation(curLocation);
			}
			pu.dismiss();
		}
	}
}
