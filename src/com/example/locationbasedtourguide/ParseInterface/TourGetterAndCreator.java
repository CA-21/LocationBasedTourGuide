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
	 *	a new tour is created and pushed to Parse, otherwise an old
	 *	tour is retrieved from Parse. If newTour is false and no
	 *  existing tours match the name then null is returned.
	 *  
	 * @param tourName Name of the tour to get/create
	 * @param newTour true if this is to create a new tour with name tourName
	 * @throws TourWithSameNameExistsException 
	 */
	public static Tour getTour(String tourName, boolean newTour) throws TourWithSameNameExistsException{
//		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<Tour> query = Tour.getQuery();
//		if(currentUserOnly)
//			query.whereEqualTo("user", currentUser);
		Tour tourToReturn = null;
		query.whereEqualTo(Tour.NAME_KEY, tourName);
		query.include(Tour.LOCATIONS_KEY);
		try {
			if(newTour){
				if(query.find().size() != 0){
					throw new TourWithSameNameExistsException(tourName);
				}
				//note to self, check for existing.
				tourToReturn = new Tour(tourName);
				tourToReturn.put("user",ParseUser.getCurrentUser());
				tourToReturn.put(Tour.NAME_KEY, tourName);
				tourToReturn.saveInBackground();
				return tourToReturn;
			} else {
				List<Tour> tour = query.find();
				if(tour.size() != 0){
					tourToReturn = tour.get(0);
					tourToReturn.initializeTour();
				}
			}
		} catch (ParseException e) {

		}
		//return null;
		return tourToReturn;
	}

	/**
	 * Retrieves all tour names from Parse. If currentUserOnly is true,
	 * only tour names of tours created by the current user will be returned
	 */
	/*
	 * Should this be beefed up to be massive, we'll need to have some way of selecting tours based
	 * off of some criterion such as alphabetically or by some keywords etc... Downloads could be
	 * done in groups such that the entire database won't just be downloaded to every device.
	 * 
	 * With the above in mind, it's possible we will need to change some protocols on how we define
	 * what is a tour, such as asking Parse for some unique tour IDs so that names are not the keys
	 * we use
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

	public static class TourWithSameNameExistsException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		public TourWithSameNameExistsException(String nameOfTour){
			super("Tour with name " + nameOfTour + " already exists.");
		}
	}
}
