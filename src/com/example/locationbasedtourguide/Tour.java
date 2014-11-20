package com.example.locationbasedtourguide;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class Tour implements Comparable<Tour> {
	
	public final static String NAME_KEY = "name", TOUR_CLASS = "Tours";
	
	private String name;
	private SortedMap<Integer,LocationData> locations;
	private int nextLocationNumber;
	
	public Tour(String name){
		this.name = name;
		nextLocationNumber = 0;
		locations = new TreeMap<Integer,LocationData>();
	}
	
	public Tour(String name, Collection<LocationData> locations){
		this.name = name;
		for(LocationData loc : locations){
			if(loc.getChronologicalOrdering() > nextLocationNumber){
				nextLocationNumber = loc.getChronologicalOrdering();
			}
			this.locations.put(loc.getChronologicalOrdering(), loc);
		}
	}
	
	/*
	 * returns the location data that was already present with the same
	 * key, otherwise null
	 */
	public LocationData addLocation(LocationData data){
		LocationData retVal = null;
		if(locations.containsKey(data.getChronologicalOrdering())){
			retVal = locations.get(data.getChronologicalOrdering());
		}
		locations.put(data.getChronologicalOrdering(), data);
		return retVal;
	}
	
	/*
	 * returns the location removed, null if no location existed with ID i
	 */
	public LocationData removeLocation(int i){
		LocationData toRet = this.locations.containsKey(i) ? this.locations.get(i) : null;
		this.locations.remove(i);
		return toRet;
	}
	
	public String getName(){
		return name;
	}
	
	/*
	 * returns the Location data if the id exists, null otherwise
	 */
	public LocationData getLocationData(int i ){
		return this.locations.containsKey(i) ? this.locations.get(i) : null;
	}
	
	@Override
	public int compareTo(Tour another) {
		return name.compareTo(another.name);
	}
	
	
	//Just compare the names
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		
		if(o.getClass() != Tour.class){
			return false;
		}
		return ((Tour)o).name.equals(this.name);
	}

}
