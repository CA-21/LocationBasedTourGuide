package tour;

import com.example.locationbasedtourguide.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class TourGalleryActivity extends Activity{
	
	private final static String TAG = "TOUR GALLERY ACTIVITY";
	
	public final static String TOUR_GALLERY_TYPE ="tour gallery type";
	public final static int ERROR = -1, EDIT_AND_CREATE = 0, TAKE = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_gallery_layout);
		
		//The extras int he intent will determine whether this is an 
		//edit/create tour activity or take tour activity
		
		switch(getIntent().getIntExtra(TOUR_GALLERY_TYPE, ERROR)){
		case EDIT_AND_CREATE:
			Log.i(TAG, "Starting edit and create tour activity");
			//handle accordingly
			break;
		case TAKE:
			Log.i(TAG, "Starting take tour activity");
			//handle accordingly
			break;
		default:
			//shouldn't happen ... 
			break;
		}
	}
}
