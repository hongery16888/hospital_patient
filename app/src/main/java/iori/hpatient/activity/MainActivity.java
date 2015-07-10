package iori.hpatient.activity;

import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import iori.hpatient.R;
import iori.hpatient.activity.base.BaseActivity;
import iori.hpatient.activity.base.BasePager;
import iori.hpatient.activity.pager.IndexPager;
import iori.hpatient.activity.pager.MinePager;
import iori.hpatient.adapter.ViewPagerAdapter;
import iori.hpatient.net.NetworkAPI;
import iori.hpatient.net.NetworkConnectListener;
import iori.hpatient.net.response.CheckVersionResponse;
import iori.hpatient.view.MyViewPager;
import iori.hpatient.view.LazyViewPager.OnPageChangeListener;

public class MainActivity extends BaseActivity implements NetworkConnectListener {

	@InjectView(R.id.viewpager)
	MyViewPager viewPager;
	@InjectView(R.id.main_radio)
	RadioGroup main_radio;
	@InjectView(R.id.result)
	TextView result;

	private List<BasePager> pages = new ArrayList<BasePager>();
	private int currentItem = R.id.index_main;
	private int oldPosition = 2;
	private ViewPagerAdapter viewPagerAdapter;

	@Override
	protected int setContentViewResId() {
		return R.layout.main;
	}

	@Override
	protected void initView() {
		setBackAction();
	}

	@Override
	protected void initData() {
		pages.clear();
		pages.add(new IndexPager(this));
		pages.add(new MinePager(this));
		pages.add(new IndexPager(this));
		pages.add(new MinePager(this));
		viewPagerAdapter = new ViewPagerAdapter(pages);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(pageChangeListener);
		main_radio.setOnCheckedChangeListener(checkedChangeListener);
		main_radio.check(currentItem);
		pages.get(oldPosition).onResume();
		NetworkAPI.getNetworkAPI().checkVersion("1", 0, null, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		pages.get(oldPosition).onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		pages.get(oldPosition).onPause();
	}

	@Override
	protected void onDestroy() {
		if (pages != null) {
			for (BasePager pager : pages) {
				pager.onDestroy();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onRequestSucceed(Object data, String requestAction, int requestMark) {
		result.setText(((CheckVersionResponse)data).toString());
	}

	@Override
	public void onRequestFailure(int error, String errorMsg, String requestAction, int requestMark) {
		result.setText("error message : " + errorMsg);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			BasePager pager = pages.get(position);
			pager.onResume();
			//pager.initData();
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
								   int positionOffsetPixels) {

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
				case R.id.index_main:
					if (oldPosition != 0) {
						pages.get(oldPosition).onPause();
						oldPosition = 0;
					}
					viewPager.setCurrentItem(0, false);
					break;
				case R.id.shopping_main:
					if (oldPosition != 1) {
						pages.get(oldPosition).onPause();
						oldPosition = 1;
					}
					viewPager.setCurrentItem(1, false);
					break;
				case R.id.activity_main:
					if (oldPosition != 2) {
						pages.get(oldPosition).onPause();
						oldPosition = 2;
					}
					viewPager.setCurrentItem(2, false);
					break;
				case R.id.personal_main:
					if (oldPosition != 3) {
						pages.get(oldPosition).onPause();
						oldPosition = 3;
					}
					viewPager.setCurrentItem(3, false);
					break;
			}
			currentItem = checkedId;
		}
	};
}