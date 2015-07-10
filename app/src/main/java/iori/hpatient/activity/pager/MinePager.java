package iori.hpatient.activity.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import iori.hpatient.R;
import iori.hpatient.activity.base.BasePager;

/**
 * Created by Administrator on 2015/7/10.
 */
public class MinePager extends BasePager {

    private View view;
    private Context context;

    public MinePager(Context ct) {
        super(ct);
        context = ct;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.pager_mine, null);
        return view;
    }

    @Override
    public void initData() {

    }

}
