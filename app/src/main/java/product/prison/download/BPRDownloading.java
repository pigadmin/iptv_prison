package product.prison.download;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import product.prison.app.MyApp;
import product.prison.utils.Logs;
import product.prison.utils.SpUtils;

public class BPRDownloading {

    private final static String TAG = "断点下载";

    public final static int OK = 0;
    public final static int SD_ERROR = 1;
    public final static int NET_ERROR = 2;

    /**
     * 支持断点续传的方式进行下载
     *
     * @param url
     * @param fileDir
     * @param fileName
     */
    public static int zipdownload(Context context, String url,
                                  String fileDir, String fileName, DownloadProgressUpdater updater) {
        Logs.e("进行断点续传下载，地址：" + url + "|文件：" + fileDir + "/" + fileName);
        if (SDHandler.isSDAvaliable()) {
            File tempFile = new File(fileDir, fileName);
            // 没有可用网络连接
            if (!NetworkStatusHandler.isNetWorkAvaliable(context)) {
                Logs.e("没有可用网络连接，下载失败！");
                return NET_ERROR;
            } else {
                if (!url.startsWith("http"))
                    return 0;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = null;
                InputStream is = null;
                // 上次中断位置
                long breakPoint = 0l;
                if (tempFile.exists() && tempFile.isFile()) {
                    Logs.e("该文件已经下载过一部分，从上次中断位置开始下载：" + tempFile.toString());
                    breakPoint = tempFile.length();
                    Logs.e("断点位置：" + breakPoint + "b" + " | " + breakPoint
                            * 1.0f / 1024 + "k" + " | " + breakPoint * 1.0f
                            / 1024 / 1024 + "m");
                }
                httpGet.addHeader("RANGE", "bytes=" + breakPoint + "-");
                try {
                    response = HttpClientHolder.getInstance().execute(httpGet);
                } catch (Exception e) {
                    Logs.e("下载失败 " + url);
//                    e.printStackTrace();
                    SpUtils.putString(context, "url", "taskurl");
                    MyApp app = (MyApp) context.getApplicationContext();
                    app.setDownloadzip(false);
                    return 0;
                }
                HttpEntity entity = null;
                long total = 0;
                try {
                    if (response != null) {
                        // 返回码不是206
                        if (response.getStatusLine().getStatusCode() != 206) {
                            Logs.e("该文件已下载或返回码不是206，下载失败");
                            tempFile.delete();
                            return NET_ERROR;
                        }
                        entity = response.getEntity();
                        total = entity.getContentLength() + breakPoint;
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    is = entity.getContent();
                } catch (Exception e) {
                    e.printStackTrace();
                    return NET_ERROR;
                }

                // 没有断点续传文件,重新新建文件
                if (!tempFile.exists()) {
                    File parentFile = new File(fileDir);
                    if (!parentFile.exists()) {
                        if (!parentFile.mkdirs()) {
                            Logs.e("创建文件存放目录失败！");
                            return SD_ERROR;
                        }
                    }
                    tempFile = new File(parentFile, fileName);
                    if (!tempFile.exists()) {
                        try {
                            tempFile.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logs.e("下载失败，新建文件失败：\n" + e.getMessage());
                            return SD_ERROR;
                        }
                    }
                    Logs.e("没有可用断点续传文件，新建一个文件:" + tempFile.toString());
                }
                byte[] buffer = new byte[1024];
                RandomAccessFile oSavedFile = null;
                try {
                    oSavedFile = new RandomAccessFile(tempFile, "rw");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return SD_ERROR;
                }
                // 定位文件指针到断点位置
                try {
                    oSavedFile.seek(breakPoint);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return SD_ERROR;
                }
                try {
                    int oldPercent = 0;
                    for (int len = is.read(buffer); len != -1; len = is
                            .read(buffer)) {
                        oSavedFile.write(buffer, 0, len);
                        breakPoint += len;
                        if (updater != null) {
                            int percent = (int) (breakPoint * 1.0f * 100 / total);
                            // 保证只有在数值有变化之后才会执行update
                            if (oldPercent != percent) {
                                oldPercent = percent;
                                updater.downloadProgressUpdate(oldPercent);
                            }
                            if (percent % 5 == 0) {
                                context.sendBroadcast(new Intent("download")
                                        .putExtra("loading", percent + "%")
                                        .putExtra("percent", percent));
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logs.e("下载失败，从流中读取字节时候异常,保存下载文件位置，等待下次下载");
                    return NET_ERROR;
                } finally {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        oSavedFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Logs.e("sd卡不可用，下载失败！");
            return SD_ERROR;
        }
        return OK;
    }

    public static int resdownload(Context context, String url,
                                  String fileDir, String fileName, DownloadProgressUpdater updater) {
        Logs.e("进行断点续传下载，地址：" + url + "|文件：" + fileDir + "/" + fileName);
        if (SDHandler.isSDAvaliable()) {
            File tempFile = new File(fileDir, fileName);
            // 没有可用网络连接
            if (!NetworkStatusHandler.isNetWorkAvaliable(context)) {
                Logs.e("没有可用网络连接，下载失败！");
                return NET_ERROR;
            } else {

                if (!url.startsWith("http"))
                    return 0;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = null;
                InputStream is = null;
                // 上次中断位置
                long breakPoint = 0l;
                if (tempFile.exists() && tempFile.isFile()) {
                    Logs.e("该文件已经下载过一部分，从上次中断位置开始下载：" + tempFile.toString());
                    breakPoint = tempFile.length();
                    Logs.e("断点位置：" + breakPoint + "b" + " | " + breakPoint
                            * 1.0f / 1024 + "k" + " | " + breakPoint * 1.0f
                            / 1024 / 1024 + "m");
                }
                httpGet.addHeader("RANGE", "bytes=" + breakPoint + "-");
                try {
                    response = HttpClientHolder.getInstance().execute(httpGet);
                } catch (Exception e) {
                    Logs.e("下载失败 " + url);
//                    e.printStackTrace();
                    SpUtils.putString(context, "url", "taskurl");
                    MyApp app = (MyApp) context
                            .getApplicationContext();
                    app.setDownloadzip(false);
                    return 0;

                }
                HttpEntity entity = null;
                long total = 0;
                try {
                    if (response != null) {
                        // 返回码不是206
                        if (response.getStatusLine().getStatusCode() != 206) {
                            Logs.e("下载失败，该文件已下载或返回码不是206");
                            return NET_ERROR;
                        }
                        entity = response.getEntity();
                        total = entity.getContentLength() + breakPoint;
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    is = entity.getContent();
                } catch (Exception e) {
                    e.printStackTrace();
                    return NET_ERROR;
                }

                // 没有断点续传文件,重新新建文件
                if (!tempFile.exists()) {
                    File parentFile = new File(fileDir);
                    if (!parentFile.exists()) {
                        if (!parentFile.mkdirs()) {
                            Logs.e("创建文件存放目录失败！");
                            return SD_ERROR;
                        }
                    }
                    tempFile = new File(parentFile, fileName);
                    if (!tempFile.exists()) {
                        try {
                            tempFile.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logs.e("下载失败，新建文件失败：\n" + e.getMessage());
                            return SD_ERROR;
                        }
                    }
                    Logs.e("没有可用断点续传文件，新建一个文件:" + tempFile.toString());
                }
                byte[] buffer = new byte[1024];
                RandomAccessFile oSavedFile = null;
                try {
                    oSavedFile = new RandomAccessFile(tempFile, "rw");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return SD_ERROR;
                }
                // 定位文件指针到断点位置
                try {
                    oSavedFile.seek(breakPoint);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return SD_ERROR;
                }
                try {
                    int oldPercent = 0;
                    for (int len = is.read(buffer); len != -1; len = is
                            .read(buffer)) {
                        oSavedFile.write(buffer, 0, len);
                        breakPoint += len;
                        if (updater != null) {
                            int percent = (int) (breakPoint * 1.0f * 100 / total);
                            // 保证只有在数值有变化之后才会执行update
                            if (oldPercent != percent) {
                                oldPercent = percent;
                                updater.downloadProgressUpdate(oldPercent);
                            }
                            if (percent % 5 == 0) {
                                context.sendBroadcast(new Intent("download")
                                        .putExtra("loading", percent + "%")
                                        .putExtra("percent", percent));
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logs.e("下载失败，从流中读取字节时候异常,保存下载文件位置，等待下次下载");
                    return NET_ERROR;
                } finally {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        oSavedFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Logs.e("sd卡不可用，下载失败！");
            return SD_ERROR;
        }
        return OK;
    }

    /**
     * 用于通知下载进度
     *
     * @author glzlaohuai
     * @date 2013-7-4
     */
    public interface DownloadProgressUpdater {
        void downloadProgressUpdate(int percent);
    }

}
