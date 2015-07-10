package iori.hpatient.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import iori.hpatient.R;
import iori.hpatient.app.MyApp;
import iori.hpatient.modules.common.BaseManager;
import iori.hpatient.view.RequestProgressDialog;


public abstract class BaseActivity extends SysKeyInvalidActivity {

    @InjectView(R.id.left_icon)
    ImageView leftIcon;
    @InjectView(R.id.right_icon)
    ImageView rightIcon;
    @InjectView(R.id.mid_title)
    TextView title;
    @InjectView(R.id.right_text)
    TextView rightText;
    @InjectView(R.id.title_bar)
    View titleBar;
	
	protected Toast mToast;
    protected RequestProgressDialog mProgressDialog;

    public MyApp getApp() {
        return ((MyApp) getApplication());
    }

    @SuppressWarnings("unused")
    private void startAnmiActivity(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewResId());
        ButterKnife.inject(this);
        initView();
        initData();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract int setContentViewResId();

    protected abstract void initView();

    protected abstract void initData();

    public void setBackAction(){
        leftIcon.setImageResource(R.drawable.back);
        leftIcon.setOnClickListener(backListener);
    }

    public void setLeftIconAction(int resId, View.OnClickListener listener){
        leftIcon.setImageResource(resId);
        leftIcon.setOnClickListener(listener);
    }

    public void setTitleAction(String titleStr){
        title.setText(titleStr);
        title.setVisibility(View.VISIBLE);
    }

    public void setRightIconAction(int resId, View.OnClickListener listener){
        rightIcon.setImageResource(resId);
        rightIcon.setOnClickListener(listener);
        rightIcon.setVisibility(View.VISIBLE);
    }

    public void setRightText(String textStr, View.OnClickListener listener){
        rightIcon.setVisibility(View.GONE);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(textStr);
        rightText.setOnClickListener(listener);
    }

    public void setHideTitleBar(boolean flag){
        titleBar.setVisibility(flag? View.GONE: View.VISIBLE);
    }

    public void setTitleBarBackground(int color){
        titleBar.setBackgroundColor(color);
    }

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    /**
     * 网络检测并跳转
     * @param intent
     */
    public void startActivityByCheckNet(Intent intent) {
        boolean flag = getApp().isNetworkConnect();
        if(!flag)
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }
    
    
    /**
     * 网络检测,调用startActivityForResult启动activity
     * @param intent
     */
    public void startActivityByCheckNet(Intent intent,int requestCode) {
        boolean flag = getApp().isNetworkConnect();
        if (flag) {
        	startActivityForResult(intent,requestCode);
        } else {
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
    

    /**
     * 网络检测
     */
    public boolean checkNet() {
        boolean flag = getApp().isNetworkConnect();
        if (flag) {
           return flag;
        } else {
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            return flag;
        }
    }
    
    /*显示提示信息*/
    protected void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public RequestProgressDialog showProgressDialog() {
        if(mProgressDialog == null) {
            mProgressDialog = new RequestProgressDialog(this);
        }
        if(!mProgressDialog.isShowing()) {
            mProgressDialog.show();
            return mProgressDialog;
        }
        return mProgressDialog;
    }

    public void dismissProgressDialog() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
        }
    }

    public static void DEBUG(String tag,String log){
        BaseManager.DEBUG(tag, log);
    }

    public static void INFO(String tag,String log){
    	BaseManager.INFO(tag,log);
    }

    public static void WARN(String tag,String log){
    	BaseManager.WARN(tag,log);
    }
    public static void ERROR(String tag,String log){
        BaseManager.ERROR(tag,log);
    }
}
