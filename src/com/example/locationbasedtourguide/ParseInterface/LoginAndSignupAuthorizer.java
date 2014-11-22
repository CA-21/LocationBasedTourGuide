package com.example.locationbasedtourguide.ParseInterface;


import tour.TourGalleryActivity;
import com.example.locationbasedtourguide.R;
import com.example.locationbasedtourguide.LoginActivity.LoginPageViewHolder;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
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
	
	
	/* ************************************************************************
	 * GUI stuff for the login page is handled below
	 * 
	 */

	private static final int animationLength = 750;
	
	private void animate(final View v, boolean top){
		int width = views.mPasswordText.getWidth();
		float x = views.mUserNameText.getX(),
				y = views.mUserNameText.getY() - 70,
				botX = views.mPasswordText.getX(),
				botY = views.mPasswordText.getY() + 120 ;
		if(!top){
			x= botX;
			y = botY;
		}

		
		//did x - 72 because it made the padding slightly greater on the top selection -> better aesthetically
		PropertyValuesHolder xPropertyHolder = PropertyValuesHolder.ofFloat("posX", v.getX(), x- 75/2); //center with 75/2
		PropertyValuesHolder yPropertyHolder = PropertyValuesHolder.ofFloat("posY", v.getY(), y);
		PropertyValuesHolder widthPropertyHolder = PropertyValuesHolder.ofFloat("width", v.getWidth(), width+75);
		PropertyValuesHolder alphaPropertyHolder = PropertyValuesHolder.ofFloat("alpha", views.mPasswordText.getAlpha(), 0);
		ValueAnimator mTranslationAnimator = ValueAnimator.ofPropertyValuesHolder(widthPropertyHolder, xPropertyHolder, yPropertyHolder,alphaPropertyHolder);
		mTranslationAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float posX = (Float) animation.getAnimatedValue("posX");
				float posY = (Float) animation.getAnimatedValue("posY");
				Float width = (Float) animation.getAnimatedValue("width");
				Log.i("SOMETHING","WIDTH IS " + width);
				v.setX(posX);
				v.setY(posY);
				v.getLayoutParams().width = width.intValue();
				v.requestLayout();
				views.mPasswordText.setAlpha((Float) animation.getAnimatedValue("alpha"));
				views.mUserNameText.setAlpha((Float) animation.getAnimatedValue("alpha"));
				if((Float) animation.getAnimatedValue("alpha") <= .01){
					//Remove the editTexts
					views.mPasswordText.setVisibility(View.GONE);
					views.mUserNameText.setVisibility(View.GONE);
				}

			}
		});
		mTranslationAnimator.setDuration(animationLength);
		RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(v.getWidth(),v.getHeight());

		//way too much trial and error to know what is happening, need the +1 or the top button disappears
		tmp.rightMargin = top ? views.mSignUpButton.getRight() + 1 : v.getRight();
		tmp.leftMargin =  top ? views.mPasswordText.getLeft() :  v.getLeft();
		tmp.topMargin = v.getTop();
		tmp.bottomMargin = v.getBottom();
		v.setLayoutParams(tmp);

		mTranslationAnimator.start();
	}

	private void updateUIToAskForMakeOrTake(){
		views.mBaseLayout.findViewById(R.id.BUFFER_DO_NOT_USE).setVisibility(View.GONE);
		animate(views.mLoginButton, true);
		animate(views.mSignUpButton, false);
		views.mSignUpButton.setText("TAKE TOUR");
		views.mLoginButton.setText("MAKE OR CREATE TOUR");

		views.mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity cur = (Activity) context;

				Intent i = new Intent(cur,TourGalleryActivity.class);
				i.putExtra(TourGalleryActivity.TOUR_GALLERY_TYPE, TourGalleryActivity.EDIT_AND_CREATE);
				cur.startActivity(i);

				//Can do a start activity forResult and then just finish in there, but 
				//leaving as is for now because it is easier. 

				//Prevents user from hitting the back button to get to the login screen
				cur.finish();

			}
		});

		views.mSignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity cur = (Activity) context;

				Intent i = new Intent(cur,TourGalleryActivity.class);
				i.putExtra(TourGalleryActivity.TOUR_GALLERY_TYPE, TourGalleryActivity.TAKE);
				cur.startActivity(i);

				//Can do a start activity forResult and then just finish in there, but 
				//leaving as is for now because it is easier. 

				//Prevents user from hitting the back button to get to the login screen
				cur.finish();
			}
		});
	}
}
