package product.prison;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.DataOutputStream;
import java.io.OutputStream;

import product.prison.app.MyApp;
import product.prison.model.Backs;
import product.prison.model.Nt;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.SpUtils;
import product.prison.view.ad.RsType;
import product.prison.view.msg.NoticeActivity;

public abstract class BaseActivity extends Activity implements IBaseView {

    public static Activity activity;

    public <T, View> T f(int id) {
        return (T) super.findViewById(id);
    }

    private MyApp app;

    @Override
    protected void onStart() {
        super.onStart();
        activity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initView(savedInstanceState);
        loadData();

        app = (MyApp) getApplication();

        setBg();

    }


    private void setBg() {
        try {
            if (app.getLogoData() == null) return;
            if (app.getBg() != null) {
                getWindow().getDecorView().setBackground(app.getBg());
                return;
            }
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setFailureDrawableId(R.drawable.bg)
                    .setLoadingDrawableId(R.drawable.bg)
                    .build();
            String url = "";
            for (Backs backs : app.getLogoData().getBacks()) {
                if (backs.getType() == 1) {
                    url = backs.getPath();
                }
            }
            Logs.e("bg " + url);
            x.image().loadDrawable(url, imageOptions, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable bg) {
                    getWindow().getDecorView().setBackground(bg);
                    app.setBg(bg);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    if (key_temp.equals("111")) {
                        setip();
                    } else if (key_temp.equals("222")) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                    key_temp = "";
                    break;

            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            handler.removeMessages(0);
            key_temp += keyCode - 7;
            handler.sendEmptyMessageDelayed(0, 1 * 1000);
        }
        return super.onKeyDown(keyCode, event);
    }

    private String key_temp = "";


    private AlertDialog serverip_dialog;

    private void setip() {
        // TODO Auto-generated method stub
        try {
            serverip_dialog = new AlertDialog.Builder(this).create();
            serverip_dialog.setView(new EditText(this));
            serverip_dialog.show();
            serverip_dialog.setContentView(R.layout.serverip_dialog);
            final EditText serverip = serverip_dialog
                    .findViewById(R.id.serverip);
            serverip.setText( SpUtils.getString(getApplicationContext(), "ip", MyApp.ip));
            serverip.requestFocus();
            ImageButton serverip_ok = serverip_dialog
                    .findViewById(R.id.serverip_ok);
            serverip_ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    serverip_dialog.dismiss();
                    SpUtils.putString(getApplicationContext(), "ip", serverip.getText().toString());
                    Toast.makeText(getApplicationContext(), "修改IP", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());


                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }

    }





}
