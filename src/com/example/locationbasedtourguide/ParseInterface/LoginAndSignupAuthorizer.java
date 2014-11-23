package com.example.locationbasedtourguide.ParseInterface;

import com.example.locationbasedtourguide.LoginActivity.LoginPageUiAnimator;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.content.Context;

import android.widget.Toast;

public class LoginAndSignupAuthorizer {

	public final int USER_ALREADY_EXISTS = 0, PASSWORD_INCORRECT = 1, SUCCESSFULL_LOGIN = 2, USER_DOES_NOT_EXIST = 3;
	public final int LOGIN = 0, PASSWORD = 1;
	private Context context;
	private LoginPageUiAnimator views;

	public LoginAndSignupAuthorizer(Context context, LoginPageUiAnimator views){
		this.context = context;
		this.views = views;
	}

	public void signUpNewUser(String user, String password){
		ParseUser pu = new ParseUser();
		pu.setUsername(user);
		pu.setPassword(password);

		pu.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(context, "Successful signup", Toast.LENGTH_LONG).show();					
					views.updateUIToAskForMakeOrTake();
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

					views.updateUIToAskForMakeOrTake();
				} else {
					Toast.makeText(context, "Unsuccessful login", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
