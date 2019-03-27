package product.prison.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.media.AudioManager;
import android.text.TextUtils;

import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import product.prison.broadcast.MyAction;
import product.prison.model.TMenu;
import product.prison.service.MyService;
import product.prison.utils.Logs;
import product.prison.utils.SocketIO;
import product.prison.utils.SpUtils;
import product.prison.utils.Utils;


public class MyApp extends Application {


    private SocketIO socketIO;
    public static String head = "http://";
    public static String ip = "192.168.2.25";
    public static String port = "8089";
    public static String sioport = "8000";
    public static String apiName = "/wisdom_iptv/remote/";
    public static String spaceName = "/tv";
    public static String apiurl = head + ip + port + apiName;
    public static String siourl = head + ip + sioport + spaceName;

    public static String mac = "testcode";
    public static int templateType = 4; // templateType  1酒店 2医院 3学校 4监狱 5水疗


    @Override

    public void onCreate() {
        super.onCreate();
        try {
            x.Ext.init(this);
//        x.Ext.setDebug(true);

            boolean isFirst = SpUtils.getBoolean(this, "isFirst", false);
            if (!isFirst) {
                SpUtils.putBoolean(this, "isFirst", true);
                SpUtils.putString(this, "mac", Utils.GetMac());
            }
            apiurl = head + SpUtils.getString(this, "ip", ip) + ":" + port + apiName;
            siourl = head + SpUtils.getString(this, "ip", ip) + ":" + sioport + spaceName;

            mac = SpUtils.getString(this, "mac", mac);


            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            registerReceiver(receiver, filter);

            socketIO = new SocketIO(this);
            startService(new Intent(this, MyService.class));

            sio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sio() {
        socketIO.socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-error-事件" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        SocketIO.socket.on("tmenus", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    tMenu = Utils.jsonToObject(json, new TypeToken<List<TMenu>>() {
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                setServertime(getServertime() + 60 * 1000);
                sendBroadcast(new Intent(MyAction.updatetime));
            } else if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {

            }
        }
    };


    public List<TMenu> gettMenu() {
        return tMenu;
    }

    public void settMenu(List<TMenu> tMenu) {
        this.tMenu = tMenu;
    }

    private List<TMenu> tMenu = new ArrayList<>();

    public long getServertime() {
        return servertime;
    }

    public void setServertime(long servertime) {
        this.servertime = servertime;
    }

    private long servertime = 0;
}
