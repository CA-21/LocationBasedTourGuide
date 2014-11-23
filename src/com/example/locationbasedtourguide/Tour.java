package com.example.locationbasedtourguide;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

@ParseClassName("Tour")
public class Tour extends ParseObject implements Comparable<Tour>{

	public final static String NAME_KEY = "name", LOCATIONS_KEY = "locaitons";

	//save this as an array when serializing out
	private ArrayList<LocationData> mLocations;

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

	/*
	 * returns the Location data if the id exists, null otherwise
	 */
	public LocationData getLocationData(int position ){
		return this.mLocations.size() <= position ? null : mLocations.get(position);
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
	
	/*
	 * for the purpose of parse, we need to update this object in the cloud.
	 * To do so we just dump the array of locations and add in the new one
	 */
	private void updateLocations(){
		this.removeAll(LOCATIONS_KEY, this.getList(LOCATIONS_KEY));
		this.addAll(LOCATIONS_KEY, this.mLocations);
	}
	
	public void synchronousSave() throws ParseException{
		updateLocations();
		this.save();
	}

	public void asyncSave(){
		updateLocations();
		this.saveInBackground();
	}

	public void asyncSave(SaveCallback saveCallback){
		updateLocations();
		this.saveInBackground(saveCallback);
	}
	
	public void asyncSaveEventually(){
		updateLocations();
		this.saveEventually();
	}
	
	public void asyncSaveEventually(SaveCallback saveCallback){
		updateLocations();
		this.saveEventually(saveCallback);
	}
	
	public static ParseQuery<Tour> getQuery(){
		return ParseQuery.getQuery(Tour.class);
	}
}
