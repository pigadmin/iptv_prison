package product.prison.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.util.DisplayMetrics;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import product.prison.model.InfoData;
import product.prison.model.LogoData;
import product.prison.model.Mings;
import product.prison.model.MsgData;
import product.prison.model.Nt;
import product.prison.model.wea.Wea;
import product.prison.service.MyService;
import product.prison.utils.Calendar;
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
    public static String ip = "s1.natfrp.org";
    public static String port = "42188";
    public static String sioport = "55577";
    //    public static String ip = "192.168.2.25";
//    public static String port = "8089";
    //    public static String ip = "192.168.2.5";
//    public static String port = "8080";
//    public static String sioport = "8000";
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
    public static DbManager db;

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

            initDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DbManager.DaoConfig daoConfig;

    private void initDb() {


        daoConfig = new DbManager.DaoConfig()
                .setDbName("iptv.db")//设置数据库名称
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File("sdcard/iptv")) // 数据库存储路径
                .setDbVersion(1)//设置数据库版本
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                })
        ;
        //db还有其他的一些构造方法，比如含有更新表版本的监听器的
        db = x.getDb(daoConfig);//获取数据库单例

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
    private Nt nt = new Nt();

    public Nt getNt() {
        return nt;
    }

    public void setNt(Nt nt) {
        this.nt = nt;
    }

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

    public Wea getWea() {
        return wea;
    }

    public void setWea(Wea wea) {
        this.wea = wea;
    }

    private Wea wea;

    public InfoData getInfoData() {
        return infoData;
    }

    public void setInfoData(InfoData infoData) {
        this.infoData = infoData;
    }

    private InfoData infoData = null;

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    private String updateurl = "";

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    private List<Calendar> calendars = new ArrayList<>();

    public boolean isDownloadzip() {
        return downloadzip;
    }

    public void setDownloadzip(boolean downloadzip) {
        this.downloadzip = downloadzip;
    }

    boolean downloadzip;


}
