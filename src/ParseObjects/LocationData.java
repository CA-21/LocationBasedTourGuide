package ParseObjects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.example.locationbasedtourguide.LoginActivity;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

@ParseClassName("LocationData")
public class LocationData extends ParseObject{

	private SaveCallback nameSavedLogger = new SaveCallback() {

		@Override
		public void done(ParseException arg0) {
			Log.i(LoginActivity.TAG, "Saved Location Data with name " + LocationData.this.getName());
		}

	};

	public final static String LOCAITON_CLASS = "LocationData",
			TEXT_KEY = "text", IMAGE_KEY = "image" , AUDIO_KEY = "audio",
			RADIUS_KEY = "raidus", ORDERING_ID_KEY = "ordering_id", GPS_KEY = "location",
			OWNING_TOUR_KEY = "owning_tour", NAME_KEY="name", NO_NAME_VALUE = "no name provided";

	//Arbitrary
	private final static double DEFAULT_RADIUS = 10;

	//This will get called by parse, DO NOT USE THIS CONSTRUCTOR
	public LocationData(){
		super();
		//this.setName(NO_NAME_VALUE);
//		this.saveInBackground(nameSavedLogger);
	}

	public LocationData(String name){
		super();
		this.setName(name);
		this.saveInBackground(nameSavedLogger);
	}

	public LocationData(String name, double lat, double lon){
		super();
		this.setName(name);
		this.setLocation(lat, lon);
		this.setRadius(DEFAULT_RADIUS);

		this.saveInBackground(nameSavedLogger);
	}

	public LocationData(String name, double lat, double lon, double radius){
		super();
		this.setName(name);
		this.setLocation(lat, lon);
		this.setRadius(radius);

		this.saveInBackground(nameSavedLogger);
	}

	public LocationData(String name, double lat, double lon, double radius, String text){
		super();
		this.setName(name);
		this.setLocation(lat, lon);
		this.setRadius(radius);
		this.setText(text);
		this.saveInBackground(nameSavedLogger);
	}

	public LocationData(String name, double lat, double lon, String text){
		super();
		this.setName(name);
		this.setLocation(lat, lon);
		this.setRadius(DEFAULT_RADIUS);
		this.setText(text);
		this.saveInBackground(nameSavedLogger);
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
		if(this.hasImage()){
			return BitmapFactory.decodeByteArray(this.getBytes(IMAGE_KEY),0,0);
		} else {
			return null;
		}
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
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setLocation(double lat, double lon){
		ParseGeoPoint geoPoint = new ParseGeoPoint(lat, lon);
		this.put(this.GPS_KEY,geoPoint);
		
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
