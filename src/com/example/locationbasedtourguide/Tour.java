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
	private ArrayList<LocationData> locations;

	public Tour(String name){
		this.add(NAME_KEY,  name);
		locations = new ArrayList<LocationData>();
	}

	public Tour(String name, ArrayList<LocationData> locations){
		this.add(NAME_KEY, name);
		this.locations = locations;
	}

	/*
	 * returns the location data that was already present with the same
	 * key, otherwise null
	 */
	public void addLocation(LocationData data){
		locations.add(data);
	}

	public void addLocation(LocationData data, int position){
		locations.add(position,data);
		
	}

	/*
	 * returns the location removed, null if no location existed with ID i
	 */
	public LocationData removeLocation(int i){
		if(i >= locations.size())
			return null;

		LocationData toRet = this.locations.remove(i);
		this.locations.remove(i);
		return toRet;
	}

	public String getName(){
		return this.getString(NAME_KEY);
	}

	/*
	 * returns the Location data if the id exists, null otherwise
	 */
	public LocationData getLocationData(int i ){
		return this.locations.size() <= i ? null : locations.get(i);
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
	
	private void updateLocations(){
		this.removeAll(LOCATIONS_KEY, this.getList(LOCATIONS_KEY));
		this.addAll(LOCATIONS_KEY, this.locations);
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
	
	public static ParseQuery<Tour> getQuery(){
		return ParseQuery.getQuery(Tour.class);
	}
}
