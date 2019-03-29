package product.prison.utils;

import android.util.Log;

public class Logs {

    private final static String APP_TAG = "iptv";

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if (st.getClassName().equals(Logs.class.getName())) {
                    continue;
                }
                return "    [ 线程:" + Thread.currentThread().getName() + ", 类名:" + st.getClassName() + "方法名称：" + st.getMethodName()
                        + "位置：(" + st.getFileName() + ":" + st.getLineNumber() + ")" + " ]";
            }
        }
        return null;
    }


    public static void v(String msg) {
        Log.v(Utils.formatTime(), getMsgFormat(msg));
    }


    public static void d(String msg) {
        Log.d(Utils.formatTime(), getMsgFormat(msg));
    }


    public static void i(String msg) {
        Log.i(Utils.formatTime(), getMsgFormat(msg));
    }


    public static void w(String msg) {
        Log.w(Utils.formatTime(), getMsgFormat(msg));
    }


    public static void e(String msg) {
        Log.e(Utils.formatTime(), getMsgFormat(msg));
    }


    /**
     * 输出格式定义
     */
    private static String getMsgFormat(String msg) {
        return msg + getFunctionName();
    }
}

