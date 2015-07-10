package iori.hpatient.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import iori.hpatient.R;

public class RequestProgressDialog extends Dialog {

    private static final String TAG = "RequestProgressDialog";
    boolean isCancel = false;

    OnCancelClickListenr onCancelClickListenr;
    public void setOnCancelClickListenr(OnCancelClickListenr listener) {
        this.onCancelClickListenr = listener;
    }
    public interface OnCancelClickListenr {
        void onCancelClick();
    }

    public RequestProgressDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        View layout = LayoutInflater.from(context).inflate(R.layout.diag_progress_load, null);
        setContentView(layout);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void cancel() {
        if(onCancelClickListenr != null) {
            onCancelClickListenr.onCancelClick();
        }
        super.cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
