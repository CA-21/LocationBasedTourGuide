package com.example.locationbasedtourguide.ParseInterface;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TourDataSaverAndRetriever {

	private final String TOURS = "tours";

	String tourName;
	public TourDataSaverAndRetriever(String tourName){
		this.tourName = tourName;
	}
	
	//may need this not sure
	private boolean tourAlreadyExists(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(TOURS);
		query.whereEqualTo("user", currentUser);
		query.whereEqualTo("name", tourName);
		try {
			return query.find().size() != 0;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Async call, no threading needed
	 */
	public void createNewTour(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(TOURS);
		query.whereEqualTo("user", currentUser);
		query.whereEqualTo("name", tourName);
		try {
			if( query.find().size() == 0){
				ParseObject tour = new ParseObject(TOURS);
				tour.put("user",ParseUser.getCurrentUser());
				tour.put("name", tourName);
				tour.saveInBackground();
			}
		} catch (ParseException e) {
			
		}
	}
	
	public boolean pinAlreadyExistsInLocation(){
		return false;
	}

	

}
