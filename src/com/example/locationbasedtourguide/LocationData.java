package com.example.locationbasedtourguide;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Locations")
public class LocationData extends ParseObject{

	public final static String LOCAITON_CLASS = "Locations",
			TEXT_KEY = "text", IMAGE_KEY = "image" , AUDIO_KEY = "audio",
			RADIUS_KEY = "raidus", ORDERING_ID_KEY = "ordering_id", GPS_KEY = "gps_location",
			OWNING_TOUR_KEY = "owning_tour", NAME_KEY="name", NO_NAME_VALUE = "no name provided";
	
	//Arbitrary
	private final static double DEFAULT_RADIUS = 10;

	public LocationData(){
		this.setName(NO_NAME_VALUE);
	}
	
	public LocationData(String name){
		this.setName(name);
	}
	
	public LocationData(String name, double lat, double lon){
		this(name);
		this.setLocation(lat, lon);
		this.setRadius(DEFAULT_RADIUS);
	}
	
	public LocationData(String name, double lat, double lon, double radius){
		this(name,lat,lon);
		this.setRadius(radius);
	}
	
	public LocationData(String name, double lat, double lon, double radius, String text){
		this(name,lat,lon,radius);
		this.setText(text);
	}
	
	public LocationData(String name, double lat, double lon, String text){
		this(name,lat,lon,DEFAULT_RADIUS,text);
	}
	
	public final double getLongitude(){
		return this.getParseGeoPoint(GPS_KEY).getLongitude();
	}

	public final double getLatitude(){
		return this.getParseGeoPoint(GPS_KEY).getLatitude();
	}

	public final double getRadius(){
		return this.getDouble(RADIUS_KEY);
	}

	private Bitmap image;
	//private SOUNDTHING sound

	public boolean hasText(){
		return this.has(TEXT_KEY);
	}
	public boolean hasImage(){
		return this.has(IMAGE_KEY);
	}
	//public abstract boolean hasAudio();

	public String getText(){
		return this.getString(TEXT_KEY);
	}
	
	public String getName(){
		return this.getString(NAME_KEY);
	}

	//public Byte[] getAudio();
	public Bitmap getImage(){
		if(image == null && this.hasImage())
			image = BitmapFactory.decodeByteArray(this.getBytes(IMAGE_KEY),0, 0);

		return image;
	}

	public void setText(String text){
		this.put(TEXT_KEY, text);
	}

	//public abstract void setAudio(Byte[] input);

	public void setImage(Bitmap input){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		input.compress(CompressFormat.PNG, 100, stream);
		byte[] toRet = stream.toByteArray();
		this.put(IMAGE_KEY, toRet);
	}

	public void setLocation(double lat, double lon){
		ParseGeoPoint geoPoint = new ParseGeoPoint(lat, lon);
		this.put("GPS",geoPoint);
	}
	
	public void setName(String name){
		this.put(NAME_KEY, name);
	}

	public void setRadius(double i ){
		this.put(RADIUS_KEY,i);
	}

	public static ParseQuery<LocationData> getQuery(){
		return ParseQuery.getQuery(LocationData.class);
	}
}
