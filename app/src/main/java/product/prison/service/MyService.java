package product.prison.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;
import product.prison.BaseActivity;
import product.prison.R;
import product.prison.WelcomeActivity;
import product.prison.adapter.NewsGridAdapter;
import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.download.BPRDownloading;
import product.prison.model.Details;
import product.prison.model.MaterialVO;
import product.prison.model.MsgData;
import product.prison.model.News;
import product.prison.model.Nt;
import product.prison.model.ProgramContentVO;
import product.prison.model.ProgramListVO;
import product.prison.model.ProgramVO;
import product.prison.model.Servermessage;
import product.prison.model.TGson;
import product.prison.msg.IScrollState;
import product.prison.msg.MarqueeToast;
import product.prison.msg.TextSurfaceView;
import product.prison.model.Calendar;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.LtoDate;
import product.prison.utils.SocketIO;
import product.prison.utils.SpUtils;
import product.prison.utils.Utils;
import product.prison.utils.ZipUtil;
import product.prison.view.ad.MsginsActivity;
import product.prison.view.ad.RsType;
import product.prison.view.ad.WeekActivity;
import product.prison.view.msg.NoticeActivity;
import product.prison.view.news.NewsActivity;

public class MyService extends Service implements Runnable, IScrollState {
    private List<MsgData> list = new ArrayList<>();
    private MarqueeToast toast;
    private TextSurfaceView Text;
    public int currentmsg;
    private static final long SHOW_MSG_PERIOD = 1L * 10L * 1000L;
    private MyApp app;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        if (dialog == null)
                            return;
                        if (dialog.isShowing())
                            dialog.dismiss();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (MyApp) getApplication();
//        Logs.e(getClass().getSimpleName() + "onCreate");

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyAction.updatetitle);
        filter.addAction(MyAction.NT);
        filter.addAction(MyAction.Calendar);
        filter.addAction(MyAction.Mings);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(receiver, filter);
        showMessage();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Start();
            }
        }).start();

        call();
    }

    private void call() {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.float_call, null);
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.x = 10;
            params.y = 36;
            params.height = 71;
            params.width = 71;
            params.gravity = Gravity.RIGHT;
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                    | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.format = PixelFormat.TRANSLUCENT;
            windowManager.addView(view, params);
            ImageView call = view.findViewById(R.id.call);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//去紧急呼叫
                    addNotice();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNotice() {
        RequestParams params = new RequestParams(MyApp.apiurl + "addNotice");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("notifyNews", "紧急呼叫");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    final TGson<String> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<String>>() {
                            }.getType());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (json.getCode().equals("200")) {
                                Toast.makeText(getApplicationContext(), "呼叫成功!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private static final int PORT = 9999;
    private ServerSocket server = null;
    private Socket socket = null;


    public void Start() {
        try {
            if (server != null) {
                server = null;
            }
            server = new ServerSocket(PORT);
            while (true) {
                socket = server.accept();
                new SocketThread(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SocketThread extends Thread {

        private Socket socket;

        public SocketThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            String msg;
            DataInputStream in = null;
            try {
                in = new DataInputStream(socket.getInputStream());

                while (true) {
                    if ((msg = in.readUTF()) != null) {
                        System.out.println("@@@" + msg);
                        event(msg);
                    }
                }

            } catch (Exception e) {
//                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String cmd = "";

    private void event(String msg) {
        // TODO Auto-generated method stub

        try {
            final Servermessage servermessage = Utils.jsonToObject(msg, new TypeToken<Servermessage>() {
            });
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "接受到修改IP指令", Toast.LENGTH_SHORT).show();

                    SpUtils.putString(getApplicationContext(), "ip", servermessage.getServerip());
                    SpUtils.putString(getApplicationContext(), "port", servermessage.getServerPort() + "");
                    SpUtils.putString(getApplicationContext(), "sioport", servermessage.getSioPort() + "");


                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void execLinuxCommand() {
        String cmd = "sleep 120;am start -n product.prison/product.prison.WelcomeActivity";

        Runtime runtime = Runtime.getRuntime();
        try {
            Process localProcess = runtime.exec("su");
            OutputStream localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes(cmd);
            localDataOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String head = new SimpleDateFormat("yyyy-MM-dd ").format(new Date(System
            .currentTimeMillis()));
    private Calendar calendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyAction.Calendar)) {
                calendar();
            }
            if (intent.getAction().equals(MyAction.Mings)) {
                //任务计划
//                long cur = System.currentTimeMillis();
                if (app.getMings() != null) {
                    Logs.e("计划任务 " + Utils.gson.toJson(app.getMings()));
//                    long start = app.getMings().getBeginTime();
                    long end = app.getMings().getEndTime();

//                    Logs.e("计划任务 " + LtoDate.yMdHmE(start) + "====" + LtoDate.yMdHmE(end));
                    Logs.e("计划任务结束时间====" + LtoDate.yMdHmE(end));
//                    if (cur > start && cur < end && !app.isMing()) {
                    SocketIO.uploadLog("开始计划播放-" + app.getMings().getName());
                    startActivity(new Intent(MyService.this, MsginsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    app.setMing(true);
//                    }

                }
            }
            if (intent.getAction().equals(MyAction.updatetitle)) {
                SocketIO.uploadLog("开始播放滚动字幕");
                showMessage();
            }
            if (intent.getAction().equals(MyAction.NT)) {
                showAlterDialog();
            }
            if (intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                System.out.println("update time");
                try {
                    long cur = System.currentTimeMillis();

                    if (calendar != null) {
                        //一周安排
                        Logs.e("一周安排 " + Utils.gson.toJson(calendar));
                        long begin = (long) sdf.parse(head
                                + calendar.getTime_begin()).getTime();

                        long end = (long) sdf.parse(head
                                + calendar.getTime_end()).getTime();

                        Logs.e("一周安排 " + LtoDate.yMdHmE(begin) + "====" + LtoDate.yMdHmE(end));

                        if (cur > begin && cur < end && !app.isWeek()) {

                            app.setWeek(true);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("key", calendar);
                            Intent week = new Intent(getApplicationContext(),
                                    WeekActivity.class);
                            week.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            week.putExtras(bundle);
                            startActivity(week);
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if (intent.getAction().equals("msg")) {
//                if (toast != null) {
//                    toast.hid();
//                    toast=null;
//                }
//                currentmsg = 0;
//                messagerun = false;
//                getSubTitle();
//            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
//        Logs.e(getClass().getSimpleName() + "onDestroy");

    }


    private void showMessage() {
        try {
            list = app.getMsgData();
            for (MsgData msgdata : list) {
                Logs.e((app.getServertime()-msgdata.getEndtime())+"");
                if (app.getServertime() > msgdata.getEndtime()) {
                    Logs.e("结束播放滚动字幕-"+msgdata.getTitle());
                    SocketIO.uploadLog("结束播放滚动字幕-"+msgdata.getTitle());
                    list.remove(msgdata);
                    app.getMsgData().remove(msgdata);
                }
            }

            if (list != null && !list.isEmpty()) {
                if (list.size() <= currentmsg)
                    currentmsg = 0;
                if (toast != null)
                    toast.hid();
                MsgData msgData = list.get(currentmsg);
                toast = new MarqueeToast(getApplicationContext());
                Text = new TextSurfaceView(getApplicationContext(), this);
                toast.setHeight(msgData.getFontSize() + 15);

                Text.setSpeed(101 - msgData.getRollSpeed());



                Text.setFocusable(false);
                Text.setOrientation(1);

//                Utils.gson.toJson(list.get(currentmsg));


//                Logs.e("跑马灯：" + list.get(currentmsg).getContent());
                Text.setFontSize(msgData.getFontSize());
                if (!msgData.getFontColor().equals("")) {
                    Text.setFontColor(msgData.getFontColor());
                }

                Text.setContent(msgData.getContent());
                toast.setView(Text);
                toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, MyApp.screenWidth, 0, 0);
                toast.show();
                currentmsg++;
            } else {
                Logs.e("结束播放所有滚动字幕");
//                SocketIO.uploadLog("结束播放所有滚动字幕");
                if (toast != null) {
                    toast.hid();
                    toast = null;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Logs.e("跑马灯异常：" + e.toString());
            e.printStackTrace();
        }

    }

    public void start() {
        // TODO Auto-generated method stub
    }


    @Override
    public void stop() {
        // TODO Auto-generated method stub
        try {
            // Text.setLoop(false);
            Looper.prepare();
            new Handler().postDelayed(this, SHOW_MSG_PERIOD);
            Looper.loop();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        showMessage();
    }

    private void calendar() {
        try {
            if (app.getCalendars().isEmpty())
                return;
            calendar = app.getCalendars().get(0);


            if (app.isDownloadzip()) {
                return;
            }

            final String url = calendar.getProgramURL();
            final String filename = url.substring(url.lastIndexOf('/') + 1);
            String old = getSharedPreferences("task", Context.MODE_PRIVATE).getString("url", "");
            System.out.println("已下载:" + old);
            System.out.println("最新的:" + url);
            if (!url.equals(old)) {
                System.out.println("开始下载：------------------" + calendar.getName() + "\n" + url);
                app.setWeek(true);
                ClearFiles();
                app.setDownloadzip(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BPRDownloading.zipdownload(getApplicationContext(), url, ZipUtil.zip_file,
                                filename, new BPRDownloading.DownloadProgressUpdater() {
                                    public void downloadProgressUpdate(int percent) {
                                        if (percent == 100) {
                                            app.setDownloadzip(false);
                                            SharedPreferences preferences = getSharedPreferences("task", Context.MODE_PRIVATE);
                                            preferences.edit().putString("url", calendar.getProgramURL())
                                                    .commit();
                                            System.out.println("下载zip文件：" + calendar.getName() + "成功");
                                            ZipUtil.unZipFiles(filename);
                                        }
                                    }


                                });

                        DownRes();
                        app.setWeek(false);
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DownRes() {
        try {
            String main = product.prison.download.ZipUtil
                    .readMainFile(product.prison.download.ZipUtil.gainNewestFileName());
            System.out.println(product.prison.download.ZipUtil.gainNewestFileName());
            ProgramListVO programlistVO = Utils.gson.fromJson(main, ProgramListVO.class);

            if (programlistVO == null)
                return;
            List<ProgramVO> programVOs = programlistVO.getPrograms();

            if (programlistVO != null) {
                for (int i = 0; i < programVOs.size(); i++) {
                    if (programVOs.get(i).getBackground() != null) {
                        String url = programVOs.get(i).getBackground()
                                .getPath();
                        final String name = url.substring(url.lastIndexOf('/') + 1);
                        final int cur = i;
                        BPRDownloading.resdownload(getApplicationContext(), url,
                                product.prison.download.ZipUtil.res_file, name, new BPRDownloading.DownloadProgressUpdater() {
                                    public void downloadProgressUpdate(int percent) {
                                        System.out.println("正在下载第" + (cur + 1) + "个" + name + "背景图片" + percent + "%");
                                    }
                                });
                    }

                    List<ProgramContentVO> contentVO = programVOs.get(i)
                            .getContents();

                    for (int j = 0; j < contentVO.size(); j++) {
                        List<MaterialVO> materialVOs = contentVO.get(j)
                                .getMaterials();
                        int ty = 0;
                        ty = contentVO.get(j).getClassify();

                        //                   if (ty != MaterialType.VIDEO) {
                        for (int k = 0; k < materialVOs.size(); k++) {
                            String url = materialVOs.get(k).getPath();
                            final String name = url.substring(url.lastIndexOf('/') + 1);
                            final int cur = k;
                            BPRDownloading.resdownload(getApplicationContext(), url,
                                    product.prison.download.ZipUtil.res_file, name, new BPRDownloading.DownloadProgressUpdater() {
                                        public void downloadProgressUpdate(int percent) {
                                            System.out.println("正在下载第" + (cur + 1) + "个" + name + "资源" + percent + "%");
                                        }
                                    });
                            //                 }
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog dialog = null;
    private int closetime = 10;

    private void showAlterDialog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    handler.removeMessages(0);
                    dialog = null;
                }
            }
            final Nt nt = app.getNt();
            Logs.e("当前activity" + BaseActivity.activity.getClass().getSimpleName());
            final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(BaseActivity.activity);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
            alterDiaglog.setIcon(R.mipmap.ic_launcher);//图标
            alterDiaglog.setTitle("通知");//文字
            if (nt.getContent().startsWith("h")) {
                alterDiaglog.setView(view);
                String temp = nt.getContent().substring(nt.getContent().lastIndexOf("."));
                switch (RsType.type.get(temp.toLowerCase())) {
                    case 1:
                        ImageView welcome_image = view.findViewById(R.id.welcome_image);
                        welcome_image.setVisibility(View.VISIBLE);
//                        ImageUtils.display(welcome_image, nt.getContent());
                        Picasso.with(getApplicationContext()).load(nt.getContent()).into(welcome_image);
                        handler.sendEmptyMessageDelayed(0, closetime * 1000);
                        break;
                    case 2:
                    case 3:

                        VideoView welcome_video = view.findViewById(R.id.welcome_video);
                        welcome_video.setVisibility(View.VISIBLE);
                        welcome_video.setVideoPath(nt.getContent());
                        welcome_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                                sendBroadcast(new Intent(MyAction.PAUSEBGMUSIC));
                            }
                        });
                        welcome_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                return true;
                            }
                        });
                        welcome_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                dialog.dismiss();
                                sendBroadcast(new Intent(MyAction.GOONBGMUSIC));
                            }
                        });
                        break;
                }
            } else {
                alterDiaglog.setMessage(nt.getContent());
                handler.sendEmptyMessageDelayed(0, closetime * 1000);
            }

            alterDiaglog.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), NoticeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });
            alterDiaglog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            //显示
            dialog = alterDiaglog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    sendBroadcast(new Intent(MyAction.GOONBGMUSIC));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void ClearFiles() {
        try {

            File zip = new File(ZipUtil.zip_file);
            File main = new File(ZipUtil.main_file);
            File res = new File(ZipUtil.res_file);

            for (File zipfiles : zip.listFiles()) {
                zipfiles.delete();
                System.out.println("删除" + zipfiles.getName());
            }
            for (File programfiles : main.listFiles()) {
                if (programfiles.isDirectory()) {
                    DirectoryDelete(programfiles);
                }
            }
            for (File resfile : res.listFiles()) {
                resfile.delete();
                System.out.println("删除" + resfile.getName());

            }


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void DirectoryDelete(File file) {
        // TODO Auto-generated method stub
        for (File files : file.listFiles()) {
            files.delete();
            System.out.println("删除" + files.getName());
        }
        file.delete();
    }

}
