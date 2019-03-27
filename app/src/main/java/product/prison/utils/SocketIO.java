package product.prison.utils;

import android.content.Context;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import product.prison.app.MyApp;
import product.prison.model.RegistVo;


public class SocketIO {
    public static Socket socket;
    private Context context;

    public SocketIO(Context context) {

        try {
            this.context = context;
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
            socket.connect();
            Logs.e("开始连接" + MyApp.siourl);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
