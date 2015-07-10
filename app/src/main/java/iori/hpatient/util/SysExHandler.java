package iori.hpatient.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import iori.hpatient.app.MyApp;
import iori.hpatient.modules.common.BaseManager;

public class SysExHandler implements UncaughtExceptionHandler {

    private static SysExHandler mInstanceSingleton;

    private UncaughtExceptionHandler previousHandler;

    private List<Context> contexts = new ArrayList<Context>();

    private ProgressDialog dialog;

    /**
     * Constructor of SYSExHandler 私有构造方法，只能被自己构造，不能被
     * 其它类构造。方法中初始化previousHandler为系统默认未捕捉异 常处理类。
     */
    private SysExHandler() {
        super();
        this.previousHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * Description : 获取一个SYSExHandler实例
     *
     * @return
     *
     */
    public synchronized static SysExHandler getInstance() {
        if (mInstanceSingleton == null) {
            mInstanceSingleton = new SysExHandler();
        }
        return mInstanceSingleton;
    }

    /**
     * Description : 为Activity、Service等注册，在Activity、Service等的
     * onCreate()方法中注册Activity、Service出现没有捕捉的异常将由本类 (SYSExHandler)处理。
     *
     * @param context
     *
     */
    public void register(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(getInstance());
        this.contexts.add(context);
        BaseManager.INFO("context.size : ", "-----activity count ----" + contexts.size());
    }

    /**
     * Description : 为Activity、Service等注销，将从contexts中删除Activity、
     * Service对象，此方法只能在Activity、Service 的onDestroy()方 法中调用。
     *
     * @param context
     *
     */
    public void unRegister(Context context) {
        this.contexts.remove(context);
    }

    /**
     * Description: 打印处理未捕获的系统异常信息并处理。
     *
     * @see UncaughtExceptionHandler#uncaughtException(
     *      Thread, Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handleException(ex) && previousHandler != null){
            previousHandler.uncaughtException(thread, ex);
        }

        //提示5秒，供用户查看
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //清除提示信息，并关闭程序
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        foreClose();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    public String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        return stringWriter.toString();
    }

    private void saveException(String exception){
        File path = new File(MyApp.APP_ROOT+"/log");
        if(!path.exists())
            path.mkdirs();
        try {
            FileOutputStream os = new FileOutputStream(new File(path.getAbsolutePath(), System.currentTimeMillis()+".txt"));
            os.write(exception.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description : 处理异常
     *
     * @param ex
     *
     */

    private boolean handleException(Throwable ex) {
        ex.printStackTrace();

        saveException(getTrace(ex));
        if (previousHandler == null)
            return false;

        new Thread() {

            @Override
            public void run() {
                try {
                    Looper.prepare();
                    //产生exception的context
                    Context context = contexts.get(contexts.size() - 1);
                    if(context instanceof Activity || context instanceof Service){

                        dialog = new ProgressDialog(context);
                        dialog.setMessage("MTS遇到不可预知的错误，正在结束...");
                        dialog.setTitle("请等待");
                        dialog.show();
                        //去掉提示栏信息，避免用户误点
                        NotificationManager nfm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        nfm.cancelAll();
                    } else {
                        //强制关闭程序
                        foreClose();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(10);
                    }
                    Looper.loop();
                } catch (Exception e) {
                    //交由系统处理该异常
                    e.printStackTrace();
                    previousHandler.uncaughtException(Thread.currentThread(), e);
                }
            }
        }.start();

        return true;
    }

    /**
     *
     * Description : 强制退出程序方法
     *
     *
     */
    public void foreClose() {
        try {
            //分别关闭stack中的activity和service
            for (int i = 0; i < contexts.size(); i++) {

                Context context = contexts.get(i);

                if(context == null)
                    continue;

                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
                if (context instanceof Service) {
                    ((Service) context).stopSelf();
                }
            }
            //退出程序

//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(10);
        } catch (Exception e) {
            previousHandler.uncaughtException(Thread.currentThread(), e);
        }
    }
}
