package product.prison.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;

import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.model.MsgData;
import product.prison.msg.IScrollState;
import product.prison.msg.MarqueeToast;
import product.prison.msg.TextSurfaceView;
import product.prison.utils.Logs;

public class MyService extends Service implements Runnable, IScrollState {
    private List<MsgData> list = new ArrayList<>();
    private MarqueeToast toast;
    private TextSurfaceView Text;
    public int currentmsg;
    private static final long SHOW_MSG_PERIOD = 1L * 10L * 1000L;
    private MyApp app;

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
        registerReceiver(receiver, filter);
        showMessage();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyAction.updatetitle)) {
                showMessage();
            }
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
            if (list != null && !list.isEmpty()) {
                if (list.size() <= currentmsg)
                    currentmsg = 0;
                if (toast != null)
                    toast.hid();
                toast = new MarqueeToast(getApplicationContext());
                Text = new TextSurfaceView(getApplicationContext(), this);

//                Text.setBackgroundColor(Color.parseColor("#7E88B9"));
                toast.setHeight(38);


                Text.setFocusable(false);
                Text.setOrientation(1);

                Text.setContent(list.get(currentmsg).getContent());
//                Logs.e("跑马灯：" + list.get(currentmsg).getContent());
                toast.setView(Text);
                toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, MyApp.screenWidth, 0, 0);
                toast.show();
                currentmsg++;
            } else {
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
}
