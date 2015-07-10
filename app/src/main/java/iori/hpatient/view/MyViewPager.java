package iori.hpatient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends LazyViewPager {
	private boolean setTouchMode = false;

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (setTouchMode)
			return super.onInterceptTouchEvent(ev);
		else
			return false;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if (setTouchMode)
//			return super.dispatchTouchEvent(ev);
//		else
//			return false;
//	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(setTouchMode)
		   return super.onTouchEvent(ev);
		else
			return false;
	}

}
