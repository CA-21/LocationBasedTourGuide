package com.example.locationbasedtourguide.ParseInterface;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.example.locationbasedtourguide.LocationData;
import com.example.locationbasedtourguide.Tour;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Used as the interface for a tour's saving, downloading, and updating.
	note that some calls are asynchronous
 * @author matt
 *
 */
public class TourDataSaverAndRetriever {

	private String tourName;
	private Tour tour;

	public TourDataSaverAndRetriever(String tourName){
		this.tourName = tourName;
		this.tour = null;
	}
	
	//may need this not sure
	public static boolean tourAlreadyExists(String nameOfTour){
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Tour.TOUR_CLASS);
		query.whereEqualTo("user", currentUser);
		query.whereEqualTo(Tour.NAME_KEY, nameOfTour);
		try {
			List<ParseObject> tours = query.find();
			if(tours.size() != 1){
				return false;
			} else {
				tours.get(0);
				return true;
			}
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Async call, no threading needed
	 */
	private void createNewTour(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Tour.TOUR_CLASS);
		query.whereEqualTo("user", currentUser);
		query.whereEqualTo(Tour.NAME_KEY, tourName);
		try {
			if( query.find().size() == 0){
				ParseObject tourObject = new ParseObject(Tour.TOUR_CLASS);
				tourObject.put("user",ParseUser.getCurrentUser());
				tourObject.put(Tour.NAME_KEY, tourName);
				tourObject.saveInBackground();
				
				this.tour = new Tour(this.tourName);
			}
		} catch (ParseException e) {

		}
	}
	
	private void getExistingTour(String nameOfTour){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(LocationData.LOCAITON_CLASS);
		query.whereEqualTo(LocationData.OWNING_TOUR, nameOfTour);
		try{
			List<ParseObject> locations = query.find();
			
		} catch(ParseException e){
			
		}
	}
	
	public static SortedSet<String> getAllTourNames(boolean currentUserOnly){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Tour.TOUR_CLASS);
		SortedSet<String> toRet = new TreeSet<String>();
		if(currentUserOnly){
			ParseUser currentUser = ParseUser.getCurrentUser();
			query.whereEqualTo("user", currentUser);
		}
		try{
			List<ParseObject> namesPo = query.find();
			for(ParseObject po : namesPo){
				toRet.add(po.getString(Tour.NAME_KEY));
			}
		}catch(ParseException e){
			//make this better later
			return null;
		}
		return toRet;
	}


}
