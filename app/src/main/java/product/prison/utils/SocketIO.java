package product.prison.utils;

import android.content.Context;
import android.content.Intent;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.model.MsgData;
import product.prison.model.RegistVo;
import product.prison.model.TMenu;


public class SocketIO {
    public static Socket socket;
    private Context context;
    private MyApp app;

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
                } catch (Exception e) {
                    e.printStackTrace();
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
                    if (list.isEmpty())
                        return;
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("close", new Emitter.Listener() {// 关机：close
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-close-事件" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("close-time", new Emitter.Listener() {// 定时关机：close-time
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-close-time-事件" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("open", new Emitter.Listener() {//  开机：open
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-open-事件" + json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("open-time", new Emitter.Listener() {//   定时开机：open-time
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-open-time-事件" + json);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on("todayCalendar", new Emitter.Listener() {//  当天任务：todayCalendar   发送当天所有任务计划，请终端按时间执行
            @Override
            public void call(Object... args) {
                try {
                    String json = args[0].toString();
                    Logs.e("sio-todayCalendar-事件" + json);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void disconnect() {
        socket.disconnect();
    }

    private Emitter.Listener EVENT_CONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("连接" + MyApp.siourl + "成功");
            RegistVo registVo = new RegistVo();
            registVo.setVersion(Double.valueOf(Utils.getVersion(context)));
            registVo.setVoice(Utils.getCurrentVolume(context));
            String registVojson = Utils.gson.toJson(registVo);
            socket.emit("register", registVojson);
            Logs.e("发送注册事件" + registVojson);
        }
    };
    private Emitter.Listener EVENT_CONNECT_ERROR = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("连接失败");
        }
    };
    private Emitter.Listener EVENT_CONNECT_TIMEOUT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("连接超时");
        }
    };
    private Emitter.Listener EVENT_DISCONNECT = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("断开连接");
        }
    };
    private Emitter.Listener EVENT_ERROR = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("EVENT_ERROR");
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
            Logs.e("EVENT_RECONNECT_ATTEMPT");
        }
    };
    private Emitter.Listener EVENT_RECONNECT_ERROR = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logs.e("EVENT_RECONNECT_ERROR");
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
            Logs.e("EVENT_RECONNECTING");
        }
    };

}
