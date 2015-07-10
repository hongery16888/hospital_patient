package iori.hpatient.activity.base;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BasePager {
	public Context ct;
	private LayoutInflater inflater;
	private View view;

	public BasePager(Context ct) {
		this.ct = ct;
		inflater = (LayoutInflater) ct
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
	}

	public View getRootView() {
		return view;
	}



	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();
	
	public void onDestroy(){
		
	};
	
	public void onPause(){
		
	};
	
	public void onResume(){
		
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

}
