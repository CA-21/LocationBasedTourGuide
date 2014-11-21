package com.example.locationbasedtourguide.ParseInterface;

import com.example.locationbasedtourguide.Tour;
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
	private Context context;

	public LoginAndSignupAuthorizer(Context context){
		this.context = context;
	}
	
	public void signUpNewUser(String user, String password){
		ParseUser pu = new ParseUser();
		pu.setUsername(user);
		pu.setPassword(password);

		pu.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(context, "Successful signup", Toast.LENGTH_LONG).show();					
					//Launch tour guide activity
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
					//launch tour guide activity
				} else {
					Toast.makeText(context, "Unsuccessful login", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
