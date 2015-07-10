package iori.hpatient.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import iori.hpatient.R;

public class ShortcutUtil {

    private Context mContext;

    public ShortcutUtil(Context context) {
        mContext = context;
        //delShortcut();
        if (!checkShortcut())
            addShortcut();
    }

    private boolean checkShortcut() {
        boolean isInstallShortcut = false;
        final ContentResolver cr = mContext.getContentResolver();
        final String AUTHORITY = "com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[] { "title", "iconResource" }, "title=?",
                new String[] { mContext.getResources().getString(R.string.app_name) }, null);// XXX
        // 表示应用名称。
        // mContext.getResources().getString(R.string.app_name);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }

        return isInstallShortcut;
    }

    private void addShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); // 不允许重复创建

        // 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        // 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序


        try{
            ComponentName comp = new ComponentName(mContext.getPackageName(), "."
                    + ((Activity) mContext).getLocalClassName());
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN)
                    .setComponent(comp));

            // 快捷方式的图标
            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext,
                    R.drawable.icon);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            mContext.sendBroadcast(shortcut);
        }catch (Exception e) {
            // TODO: handle exception
        }


    }

    @SuppressWarnings("unused")
    private void delShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 快捷方式的名称 s
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
        // 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        // 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
        String appClass = mContext.getPackageName() + "."
                + ((Activity) mContext).getLocalClassName();
        ComponentName comp = new ComponentName(mContext.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN)
                .setComponent(comp));
        mContext.sendBroadcast(shortcut);
    }

}
