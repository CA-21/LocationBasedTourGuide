package com.example.locationbasedtourguide.ParseInterface;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


import com.example.locationbasedtourguide.Tour;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Used as the interface for a tour's saving, downloading, and updating.
	note that some calls are asynchronous
 * @author matt
 *
 */
public class TourGetterAndCreator {
	
	/**
	 *	Returns a tour with name tourName. If newTour is true then
	 *	a new tour is created and pushed to parse, otherwise an old
	 *	tour is retrieved from parse. If newTour is false and no
	 *  existing tours match the name then null is returned.
	 */
	public static Tour getTour(String tourName, boolean newTour){
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<Tour> query = Tour.getQuery();
		query.whereEqualTo("user", currentUser);
		query.whereEqualTo(Tour.NAME_KEY, tourName);
		try {
			List<Tour> tour = query.find();
			if( tour.size() == 0){
				Tour tourToReturn = new Tour(tourName);
				tourToReturn.put("user",ParseUser.getCurrentUser());
				tourToReturn.put(Tour.NAME_KEY, tourName);
				tourToReturn.saveInBackground();
				
				return tourToReturn;
			} else if(newTour){
				return tour.get(0);
			} 
		} catch (ParseException e) {

		}
		return null;
	}
	
	/**
	 * Retrieves all tour names from Parse. If currentUserOnly is true,
	 * only tour names of tours created by the current user will be returned
	 */
	public static SortedSet<String> getAllTourNames(boolean currentUserOnly){
		ParseQuery<Tour> query = Tour.getQuery();
		SortedSet<String> toRet = new TreeSet<String>();
		if(currentUserOnly){
			ParseUser currentUser = ParseUser.getCurrentUser();
			query.whereEqualTo("user", currentUser);
		}
		try{
			List<Tour> namesPo = query.find();
			for(Tour po : namesPo){
				toRet.add(po.getName());
			}
		}catch(ParseException e){
			//make this better later
			return null;
		}
		return toRet;
	}
}
