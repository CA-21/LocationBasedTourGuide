package CustomAnimations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAndTranslateAnimation extends Animation{
	private int mWidth;
	private int mStartWidth;
	private View mView;
	private float endX, endY,startX,startY;

	public ResizeAndTranslateAnimation(View view, int width,float x, float y){
		mView = view;
		mWidth = width;
		mStartWidth = view.getWidth();
		this.endX =x;
		this.endY=y;
		this.startX = view.getX();
		this.startY = view.getY();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t){
		int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
		float newX = startX +  ((endX - startX) *interpolatedTime),
				newY = startY +  ((endY - startY) *interpolatedTime);

		mView.getLayoutParams().width = newWidth;
		mView.setX(newX);
		mView.setY(newY);
		mView.requestLayout();
		super.applyTransformation(interpolatedTime, t);
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight){
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds(){
		return true;
	}
}