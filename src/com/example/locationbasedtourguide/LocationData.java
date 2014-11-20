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
			OWNING_TOUR_KEY = "owning_tour";

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

	/*
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
	}*/

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

	public void setRadius(double i ){
		this.put(RADIUS_KEY,i);
	}

	public static ParseQuery<LocationData> getQuery(){
		return ParseQuery.getQuery(LocationData.class);
	}
}
