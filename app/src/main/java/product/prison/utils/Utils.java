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
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import product.prison.R;

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
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        String mac = "testcode";
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

    public void Download(final Context context, final String url, final boolean install) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mProgressDialog = new ProgressDialog(context);

            final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "iptv" + File.separator;
            final RequestParams params = new RequestParams(url);
            params.setAutoResume(true);//设置是否在下载是自动断点续传
            params.setAutoRename(false);//设置是否根据头信息自动命名文件
            final String filepath = path + url.substring(url.lastIndexOf("/") + 1);
            params.setSaveFilePath(filepath);
            params.setExecutor(new PriorityExecutor(2, true));//自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
            params.setCancelFast(true);//是否可以被立即停止.
            x.http().get(params, new Callback.ProgressCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    Logs.e("下载成功" + url + " " +filepath);
                    Toast.makeText(context, context.getString(R.string.downloadsuccess), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    if (install) {
                        install(context, filepath);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Logs.e("下载失败 " + url);
                    Toast.makeText(context, context.getString(R.string.downloadfail), Toast.LENGTH_SHORT).show();
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

    private void install(Context context, String s) {
        ShellExecute ex = new ShellExecute();
        String[] cmd = {"chmod", "607", s};
        try {
            ex.execute(cmd, "/");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + s),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private class ShellExecute {
        public String execute(String[] cmmand, String directory)
                throws IOException {
            String result = "";
            try {
                ProcessBuilder builder = new ProcessBuilder(cmmand);
                if (directory != null)
                    builder.directory(new File(directory));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream is = process.getInputStream();
                byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    result = result + new String(buffer);
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public static void fullvideo(View view) {
        // TODO Auto-generated method stub
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        view.setLayoutParams(layoutParams);
    }
}
