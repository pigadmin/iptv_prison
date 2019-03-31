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

import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.model.MsgData;
import product.prison.model.Ncommand;
import product.prison.msg.IScrollState;
import product.prison.msg.MarqueeToast;
import product.prison.msg.TextSurfaceView;
import product.prison.utils.Logs;
import product.prison.utils.SpUtils;
import product.prison.utils.Utils;

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
        Start();
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
                e.printStackTrace();
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
//        String msgs = "{\"data\":" + msg.substring(1, msg.length() - 1)
//                + ",\"msg\":null,\"status\":\"success\"}";
//        System.out.println("接收到命令：" + msgs);
        try {
            final Ncommand cm = Utils.jsonToObject(msg, new TypeToken<Ncommand>() {
            });
            if (cm.getCommand() == 7) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        SpUtils.putString(MyService.this, "ip", cm.getServermessage().getServerip());
                        SpUtils.putString(MyService.this, "port", cm.getServermessage().getServerPort() + "");
                        SpUtils.putString(MyService.this, "sioport", cm.getServermessage().getSioPort() + "");

                        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


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
                toast.setHeight(40);


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
