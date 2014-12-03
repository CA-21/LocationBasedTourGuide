package ParseObjects;

import java.util.ArrayList;
import java.util.List;


import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser{

	private User(){
		super();
	}
	
	public final static String CURRENT_TOUR_KEY = "current_tour",
			VISITED_LOCATION_DATA_KEY = "visited_location_data";

	private void setCurrentTour(String tourName){
		this.put(CURRENT_TOUR_KEY,tourName);
	}

	private void setVisitedLocations(ArrayList<Integer> locationIndicies){
		this.remove(VISITED_LOCATION_DATA_KEY);
		this.addAll(VISITED_LOCATION_DATA_KEY, locationIndicies);
	}

	private void addVisitedLocationIndex(int i ){
		this.add(VISITED_LOCATION_DATA_KEY, i);
	}

	private void clearTour(){
		this.remove(CURRENT_TOUR_KEY);
		this.remove(VISITED_LOCATION_DATA_KEY);
	}

	private List<Integer> getVisitedLocations(){
		return this.getList(VISITED_LOCATION_DATA_KEY);
	}
	
	public static void setCurrentTourForUser(String tourName){
		User curUser = (User) ParseUser.getCurrentUser();
		curUser.setCurrentTour(tourName);
	}
	
	public static void addVisitedLocationIndexForCurrentUser(int i){
		User curUser = (User) ParseUser.getCurrentUser();
		curUser.addVisitedLocationIndex(i);
	}
	
	public static void setVisitedLocationsForCurrentUser(ArrayList<Integer> locationIndicies){
		User curUser = (User) ParseUser.getCurrentUser();
		curUser.setVisitedLocations(locationIndicies);
	}
	
	public static void clearCurrentUserTour(){
		User curUser = (User) ParseUser.getCurrentUser();
		curUser.clearTour();
	}
	
	public static List<Integer> getVisitedLocationsForUser(){
		User curUser = (User) ParseUser.getCurrentUser();
		return curUser.getVisitedLocations();
	}
	
	public static void asyncSaveCurrentUserData(){
		User curUser = (User) ParseUser.getCurrentUser();
		curUser.saveInBackground();
	}	
}
