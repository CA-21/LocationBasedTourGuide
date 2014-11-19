package com.example.locationbasedtourguide;

import com.example.locationbasedtourguide.ParseInterface.LoginAndSignupAuthorizer;
import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private LoginAndSignupAuthorizer accountAuthorizer;
	
	private RelativeLayout mBaseLayout;
	private EditText mPasswordText, mUserNameText;
	private Button mLoginButton, mSignUpButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		Parse.initialize(this, "z4BT72BT3CZl4tas1zE9EKT0WPPeKMH8OpMWI2M9", "lrPhBajdOjiwPEcvGrg7VBHTxb9h4a7tlDYQpyJW");
		
		accountAuthorizer = new LoginAndSignupAuthorizer(this);
		mBaseLayout =(RelativeLayout) this.findViewById(R.id.login_layout);
		mLoginButton = (Button) findViewById(R.id.loginButton);
		mSignUpButton = (Button) findViewById(R.id.signupButton);
		mPasswordText = (EditText) findViewById(R.id.passwordText);
		mUserNameText = (EditText) findViewById(R.id.loginText);
		
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
