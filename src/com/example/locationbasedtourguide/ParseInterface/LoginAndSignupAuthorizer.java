package com.example.locationbasedtourguide.ParseInterface;

import java.util.List;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginAndSignupAuthorizer {

	public final int USER_ALREADY_EXISTS = 0, PASSWORD_INCORRECT = 1, SUCCESSFULL_LOGIN = 2, USER_DOES_NOT_EXIST = 3;
	public final int LOGIN = 0, PASSWORD = 1;
	private final String USER_NAME_KEY = "userName",PASSWORD_KEY = "password",
			USER_CLASS = "Users",

			USER_ALREADY_EXISTS_TOAST = "Sorry, this user-name is taken",
			INCORRECT_PASSWORD_TOAST = "Sorry, the password entered is incorrect",
			NON_EXISTANT_USER_TOAST = "Sorry, there does not exist a user with that name\nSelect signup to sign up as a new user";

	private Context context;

	public LoginAndSignupAuthorizer(Context context){
		this.context = context;
	}

	private boolean userExists(String user){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(USER_CLASS);
		query.whereEqualTo(USER_NAME_KEY, user);
		try {
			return query.find().size() == 1;
		} catch (ParseException e) {

		}
		return false;
	}


	private boolean userAndPasswordMatch(String user, String password){
		ParseQuery<ParseObject> query = ParseQuery.getQuery(USER_CLASS);
		query.whereEqualTo(USER_NAME_KEY, user);
		query.whereEqualTo(PASSWORD_KEY, password);
		try {
			return query.find().size() == 1;
		} catch (ParseException e) {

		}
		return false;
	}

	public void signUpNewUser(String user, String password){
		ParseUser pu = new ParseUser();
		pu.setUsername(user);
		pu.setPassword(password);

		pu.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(context, "Successful signup", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "That name has already been taken", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void loginUser(String user, String password){
		ParseUser.logInInBackground(user, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					Toast.makeText(context, "Successful login", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "Unsuccessful login", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private class NewUserSignUp extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			String user = params[LOGIN], password = params[PASSWORD];
			if(userExists(user)){
				return USER_ALREADY_EXISTS;
			}

			ParseObject po = new ParseObject(USER_CLASS);
			po.put(USER_NAME_KEY, user);
			po.put(PASSWORD_KEY,password);
			po.saveInBackground();

			return SUCCESSFULL_LOGIN;

		}

		@Override
		public void onPostExecute(Integer result){
			if(result == USER_ALREADY_EXISTS){
				Toast.makeText(context, USER_ALREADY_EXISTS_TOAST, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Successful creation of user", Toast.LENGTH_LONG).show();
			}

			//launch the tour guide

		}
	}

	private class UserLogin extends AsyncTask<String, Void, Integer>{
		@Override
		protected Integer doInBackground(String... params) {
			String user = params[LOGIN], password = params[PASSWORD];
			if(userExists(user)){
				if(userAndPasswordMatch(user, password)){
					return SUCCESSFULL_LOGIN;
				} else {
					return PASSWORD_INCORRECT;
				}
			} else {
				return USER_DOES_NOT_EXIST;
			}
		}

		@Override
		public void onPostExecute(Integer result){
			switch(result){

			case PASSWORD_INCORRECT:
				Toast.makeText(context, INCORRECT_PASSWORD_TOAST, Toast.LENGTH_LONG).show();
				return;
			case USER_DOES_NOT_EXIST:
				Toast.makeText(context, NON_EXISTANT_USER_TOAST, Toast.LENGTH_LONG).show();
				return;
			case SUCCESSFULL_LOGIN:
				Toast.makeText(context, "Successfull login", Toast.LENGTH_LONG).show();
				break;
			}

			//Launch tour guide here
		}
	}

}
