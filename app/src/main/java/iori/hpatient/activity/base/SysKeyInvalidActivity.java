package iori.hpatient.activity.base;

import android.view.KeyEvent;

public class SysKeyInvalidActivity extends ExceptionActivity {

	
	//屏蔽系统自带的返回键
	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
	    switch (kCode) {
	    case KeyEvent.KEYCODE_DPAD_LEFT: {
	        return true;
	    }

	    case KeyEvent.KEYCODE_DPAD_UP: {
	        return true;
	    }

	    case KeyEvent.KEYCODE_DPAD_RIGHT: {
	        return true;
	    }

	    case KeyEvent.KEYCODE_DPAD_DOWN: {
	        return true;
	    }
	    case KeyEvent.KEYCODE_DPAD_CENTER: {
	        return true;
	    }
        //屏蔽返回键
//	    case KeyEvent.KEYCODE_BACK: {
//	        return true;
//	    }
	    case KeyEvent.KEYCODE_HOME:{
	    	return true;
	    }
	    }
	    return super.onKeyDown(kCode, kEvent);
	}
}
