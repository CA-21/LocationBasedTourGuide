package com.example.locationbasedtourguide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

@ParseClassName("Tour")
public class Tour extends ParseObject implements Comparable<Tour>{

	public final static String NAME_KEY = "name", LOCATIONS_KEY = "Locations", IMAGE_KEY = "image";

	//save this as an array when serializing out
	private ArrayList<LocationData> mLocations;

	/**
	 * DO NOT CALL THIS DIRECTLY. USE TourGettingAndCreator
	 * @param name
	 */
	public Tour(){
		super();
	}
	/**
	 * DO NOT CALL THIS DIRECTLY. USE TourGettingAndCreator
	 * @param name
	 */
	public Tour(String name){
		this.add(NAME_KEY,  name);
		mLocations = new ArrayList<LocationData>();
	}

	public Tour(String name, ArrayList<LocationData> locations){
		this.add(NAME_KEY, name);
		this.mLocations = locations;
	}

	/*
	 * returns the location data that was already present with the same
	 * key, otherwise null
	 */
	public void addLocation(LocationData data){
		mLocations.add(data);
	}

	public void addLocation(LocationData data, int position){
		mLocations.add(position,data);

	}

	/*
	 * returns the location removed, null if no location existed with ID i
	 */
	public LocationData removeLocation(int i){
		if(i >= mLocations.size())
			return null;

		LocationData toRet = this.mLocations.remove(i);
		this.mLocations.remove(i);
		return toRet;
	}

	public void removeLocation(String id){

	}

	public String getName(){
		return this.getString(NAME_KEY);
	}
	
	public String getAuthorName(){
		return this.getParseUser("user").getUsername();
	}

	/*
	 * returns the Location data if the id exists, null otherwise
	 */
	public LocationData getLocationData(int position ){
		return this.mLocations.size() <= position ? null : mLocations.get(position);
	}

	public Bitmap getImage(){
		Bitmap toRet = null;
		if(this.has(IMAGE_KEY)){
			ParseFile pf = this.getParseFile(IMAGE_KEY);
			try {

				toRet = BitmapFactory.decodeByteArray(pf.getData(),0,pf.getData().length);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return toRet;
	}
	
	public void setImage(Bitmap input){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		input.compress(CompressFormat.PNG, 100, stream);
		byte[] toRet = stream.toByteArray();
		ParseFile pf = new ParseFile("tourImage.png",toRet);
		this.put(IMAGE_KEY, pf);
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Returns all of the locationData. The returned list is a shallow copy
	 * of the internal data, so modifications to the structure of the list itself
	 * will not be retained. To modify the structure, use the appropriate add/remove
	 * methods provided
	 */
	public ArrayList<LocationData> getAllLocationData(){
		ArrayList<LocationData> toRet = new ArrayList<LocationData>();
		for(LocationData ld : mLocations){
			toRet.add(ld);
		}
		return toRet;
	}

	//Call this after downloading the tour
	public void initializeTour(){
		this.mLocations = (ArrayList) this.get(LOCATIONS_KEY);
	}

	@Override
	public int compareTo(Tour another) {
		return this.getName().compareTo(another.getName());
	}

	//Just compare the names
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;

		if(o.getClass() != Tour.class){
			return false;
		}
		return ((Tour)o).getName().equals(this.getName());
	}

	public void asyncSave(){
		(new DataPersister()).execute();
	}

	public void asyncSave(SaveCallback saveCallback){	
		(new DataPersister()).execute(saveCallback);
	}


	public static ParseQuery<Tour> getQuery(){
		return ParseQuery.getQuery(Tour.class);
	}

	/**
	 * Saves the location data serially, then saves the overall tour in the background.
	 * Needed to ensure that the location data is up to date before this tour is saved
	 * @author matt
	 *
	 */
	private class DataPersister extends AsyncTask<SaveCallback, Void, SaveCallback>{

		@Override
		protected SaveCallback doInBackground(SaveCallback... params) {
			if(Tour.this.getList(LOCATIONS_KEY) != null)
				Tour.this.removeAll(LOCATIONS_KEY, getList(LOCATIONS_KEY));
			for(LocationData ld : mLocations){
				try {
					ld.save();
				} catch (ParseException e) {

					e.printStackTrace();
				}
			}
			Tour.this.addAll(LOCATIONS_KEY, Tour.this.mLocations);

			return params.length == 0 ? null : params[0];
		}

		@Override
		protected void onPostExecute(SaveCallback c){
			if(c == null)
				Tour.this.saveInBackground();
			else 
				Tour.this.saveInBackground(c);
		}

	}
}
