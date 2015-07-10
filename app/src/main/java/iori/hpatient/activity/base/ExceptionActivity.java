package iori.hpatient.activity.base;

import android.app.Activity;
import android.os.Bundle;

import iori.hpatient.util.SysExHandler;

public class ExceptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SysExHandler.getInstance().register(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SysExHandler.getInstance().unRegister(this);
    }
}
