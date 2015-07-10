package iori.hpatient.util;

import android.content.Context;

import java.io.InputStream;
import java.lang.reflect.Field;

import iori.hpatient.R;

public class ResourceUtil {

    /**
     * 获取Drawable ID
     * @param name
     * @return
     */
    public static int getDrawable(String name){
        int res=-1;
        try {
            Field f=R.drawable.class.getDeclaredField(name);
            res=f.getInt(null);
        } catch (Exception e) {
        }
        return res;
    }

    /**
     * 获取Raw ID
     * @param name
     * @return
     */
    public static int getRaw(String name){
        int res=-1;
        try {
            Field f=R.raw.class.getDeclaredField(name);
            res=f.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取Raw内容
     * @param context
     * @param rawName
     * @return
     */
    public static String getRawContext(Context context,String rawName)
    {
        try {
            InputStream inputStream = context.getResources().openRawResource(
                    ResourceUtil.getRaw(rawName));
            byte[] reader = new byte[inputStream.available()];
            while (inputStream.read(reader) != -1) {
            }
            String fileContext = new String(reader);
            return fileContext;
        } catch (Exception e) {
            return "-1";
        }
    }

}
