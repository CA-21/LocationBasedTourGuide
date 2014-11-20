package com.example.locationbasedtourguide;

import com.example.locationbasedtourguide.ParseInterface.LoginAndSignupAuthorizer;
import com.parse.Parse;

import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends Activity {
	private LoginAndSignupAuthorizer accountAuthorizer;

	private RelativeLayout mBaseLayout;
	private EditText mPasswordText, mUserNameText;
	private Button mLoginButton, mSignUpButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		//Will do this later if need be
		//Parse.enableLocalDatastore(this);
		Parse.initialize(this, "z4BT72BT3CZl4tas1zE9EKT0WPPeKMH8OpMWI2M9", "lrPhBajdOjiwPEcvGrg7VBHTxb9h4a7tlDYQpyJW");

		accountAuthorizer = new LoginAndSignupAuthorizer(this);
		mBaseLayout =(RelativeLayout) this.findViewById(R.id.login_layout);
		mLoginButton = (Button) findViewById(R.id.loginButton);
		mSignUpButton = (Button) findViewById(R.id.signupButton);
		mPasswordText = (EditText) findViewById(R.id.passwordText);
		mUserNameText = (EditText) findViewById(R.id.loginText);

		mPasswordText.setOnEditorActionListener(new EditText.OnEditorActionListener(){

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
				accountAuthorizer.loginUser(mUserNameText.getText().toString(), mPasswordText.getText().toString());
			}
		});

		mSignUpButton.findViewById(R.id.signupButton).setOnClickListener(new OnClickListener() {

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
}
