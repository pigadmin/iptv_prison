package product.prison.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static Gson gson = new Gson();

    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String formatTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
        return format.format(new Date());
    }

    public static String formatMyTime(String type, long time) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        return format.format(time);
    }


    public static <T> T jsonToObject(String json, TypeToken<T> typeToken) {
        //  new TypeToken<AJson<Object>>() {}.getType()   对象参数
        // new TypeToken<AJson<List<Object>>>() {}.getType() 集合参数
        try {
            if (TextUtils.isEmpty(json) || json.equals("null"))
                return null;
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getVersion(Context context) {
        String version = "";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int getCurrentVolume(Context context) {
        int mCurrentVolume = 0;
        try {
            //得到音量
            AudioManager mAm = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //最大音量
            int mMaxVolume = mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            //当前音量
            mCurrentVolume = mAm.getStreamVolume(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCurrentVolume;
    }

    public static boolean isInstall(Context context, String packageName) {
        try {
            PackageInfo pin = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            if (pin != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean startApk(Context context, String pkgNmae, String className) {
        try {
            if (isInstall(context, pkgNmae)) {
                if (!className.trim().equals("") || className == null) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(pkgNmae, className));
                    context.startActivity(intent);
                } else {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgNmae);
                    context.startActivity(intent);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public static String GetMac() {
        String mac = "test";
        try {
            Process pro = Runtime.getRuntime().exec(
                    "cat /sys/class/net/eth0/address");
            InputStreamReader inReader = new InputStreamReader(
                    pro.getInputStream());
            BufferedReader bReader = new BufferedReader(inReader);
            String line = null;
            while ((line = bReader.readLine()) != null) {
                mac = line.trim();
            }
            return mac.replaceAll(":", "").toLowerCase();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return mac;
    }

    private static ProgressDialog mProgressDialog;

    private static void Download(Activity activity, String url) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mProgressDialog = new ProgressDialog(activity);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            // mDownloadUrl为JSON从服务器端解析出来的下载地址
            RequestParams requestParams = new RequestParams(url);
            // 为RequestParams设置文件下载后的保存路径
            requestParams.setSaveFilePath(path);
            // 下载完成后自动为文件命名
            requestParams.setAutoRename(true);
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    Logs.e("下载成功");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Logs.e("下载失败");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Logs.e("取消下载");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFinished() {
                    Logs.e("结束下载");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onWaiting() {
                    // 网络请求开始的时候调用
                    Logs.e("等待下载");
                }

                @Override
                public void onStarted() {
                    // 下载的时候不断回调的方法
                    Logs.e("开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    // 当前的下载进度和文件总大小
                    Logs.e("正在下载中......");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setMessage("正在下载中......");
                    mProgressDialog.show();
                    mProgressDialog.setMax((int) total);
                    mProgressDialog.setProgress((int) current);
                }
            });
        }
    }

}
