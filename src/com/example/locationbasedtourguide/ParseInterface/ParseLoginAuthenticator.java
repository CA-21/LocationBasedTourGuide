package com.example.locationbasedtourguide.ParseInterface;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.AsyncTask;

public class ParseLoginAuthenticator extends AsyncTask<String, Void, Boolean>{

	public final int LOGIN = 0, PASSWORD = 1;
	
	private final String USER_NAME_KEY = "userName",PASSWORD_KEY = "password",
			USER_CLASS = "Users";
	@Override
	protected Boolean doInBackground(String... params) {
		String login = params[LOGIN], password = params[PASSWORD];
		ParseQuery<ParseObject> query = ParseQuery.getQuery(USER_CLASS);
		query.whereEqualTo(USER_NAME_KEY, login);
		query.whereEqualTo(PASSWORD_KEY, password);
		try {
			List<ParseObject> res = query.find();
			return res.size() == 0 ? false : true;
		} catch (ParseException e) {
			return false;
			
//			e.printStackTrace();

		}/*(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
				if (e == null) {
					Log.d("score", "Retrieved " + scoreList.size() + " scores");
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}


		});*/
	}

}
