package product.prison.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class InstallApk {

    private Context context;
    private String packageName;
    private String apkurl;
    private String savePath;

    private DownLoadFileThread downLoadFileThread;
    private Handler handler;
    private String filename;

    public InstallApk(Context context, String apkurl) {
        this.context = context;

        File dir = context.getDir("install", Context.MODE_PRIVATE
                | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        dir.mkdirs();
        this.packageName = context.getPackageName();
        this.apkurl = apkurl;
        this.savePath = dir.getAbsolutePath() + "/";
        handler = new Handler();
        // filename = Math.abs(this.apkurl.hashCode()) + ".apk";
        filename = "install.apk";
    }

    private ProgressDialog pBar;

    public void downloadAndInstall() {

        pBar = new ProgressDialog(context);
        pBar.setTitle("温馨提示");
        pBar.setMessage("正在下载应用，请稍后");
        // pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setCancelable(false);

        // pBar.setButton(ProgressDialog.BUTTON_NEGATIVE,
        // context.getString(R.string.cancel), new OnClickListener() {
        //
        // public void onClick(DialogInterface dialog, int which) {
        // pBar.cancel();
        // if (downLoadFileThread != null) {
        // downLoadFileThread.interrupt();
        // }
        // }
        // });
        // pBar.setButton(ProgressDialog.BUTTON_NEUTRAL,
        // context.getString(R.string.background), new OnClickListener() {
        // public void onClick(DialogInterface dialog, int which) {
        // pBar.cancel();
        // }
        // });

        pBar.setProgress(100);
        pBar.show();
        downLoadFileThread = new DownLoadFileThread(apkurl, savePath
                + this.filename, new DownloadCallback() {

            @Override
            public void onSuccess() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pBar.cancel();
                        getFilePermission(savePath + filename);
                        install(savePath + filename);
//                        clientInstall(savePath + filename);
//						adb.install(savePath + filename);
                    }
                });
            }

            @Override
            public void onPrecessing(int written, long total) {
                int c = (int) ((long) written * 100 / total);
                pBar.setProgress(c);
            }

            @Override
            public void onFail(Throwable e) {
                e.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "失败",
                                Toast.LENGTH_LONG).show();
                    }
                });
                pBar.cancel();
            }

            @Override
            public void onCancel() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "取消",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        downLoadFileThread.start();
    }

    // 静默安装
    private static boolean clientInstall(String apkPath) {
        System.out.println(apkPath);
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 " + apkPath);
            PrintWriter
                    .println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r " + apkPath);
            // PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();
            System.out.println("----" + returnResult(value));
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    private static boolean returnResult(int value) {
        // 代表成功
        if (value == 0) {
            return true;
        } else if (value == 1) { // 失败
            return false;
        } else { // 未知情况
            return false;
        }
    }

    private void install(String fullfilepath) {
        getFilePermission(fullfilepath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + fullfilepath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void getFilePermission(String file) {
        ShellExecute ex = new ShellExecute();
        String[] cmd = {"chmod", "607", file};
        try {
            ex.execute(cmd, "/");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class DownLoadFileThread extends Thread {

        private String url;
        // 文件保存路径
        private String fullFilename;
        private DownloadCallback callback;

        public DownLoadFileThread(String url, String fullFilename,
                                  DownloadCallback callback) {
            this.url = url;
            this.fullFilename = fullFilename;
            this.callback = callback;
        }

        public void run() {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(this.url);
            HttpResponse response;

            FileOutputStream outStream = null;
            try {
                response = client.execute(get);

                HttpEntity entity = response.getEntity();
                long length = entity.getContentLength();
                InputStream is = entity.getContent();
                File f = new File(fullFilename);
                f.getParentFile().mkdirs();
                if (is != null) {
                    outStream = new FileOutputStream(new File(fullFilename));

                    byte[] buf = new byte[1024];
                    int ch = -1;
                    int count = 0;
                    while ((ch = is.read(buf)) != -1) {
                        if (this.isInterrupted()) {
                            callback.onCancel();
                            return;
                        }
                        outStream.write(buf, 0, ch);
                        count += ch;
                        if (count != 0) {
                            callback.onPrecessing(count, length);
                        }
                    }
                }
                outStream.flush();
                callback.onSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFail(e);
            } finally {
                if (outStream != null) {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    interface DownloadCallback {

        public void onSuccess();

        public void onPrecessing(int written, long total);

        public void onFail(Throwable e);

        public void onCancel();
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

    public static void getUninstallAppIntent(Context context, String packageName) {
        try {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
