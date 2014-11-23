package com.example.locationbasedtourguide;

import tour.TourGalleryActivity;

import com.example.locationbasedtourguide.ParseInterface.LoginAndSignupAuthorizer;
import com.parse.Parse;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

public class LoginActivity extends Activity {

	private LoginAndSignupAuthorizer accountAuthorizer;

	private LoginPageUiAnimator loginPageViewHolder;
	public RelativeLayout mBaseLayout;
	public EditText mPasswordText, mUserNameText;
	public Button mLoginButton, mSignUpButton;
	public OnClickListener mMakeOrEditListener, mTakeListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		//Will do this later if need be
		//Parse.enableLocalDatastore(this);
		Parse.initialize(this, "z4BT72BT3CZl4tas1zE9EKT0WPPeKMH8OpMWI2M9", "lrPhBajdOjiwPEcvGrg7VBHTxb9h4a7tlDYQpyJW");


		mMakeOrEditListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(LoginActivity.this,TourGalleryActivity.class);
				i.putExtra(TourGalleryActivity.TOUR_GALLERY_TYPE, TourGalleryActivity.EDIT_AND_CREATE);
				startActivity(i);

				//Can do a start activity forResult and then just finish in there, but 
				//leaving as is for now because it is easier. 

				//Prevents user from hitting the back button to get to the login screen
				finish();

			}
		};
		
		mTakeListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(LoginActivity.this,TourGalleryActivity.class);
				i.putExtra(TourGalleryActivity.TOUR_GALLERY_TYPE, TourGalleryActivity.TAKE);
				startActivity(i);

				//Can do a start activity forResult and then just finish in there, but 
				//leaving as is for now because it is easier. 

				//Prevents user from hitting the back button to get to the login screen
				finish();
			}
		};
		
		//Collect the views into the loginPageViewHolder ******************************************************************************
		this.loginPageViewHolder = new LoginPageUiAnimator();
		this.mBaseLayout =(RelativeLayout) this.findViewById(R.id.login_layout);
		this.mLoginButton = (Button) findViewById(R.id.loginButton);
		this.mSignUpButton = (Button) findViewById(R.id.signupButton);
		this.mPasswordText = (EditText) findViewById(R.id.passwordText);
		this.mUserNameText = (EditText) findViewById(R.id.loginText);

		accountAuthorizer = new LoginAndSignupAuthorizer(this,loginPageViewHolder); 

		// Set listeners *********************************************************************************************************************
		this.mPasswordText.setOnEditorActionListener(new EditText.OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					mLoginButton.performClick();

					InputMethodManager imm = (InputMethodManager)getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});

		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				Toast.makeText(getApplicationContext(), "LOGIN CLICKED", Toast.LENGTH_SHORT).show();
				accountAuthorizer.loginUser(mUserNameText.getText().toString(),mPasswordText.getText().toString());
			}
		});

		mSignUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "LOGIN SIGN-UP", Toast.LENGTH_SHORT).show();

				accountAuthorizer.signUpNewUser(mUserNameText.getText().toString(), mPasswordText.getText().toString());

			}
		});
	}

	@Override
	public void onPause(){
		super.onPause();
		((AnimationDrawable)mBaseLayout.getBackground()).stop();	
	}

	@Override 
	public void onResume(){
		super.onResume();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		mBaseLayout.setBackground(getResources().getDrawable(R.drawable.background_animation));
		((AnimationDrawable)mBaseLayout.getBackground()).start();	
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

// * ****************************** ANIMATION STUFF ************************************************
	//Effectively just a way of passing a set of methods to the LoginAndSignup class
	
	//Should act like a singleton, but going through the hoops to implement it in an
	//inner class is unnecessary work
	public class LoginPageUiAnimator {

		private LoginPageUiAnimator(){}
		
		private static final int animationLength = 750;
		
		
		//PLEASE IGNORE THESE PART. A LOT OF GUESS AND CHECK TILL STUFF WORKED. 
		//WARNING: HORRIBLE CODE AHEAD
		private void animate(final View v, boolean top){
			int width = mPasswordText.getWidth();
			float x = mUserNameText.getX(),
					y = mUserNameText.getY() - 70,
					botX = mPasswordText.getX(),
					botY = mPasswordText.getY() + 120 ;
			if(!top){
				x= botX;
				y = botY;
			}

			//did x - 72 because it made the padding slightly greater on the top selection -> better aesthetically
			PropertyValuesHolder xPropertyHolder = PropertyValuesHolder.ofFloat("posX", v.getX(), x- 75/2); //center with 75/2
			PropertyValuesHolder yPropertyHolder = PropertyValuesHolder.ofFloat("posY", v.getY(), y);
			PropertyValuesHolder widthPropertyHolder = PropertyValuesHolder.ofFloat("width", v.getWidth(), width+75);
			PropertyValuesHolder alphaPropertyHolder = PropertyValuesHolder.ofFloat("alpha", mPasswordText.getAlpha(), 0);
			ValueAnimator mTranslationAnimator = ValueAnimator.ofPropertyValuesHolder(widthPropertyHolder, xPropertyHolder, yPropertyHolder,alphaPropertyHolder);
			mTranslationAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float posX = (Float) animation.getAnimatedValue("posX");
					float posY = (Float) animation.getAnimatedValue("posY");
					Float width = (Float) animation.getAnimatedValue("width");
				
					//change button position
					v.setX(posX);
					v.setY(posY);
					
					//Change button width
					v.getLayoutParams().width = width.intValue();
					v.requestLayout();
					
					//fade out
					mPasswordText.setAlpha((Float) animation.getAnimatedValue("alpha"));
					mUserNameText.setAlpha((Float) animation.getAnimatedValue("alpha"));
					
					//Remove the editTexts so the keyboard won't stay open or or re-open
					if((Float) animation.getAnimatedValue("alpha") <= .01){
						mPasswordText.setVisibility(View.GONE);
						mUserNameText.setVisibility(View.GONE);
					}

				}
			});
			mTranslationAnimator.setDuration(animationLength);
			RelativeLayout.LayoutParams tmp = new RelativeLayout.LayoutParams(v.getWidth(),v.getHeight());

			//way too much trial and error to know what is happening, need the +1 or the top button disappears
			tmp.rightMargin = top ? mSignUpButton.getRight() + 1 : v.getRight();
			tmp.leftMargin =  top ? mPasswordText.getLeft() :  v.getLeft();
			tmp.topMargin = v.getTop();
			tmp.bottomMargin = v.getBottom();
			v.setLayoutParams(tmp);

			mTranslationAnimator.start();
		}

		public void updateUIToAskForMakeOrTake(){
			//BUFFER_DO_NOT_USE is just a way of keeping a gap between the buttons in 
			//a relative layout
			mBaseLayout.findViewById(R.id.BUFFER_DO_NOT_USE).setVisibility(View.GONE);
			animate(mLoginButton, true);
			animate(mSignUpButton, false);
			mSignUpButton.setText("TAKE TOUR");
			mLoginButton.setText("MAKE OR CREATE TOUR");
			mLoginButton.setOnClickListener(mMakeOrEditListener);
			mSignUpButton.setOnClickListener(mTakeListener);
		}
	}
}
