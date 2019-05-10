package product.prison.view.video;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.VideoGridAdapter;
import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.model.TGson;
import product.prison.model.Vod;
import product.prison.model.VodData;
import product.prison.utils.Logs;
import product.prison.utils.SocketIO;
import product.prison.utils.SpUtils;
import product.prison.utils.Utils;
import product.prison.view.ad.AdActivity;

public class PlayerActivity extends BaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private VideoView res_video;
    private TextView res_video_title;
    private VodData vodData;
    private AlertDialog vod_time_dialog;
    private int vodtime = 0;
    private String url = "";
    private int vid = 0;
    private int position = 0;
    private String title = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    res_video_title.setVisibility(View.GONE);
                    break;
                case 1:
                    if (vodData.getAd() != null) {
                        Logs.e("vod aaad" + Utils.gson.toJson(vodData.getAd()));

                        String[] s = vodData.getAd().getInter().split(",");
                        for (String t : s) {
                            int ti = Integer.parseInt(t);
                            Logs.e(ti + "分钟后执行点播广告");
                            if (ti == 0) {
                                handler.sendEmptyMessage(2);
                            } else {
                                handler.sendEmptyMessageDelayed(2, ti * 60 * 1000);
                            }
                        }
                    }
                    break;
                case 2:
                    SocketIO.uploadLog("播放点播广告-" + vodData.getAd().getName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", (Serializable) vodData.getAd().getDetails());
                    startActivity(new Intent(getApplicationContext(), AdActivity.class).putExtras(bundle));
                    break;
            }
        }
    };

    @Override
    public void initView(Bundle savedInstanceState) {
        res_video = f(R.id.res_video);
        Utils.fullvideo(res_video);

        MediaController controller = new MediaController(this);
        res_video.setMediaController(controller);


        res_video_title = f(R.id.res_video_title);

        res_video.setOnPreparedListener(this);
        res_video.setOnCompletionListener(this);
        res_video.setOnErrorListener(this);


        IntentFilter filter = new IntentFilter();
        filter.addAction(MyAction.STOPAD);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyAction.STOPAD)) {
                finish();
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        handler.removeMessages(2);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!res_video.isPlaying()) {
            res_video.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (res_video.isPlaying()) {
            res_video.pause();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (res_video.getCurrentPosition() > 0) {
            SpUtils.putInt(this, "video" + vodData.getId(), res_video.getCurrentPosition());
        }
        super.onPause();
    }


    @Override
    public void loadData() {
        try {
            vodData = (VodData) getIntent().getSerializableExtra("key");
            position = getIntent().getIntExtra("position", 0);
            title = vodData.getName() + " (" + vodData.getDetails().get(position).getName() + ")";
            res_video_title.setText(title);
            res_video_title.setVisibility(View.VISIBLE);
            vodtime = SpUtils.getInt(this, "video" + vodData.getId(), 0);
            url = vodData.getDetails().get(position).getFilePath();
//            vid = vodData.getDetails().get(position).getId();
            vid = vodData.getId();
            Logs.e(url);
            if (vodtime > 0) {
                crt(vodtime);
            } else {

                SocketIO.uploadLog("播放点播正片-" + title);
                res_video.setVideoPath(url);
            }
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 5 * 1000);

            handler.sendEmptyMessage(1);
//            handler.sendEmptyMessageDelayed(1, 1000);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void vrecord() {
        RequestParams params = new RequestParams(MyApp.apiurl + "vrecord");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("vid", vid + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("vrecord " + result);
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

    public void crt(final int vodtime) {
        // TODO Auto-generated method stub
        vod_time_dialog = new AlertDialog.Builder(this).create();
        vod_time_dialog.setCancelable(false);
        if (vod_time_dialog != null && vod_time_dialog.isShowing()) {
            vod_time_dialog.dismiss();
        } else {
            vod_time_dialog.show();
        }
        vod_time_dialog.setContentView(R.layout.vod_time_dialog);

        TextView update_content = vod_time_dialog
                .findViewById(R.id.update_content);

        update_content.setText(getString(R.string.video_time).replace("x", getTimeStr(vodtime)));

        ImageButton update_ok = vod_time_dialog.findViewById(R.id.update_ok);

        update_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                res_video.setVideoPath(url);
                res_video.seekTo(vodtime);

//                vrecord(resData.getId());
                vod_time_dialog.dismiss();
                SocketIO.uploadLog("继续播放点播正片-" + title + getTimeStr(vodtime));
            }
        });
        ImageButton update_cancle = vod_time_dialog
                .findViewById(R.id.update_cancle);
        update_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vod_time_dialog.dismiss();
                res_video.setVideoPath(url);
                SocketIO.uploadLog("重新播放点播正片-" + title);
            }
        });

        vod_time_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                res_video.setVideoPath(url);
                SocketIO.uploadLog("重新播放点播正片-" + title);
            }
        });
    }

    private String getTimeStr(int d) {
        // TODO Auto-generated method stub

        int hour = 0;
        int minute = 0;
        int second = 0;

        second = d / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_player;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
//        mp.setLooping(true);
        vrecord();
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//        super.onBackPressed();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                res_video_title.setVisibility(View.VISIBLE);
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, 5 * 1000);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            res_video_title.setVisibility(View.VISIBLE);
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 5 * 1000);
        }
        return super.onTouchEvent(event);
    }
}
