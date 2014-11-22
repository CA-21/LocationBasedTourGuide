package com.example.locationbasedtourguide.ParseInterface;

import com.example.locationbasedtourguide.LoginActivity.LoginPageViewHolder;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.content.Context;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginAndSignupAuthorizer {

	public final int USER_ALREADY_EXISTS = 0, PASSWORD_INCORRECT = 1, SUCCESSFULL_LOGIN = 2, USER_DOES_NOT_EXIST = 3;
	public final int LOGIN = 0, PASSWORD = 1;
	private Context context;
	private LoginPageViewHolder views;

	public LoginAndSignupAuthorizer(Context context, LoginPageViewHolder views){
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
					updateUIToAskForMakeOrTake();
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
					
					updateUIToAskForMakeOrTake();
				} else {
					Toast.makeText(context, "Unsuccessful login", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void updateUIToAskForMakeOrTake(){
		views.mTopFlipper.showNext();
		views.mBottomFlipper.showNext();
		views.mLoginButton.setVisibility(View.GONE);
		views.mSignUpButton.setVisibility(View.GONE);
		
		//Buttons should be set up to launch the tour activity
	}
}
