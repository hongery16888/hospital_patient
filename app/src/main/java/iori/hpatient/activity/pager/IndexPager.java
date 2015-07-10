package iori.hpatient.activity.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import iori.hpatient.R;
import iori.hpatient.activity.base.BasePager;

/**
 * Created by Administrator on 2015/7/10.
 */
public class IndexPager extends BasePager {

    private View view;
    private Context context;

    private boolean isInit = false;

    public IndexPager(Context ct) {
        super(ct);
        context = ct;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.pager_index, null);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        if (!isInit) {
            initData();
            isInit = !isInit;
        }
    }

}
