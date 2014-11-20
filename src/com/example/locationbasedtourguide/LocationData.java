package com.example.locationbasedtourguide;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class LocationData {

	public final static String LOCAITON_CLASS = "Locations",
			TEXT_KEY = "text", IMAGE_KEY = "image" , AUDIO_KEY = "audio",
			RADIUS_KEY = "raidus", ORDERING_ID_KEY = "ordering_id", GPS_KEY = "gps_location",
			OWNING_TOUR = "owning_tour";
	
	public final double getLongitude(){
		return lon;
	}
	
	public final double getLatitude(){
		return lat;
	}
	
	public final double getRadius(){
		return radius;
	}
	
	public final int getChronologicalOrdering(){
		return ordering;
	}
	
	public final void setChonologicalOrdering(int i){
		this.ordering = i;
	}
	
	private double lon,lat,radius;
	private int ordering;
	private String text;
	private Bitmap image;
	//private SOUNDTHING sound
	
	public LocationData(double lon, double lat, double radius, int ordering){
		this.lon = lon;
		this.lat = lat;
		this.radius = radius;
		this.ordering = ordering;
	}
	
	public LocationData(ParseObject po){
		this.radius = po.getDouble(RADIUS_KEY);
		this.ordering = po.getInt(ORDERING_ID_KEY);
		
		ParseGeoPoint geoPoint = po.getParseGeoPoint(GPS_KEY);
		this.lon = geoPoint.getLongitude();
		this.lat = geoPoint.getLatitude();
		
		this.text = po.has(TEXT_KEY) ? po.getString(TEXT_KEY) : null;
		if(po.has(IMAGE_KEY)){
			byte[] byteImage = po.getBytes(IMAGE_KEY);
			this.image = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
		} else {
			this.image = null;
		}
	}
	
	public boolean hasText(){
		return this.text != null;
	}
	public boolean hasImage(){
		return this.image != null;
	}
	//public abstract boolean hasAudio();
	
	public String getText(){
		return this.text;
	}
	//public Byte[] getAudio();
	public byte[] getImage(){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		this.image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}
	
	public void setText(String text){
		this.text = text;
	}
	//public abstract void setAudio(Byte[] input);
	public void setImage(Byte[] input){
		//fill this in later
	}
}
