package product.prison.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.reflect.TypeToken;
import com.mstar.android.tv.TvPictureManager;

import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import product.prison.R;
import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.model.CalendarData;
import product.prison.model.Command;
import product.prison.model.Mings;
import product.prison.model.MsgData;
import product.prison.model.Nt;
import product.prison.model.RegistVo;
import product.prison.model.TMenu;
import product.prison.model.UploadLogVo;
import product.prison.model.wea.NewWea;
import product.prison.model.wea.Wea;
import product.prison.view.ad.NowinsActivity;
import product.prison.view.msg.TimeSort;


public class SocketIO {
    public static Socket socket;
    private Context context;
    private MyApp app;
    private final int openw = 0;
    private final int closew = 1;
    private boolean isreg = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case openw:
                    try {
//                        if (app.isPower())
//                            return;
                        uploadLog("执行开机");
//                        app.setPower(true);
                        Toast.makeText(context, "开机", Toast.LENGTH_SHORT).show();
//                        TvPictureManager.getInstance().enableBacklight();
                        adb.InputEvent(KeyEvent.KEYCODE_POWER);
                        socket.emit("power", 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logs.e("执行adb开机");
//                        app.setPower(true);
                        adb.InputEvent(KeyEvent.KEYCODE_POWER);
                    }
                    break;
                case closew:
                    try {
//                        if (!app.isPower())
//                            return;
                        uploadLog("执行关机");
//                        app.setPower(false);
                        Toast.makeText(context, "关机", Toast.LENGTH_SHORT).show();
//                        TvPictureManager.getInstance().disableBacklight();
                        adb.InputEvent(KeyEvent.KEYCODE_POWER);
                        socket.emit("power", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logs.e("执行adb关机");
//                        app.setPower(false);
                        adb.InputEvent(KeyEvent.KEYCODE_POWER);
                    }
                    break;
            }
        }
    };

    public SocketIO(Context context) {


        try {
            this.context = context;
            app = (MyApp) context.getApplicationContext();
            socket = IO.socket(MyApp.siourl);
            socket.on(Socket.EVENT_CONNECT, EVENT_CONNECT);
            socket.on(Socket.EVENT_CONNECT_ERROR, EVENT_CONNECT_ERROR);
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, EVENT_CONNECT_TIMEOUT);
            socket.on(Socket.EVENT_DISCONNECT, EVENT_DISCONNECT);
            socket.on(Socket.EVENT_ERROR, EVENT_ERROR);
            socket.on(Socket.EVENT_MESSAGE, EVENT_MESSAGE);
            socket.on(Socket.EVENT_RECONNECT, EVENT_RECONNECT);
            socket.on(Socket.EVENT_RECONNECT_ATTEMPT, EVENT_RECONNECT_ATTEMPT);
            socket.on(Socket.EVENT_RECONNECT_ERROR, EVENT_RECONNECT_ERROR);
            socket.on(Socket.EVENT_RECONNECT_FAILED, EVENT_RECONNECT_FAILED);
            socket.on(Socket.EVENT_RECONNECTING, EVENT_RECONNECTING);
            //-----------------------------------------------------------------
            sio();
            socket.connect();
            Logs.e("开始连接" + MyApp.siourl);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sio() {
        socket.on("error", new Emitter.Listener() {
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
//        socket.on("tmenus", new Emitter.Listener() {//主菜单事件：tmenus
//            @Override
//            public void call(Object... args) {
//                try {
//                    String json = args[0].toString();
//                    Logs.e("sio-tmenus-事件" + json);
//                    List<TMenu> list = Utils.jsonToObject(json, new TypeToken<List<TMenu>>() {
//                    });
//                    if (list.isEmpty())
//                        return;
//                    app.settMenu(list);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        socket.on("upgrade", new Emitter.Listener() {
            @Override
            public void call(Object... args) {//升级：upgrade
                try {
                    String json = args[0].toString();
                    Logs.e("sio-upgrade-事件" + json);

                    app.setUpdateurl(json);
                    context.sendBroadcast(new Intent().setAction(MyAction.upgrade).putExtra("key", json));

                    uploadLog("接受升级消息推送-" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("weather", new Emitter.Listener() {
            @Override
            public void call(Object... args) {//   天气更新事件:  weather
                try {
                    String json = args[0].toString();
                    Logs.e("sio-weather-事件" + json);
                    NewWea newWea = Utils.gson.fromJson(json, NewWea.class);
                    Wea wea = Utils.gson.fromJson(newWea.getInfo(), Wea.class);
                    app.setWea(wea);
//                    Logs.e(Utils.gson.toJson(wea.getData().get(0).getAlarm()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Logs.e(e.toString());
                }
            }
        });
        socket.on("notice", new Emitter.Listener() {
            @Override
            public void call(Object... args) {//   通知更新事件:  notice
                try {
                    String json = args[0].toString();
                    Logs.e("sio-notice-事件" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("title", new Emitter.Listener() {//字幕更新事件:  title
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-title-事件" + json);
                    List<MsgData> list = Utils.jsonToObject(json, new TypeToken<List<MsgData>>() {
                    });
//                    if (list.isEmpty())
//                        return;
                    app.setMsgData(list);
                    context.sendBroadcast(new Intent().setAction(MyAction.updatetitle));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("in_play", new Emitter.Listener() {//即时插播事件 in_play
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-in_play-事件" + json);

                    Command cmmond = Utils.gson.fromJson(
                            json, Command.class);
                    exec(cmmond);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("close", new Emitter.Listener() {// 关机：close
            @Override
            public void call(Object... args) {
                try {
//                    String json = args[0].toString();
                    Logs.e("sio-close-事件");
                    handler.sendEmptyMessage(closew);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("close-time", new Emitter.Listener() {// 定时关机：close-time
            @Override
            public void call(Object... args) {
                try {
                    final String json = args[0].toString();
                    Logs.e("sio-close-time-事件" + json);
                    String head = WZDateUtil.toString(new Date(), "yyyy-MM-dd");
//                    Date date = new Date(Long.valueOf(json));
//                    final String hms = WZDateUtil.toString(date, "HH:mm:ss");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "系统将在" + json + "关闭", Toast.LENGTH_SHORT).show();
                        }
                    });
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    long close = dateFormat.parse(head + json).getTime();
                    long local = new Date().getTime();
                    if (close > local) {
                        handler.sendEmptyMessageDelayed(closew, close - local);
                    }
                    if (local > close) {
                        handler.sendEmptyMessageDelayed(closew, local - close);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("open", new Emitter.Listener() {//  开机：open
            @Override
            public void call(Object... args) {
                try {
//                    String json = args[0].toString();
                    Logs.e("sio-open-事件");
                    handler.sendEmptyMessage(openw);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("open-time", new Emitter.Listener() {//   定时开机：open-time
            @Override
            public void call(Object... args) {
                try {
                    final String json = args[0].toString();
                    Logs.e("sio-open-time-事件" + json);

                    String head = WZDateUtil.toString(new Date(), "yyyy-MM-dd");
//                    Date date = new Date(Long.valueOf(json));
//                    final String hms = WZDateUtil.toString(date, "HH:mm:ss");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "系统将在" + json + "开机", Toast.LENGTH_SHORT).show();
                        }
                    });
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    long open = dateFormat.parse(head + json).getTime();
                    long local = new Date().getTime();
                    if (open > local) {
                        handler.sendEmptyMessageDelayed(openw, open - local);
                    }
                    if (local > open) {
                        handler.sendEmptyMessageDelayed(openw, local - open);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("msgInsert", new Emitter.Listener() {//   计划插播：msgInsert  发送数据，跟之前一样
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-msgInsert-事件" + json);
                    Mings list = Utils.jsonToObject(json, new TypeToken<Mings>() {
                    });
                    if (list == null)
                        return;

                    app.setMings(list);
                    context.sendBroadcast(new Intent().setAction(MyAction.Mings));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("msgStop", new Emitter.Listener() {

            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                String json = arg0[0].toString();
                Logs.e("sio-msgStop-事件" + json);
                int id = Integer.parseInt(json);
                if (app.getMings() != null) {
                    if (app.getMings().getId() == id) {
                        Logs.e("sio-msgStop-事件app.getMings().getId() == id");
                        context.sendBroadcast(new Intent(MyAction.msgStop));
                        app.setMings(null);
                    }
                }

            }

        });

        socket.on("todayCalendar", new Emitter.Listener() {//  当天任务：todayCalendar   发送当天所有任务计划，请终端按时间执行
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-todayCalendar-事件" + json);
                    List<CalendarData> list = Utils.jsonToObject(json, new TypeToken<List<CalendarData>>() {
                    });
                    if (list.isEmpty())
                        return;

                    app.setCalendars(list.get(0).getDetails());
                    context.sendBroadcast(new Intent().setAction(MyAction.Calendar));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("nt", new Emitter.Listener() {//  当天任务：todayCalendar   发送当天所有任务计划，请终端按时间执行
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-nt-事件" + json);
                    Nt nt = new Nt();
                    nt.setRead(false);
                    nt.setContent(json);
                    nt.setCtiem(app.getServertime());
                    app.setNt(nt);

                    MyApp.db.save(nt);

//                    List<Nt> list = MyApp.db.findAll(Nt.class);
//                    TimeSort sort = new TimeSort();
//                    Collections.sort(list, sort);
//                    for (Nt n : list) {
//                        MyApp.db.update(n);
//                    }

                    context.sendBroadcast(new Intent().setAction(MyAction.NT));

                    uploadLog("弹出消息通知窗口-" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("vol", new Emitter.Listener() {

            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                try {
                    String json = arg0[0].toString();
                    Logs.e("sio-vol-事件" + json);
                    MyApp.setStreamVolume(Integer.valueOf(json));
                    uploadLog("音量更新为-" + json + "%");
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        });
        socket.on("success", new Emitter.Listener() {

            public void call(Object... arg0) {
                // TODO Auto-generated method stub
                isreg = true;
                String json = arg0[0].toString();
                Logs.e("sio-success-事件" + json);

            }

        });

    }

    private Emitter.Listener EVENT_CONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("连接" + MyApp.siourl + "成功");
            if (!isreg) {
                registVo();
            }
        }
    };

    private void registVo() {
        RegistVo registVo = new RegistVo();
        registVo.setVersion(Double.valueOf(Utils.getVersion(context)));
        registVo.setVoice(Utils.getCurrentVolume(context));
        String registVojson = Utils.gson.toJson(registVo);
        socket.emit("register", registVojson);
        Logs.e("发送注册事件" + registVojson);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isreg) {
                    registVo();
                }
                uploadLog("注册请求终端");
            }
        }, 5 * 1000);
    }

    private Emitter.Listener EVENT_CONNECT_ERROR = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("连接失败");
            if (isreg) {
                isreg = false;
            }
        }
    };
    private Emitter.Listener EVENT_CONNECT_TIMEOUT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("连接超时");
            if (isreg) {
                isreg = false;
            }
        }
    };
    private Emitter.Listener EVENT_DISCONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("断开连接");
            if (isreg) {
                isreg = false;
            }
        }
    };
    private Emitter.Listener EVENT_ERROR = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("EVENT_ERROR");
            if (isreg) {
                isreg = false;
            }
        }
    };
    private Emitter.Listener EVENT_MESSAGE = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("EVENT_MESSAGE");
        }
    };
    private Emitter.Listener EVENT_RECONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("EVENT_RECONNECT");
        }
    };
    private Emitter.Listener EVENT_RECONNECT_ATTEMPT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Logs.e("EVENT_RECONNECT_ATTEMPT");
        }
    };
    private Emitter.Listener EVENT_RECONNECT_ERROR = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Logs.e("EVENT_RECONNECT_ERROR");
        }
    };
    private Emitter.Listener EVENT_RECONNECT_FAILED = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("EVENT_RECONNECT_FAILED");
        }
    };
    private Emitter.Listener EVENT_RECONNECTING = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Logs.e("EVENT_RECONNECTING");
        }
    };

    private void exec(Command cmmond) {
        // TODO Auto-generated method stub
        try {
//            app.setCmmond(cmmond);
            switch (cmmond.getCommand()) {
                case 1:
                    uploadLog("实时插播-" + cmmond.getPlay().getSname());
                    NowinsActivity.exit();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", cmmond);
                    Intent intent = new Intent(context,
                            NowinsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
                case 2:
                    uploadLog("实时插播-执行快进");
                    context.sendBroadcast(new Intent(MyApp.FORWARD));
                    break;
                case 3:
                    uploadLog("实时插播-执行快退");
                    context.sendBroadcast(new Intent(MyApp.REWIND));
                    break;
                case 4:
                    context.sendBroadcast(new Intent(MyApp.Cancle));
                    break;
                case 5:
                case 7:
                    uploadLog("实时插播-执行暂停/播放");
                    context.sendBroadcast(new Intent(MyApp.PAUSE));
                    break;
                case 6:
                    uploadLog("实时插播-执行停止");
                    context.sendBroadcast(new Intent(MyApp.STOP));
                    break;
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void uploadLog(String operatetion) {
        UploadLogVo uploadLogVo = new UploadLogVo();
        uploadLogVo.setOperatetion(operatetion);
        String json = Utils.gson.toJson(uploadLogVo);
        Logs.e("uploadLog " + json);
        socket.emit("uploadLog", json);
    }
}
