package com.example.locationbasedtourguide;

import tour.TourGalleryActivity;

import com.example.locationbasedtourguide.ParseInterface.LoginAndSignupAuthorizer;
import com.parse.Parse;

import CustomAnimations.ResizeAndTranslateAnimation;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LoginActivity extends Activity {

	private LoginAndSignupAuthorizer accountAuthorizer;

	private LoginPageViewHolder loginPageViewHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		//Will do this later if need be
		//Parse.enableLocalDatastore(this);
		Parse.initialize(this, "z4BT72BT3CZl4tas1zE9EKT0WPPeKMH8OpMWI2M9", "lrPhBajdOjiwPEcvGrg7VBHTxb9h4a7tlDYQpyJW");

		//Collect the views into the loginPageViewHolder ******************************************************************************
		this.loginPageViewHolder = new LoginPageViewHolder();
		loginPageViewHolder.mBaseLayout =(RelativeLayout) this.findViewById(R.id.login_layout);
		loginPageViewHolder.mLoginButton = (Button) findViewById(R.id.loginButton);
		loginPageViewHolder.mSignUpButton = (Button) findViewById(R.id.signupButton);
		loginPageViewHolder.mPasswordText = (EditText) findViewById(R.id.passwordText);
		loginPageViewHolder.mUserNameText = (EditText) findViewById(R.id.loginText);

		
		
//		loginPageViewHolder.mUserNameText.setAnimation(fade.);
//		loginPageViewHolder.mPasswordText.setAnimation(fade);


		//		loginPageViewHolder.mTopFlipper = (ViewFlipper) findViewById(R.id.topViewFlipper);
		//		loginPageViewHolder.mBottomFlipper= (ViewFlipper) findViewById(R.id.bottomViewFlipper);
		//		loginPageViewHolder.mTakeButton = (Button) findViewById(R.id.takeTourButton);
		//		loginPageViewHolder.mEditOrCreateButton = (Button) findViewById(R.id.editTourButton);
		//		
		//SetUp view flipper animation upon login ******************************************************************************
		//		loginPageViewHolder.mTopFlipper.setInAnimation(this,R.animator.fade_in);
		//		loginPageViewHolder.mTopFlipper.setOutAnimation(this,R.animator.fade_out);
		//		loginPageViewHolder.mBottomFlipper.setInAnimation(this,R.animator.fade_in);
		//		loginPageViewHolder.mBottomFlipper.setOutAnimation(this,R.animator.fade_out);



		accountAuthorizer = new LoginAndSignupAuthorizer(this,loginPageViewHolder); 

		// Set listeners *********************************************************************************************************************
		loginPageViewHolder.mPasswordText.setOnEditorActionListener(new EditText.OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					loginPageViewHolder.mLoginButton.performClick();

					InputMethodManager imm = (InputMethodManager)getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return true;
				}
				return false;
			}

		});

		loginPageViewHolder.mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				Toast.makeText(getApplicationContext(), "LOGIN CLICKED", Toast.LENGTH_SHORT).show();
				accountAuthorizer.loginUser(loginPageViewHolder.mUserNameText.getText().toString(),loginPageViewHolder.mPasswordText.getText().toString());
			}
		});

		loginPageViewHolder.mSignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "LOGIN SIGN-UP", Toast.LENGTH_SHORT).show();

				accountAuthorizer.signUpNewUser(loginPageViewHolder.mUserNameText.getText().toString(), loginPageViewHolder.mPasswordText.getText().toString());

			}
		});

//		loginPageViewHolder.mTakeButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				Intent i = new Intent(LoginActivity.this,TourGalleryActivity.class);
//				i.putExtra(TourGalleryActivity.TOUR_GALLERY_TYPE, TourGalleryActivity.TAKE);
//				startActivity(i);
//
//				//Can do a start activity forResult and then just finish in there, but 
//				//leaving as is for now because it is easier. 
//
//				//Prevents user from hitting the back button to get to the login screen
//				finish();
//			}
//		});
//
//		loginPageViewHolder.mEditOrCreateButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(LoginActivity.this,TourGalleryActivity.class);
//				i.putExtra(TourGalleryActivity.TOUR_GALLERY_TYPE, TourGalleryActivity.EDIT_AND_CREATE);
//				startActivity(i);
//				finish();
//			}
//		});


	}

	@Override
	public void onPause(){
		super.onPause();

		((AnimationDrawable)loginPageViewHolder.mBaseLayout.getBackground()).stop();	
	}

	@Override 
	public void onResume(){
		super.onResume();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		loginPageViewHolder.mBaseLayout.setBackground(getResources().getDrawable(R.drawable.background_animation));
		((AnimationDrawable)loginPageViewHolder.mBaseLayout.getBackground()).start();	
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	//I know that this is the incorrect use of this design pattern, but it will
	//make it easier to pass these fields to the loginClass
	public static class LoginPageViewHolder {
		public RelativeLayout mBaseLayout;
		public EditText mPasswordText, mUserNameText;
		public Button mLoginButton, mSignUpButton;

		public LoginPageViewHolder(){}
	}
}
