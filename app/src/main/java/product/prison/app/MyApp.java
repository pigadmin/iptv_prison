package product.prison.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import product.prison.broadcast.MyAction;
import product.prison.model.LogoData;
import product.prison.model.Mings;
import product.prison.model.MsgData;
import product.prison.model.TMenu;
import product.prison.service.MyService;
import product.prison.utils.Logs;
import product.prison.utils.SocketIO;
import product.prison.utils.SpUtils;
import product.prison.utils.Utils;


public class MyApp extends Application {
    public static final String PALY = "PALY";
    public static final String PAUSE = "PAUSE";
    public static final String STOP = "STOP";
    public static final String FORWARD = "FORWARD";
    public static final String REWIND = "REWIND";
    public static final String Cancle = "Cancle";

    private SocketIO socketIO;
    public static String head = "http://";
    //    public static String ip = "192.168.1.202";
//        public static String ip = "192.168.2.25";
//        public static String port = "8089";
    public static String ip = "192.168.2.3";
    public static String port = "8080";
    public static String sioport = "8000";
    public static String apiName = "/wisdom_iptv/remote/";
    public static String spaceName = "/tv";
    public static String apiurl = head + ip + port + apiName;
    public static String siourl = head + ip + sioport + spaceName;

    public static String mac = "testcode";
    //    public static int templateType = 1; //1酒店
//    public static int templateType = 2;//2医院
    public static int templateType = 3;//3学校
//    public static int templateType = 4;//4监狱
//    public static int templateType = 5;//5水疗
//    public static String[] info = {"酒店信息", "医院信息", "学校信息", "监狱信息", "水疗信息",};

    public static int screenWidth;

    private static AudioManager am;
    private static int maxvolume;
    private static int defaultvolume;

    @Override

    public void onCreate() {
        super.onCreate();
        try {
            x.Ext.init(this);
//        x.Ext.setDebug(true);

            boolean isFirst = SpUtils.getBoolean(this, "isFirst", false);
            if (!isFirst) {
                SpUtils.putBoolean(this, "isFirst", true);
//                SpUtils.putString(this, "mac", Utils.GetMac());
            }
//            mac = SpUtils.getString(this, "mac", mac);
            mac = Utils.GetMac();
            port = SpUtils.getString(this, "port", port);
            sioport = SpUtils.getString(this, "sioport", sioport);
            apiurl = head + SpUtils.getString(this, "ip", ip) + ":" + port + apiName;
            siourl = head + SpUtils.getString(this, "ip", ip) + ":" + sioport + spaceName;


            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            registerReceiver(receiver, filter);

            socketIO = new SocketIO(this);

            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            maxvolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            defaultvolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

            DisplayMetrics dm = getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;

            startService(new Intent(this, MyService.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置音量
    public static void setStreamVolume(int percent) {
        int volume = (int) Math.round((double) maxvolume * percent / 100);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                setServertime(getServertime() + 60 * 1000);
//                sendBroadcast(new Intent(MyAction.updatetime));
            } else if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {

            }
        }
    };

    public List<MsgData> getMsgData() {
        return msgData;
    }

    public void setMsgData(List<MsgData> msgData) {
        this.msgData = msgData;
    }

    private List<MsgData> msgData = new ArrayList<>();

    private LogoData logoData = null;

    public LogoData getLogoData() {
        return logoData;
    }

    public void setLogoData(LogoData logoData) {
        this.logoData = logoData;
    }


    public long getServertime() {
        return servertime;
    }

    public void setServertime(long servertime) {
        this.servertime = servertime;
    }

    private long servertime = 0;

    private Drawable bg = null;

    public Drawable getBg() {
        return bg;
    }

    public void setBg(Drawable bg) {
        this.bg = bg;
    }

    //    ----
    public boolean isWeek() {
        return week;
    }

    public void setWeek(boolean week) {
        this.week = week;
    }

    public boolean week;

    public boolean isMing() {
        return ming;
    }

    public void setMing(boolean ming) {
        this.ming = ming;
    }

    public boolean ming;

    public Mings getMings() {
        return mings;
    }

    public void setMings(Mings mings) {
        this.mings = mings;
    }

    private Mings mings = new Mings();
}
