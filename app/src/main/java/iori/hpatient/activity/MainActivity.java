package iori.hpatient.activity;

import android.widget.TextView;

import butterknife.InjectView;
import iori.hpatient.R;
import iori.hpatient.activity.base.BaseActivity;
import iori.hpatient.net.NetworkAPI;
import iori.hpatient.net.NetworkConnectListener;
import iori.hpatient.net.response.CheckVersionResponse;

public class MainActivity extends BaseActivity implements NetworkConnectListener {

	@InjectView(R.id.result)
	TextView result;

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
		NetworkAPI.getNetworkAPI().checkVersion("1", 0, null, this);
	}

	@Override
	public void onRequestSucceed(Object data, String requestAction, int requestMark) {
		result.setText(((CheckVersionResponse)data).toString());
	}

	@Override
	public void onRequestFailure(int error, String errorMsg, String requestAction, int requestMark) {
		result.setText("error message : " + errorMsg);
	}
}