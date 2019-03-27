package product.prison;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import product.prison.app.MyApp;
import product.prison.model.TGson;
import product.prison.model.WelcomeAd;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class WelcomeActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private long timeout = 24 * 60 * 60 * 1000;

    private CountDownTimer countDownTimer = null;
    private MyApp myApp;
    private ImageView welcome_image;
    private VideoView welcome_video;
    private TextView welcome_tips, welcome_time_tips;
    private WelcomeAd currentad;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        if (!Utils.isNetworkConnected(WelcomeActivity.this)) {
                            Logs.e("网络异常");
                            handler.sendEmptyMessageDelayed(0, 1000);
                            return;
                        }
                        Logs.e("网络已连接");
                        checkAuth();
                        break;
                    case 1:
                        currentad = (WelcomeAd) msg.getData().getSerializable("key");
                        switch (currentad.getType()) {
                            case 1:
                                playimg();
                                playmusic();
                                break;
                            case 2:
                                playvideo();
                                break;
                        }
                        break;
                    case 2:
                        toMain();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override

    public void initView(Bundle savedInstanceState) {
        myApp = (MyApp) getApplication();
        welcome_image = f(R.id.welcome_image);
        welcome_video = f(R.id.welcome_video);
        welcome_video.setOnPreparedListener(this);
        welcome_video.setOnErrorListener(this);
        welcome_tips = f(R.id.welcome_tips);
        welcome_time_tips = f(R.id.welcome_time_tips);
        setMediaListene();
        handler.sendEmptyMessage(0);
    }

    private MediaPlayer mediaPlayer;

    private void setMediaListene() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);

    }


    @Override
    public void loadData() {
    }


    private void checkAuth() {
        RequestParams params = new RequestParams(MyApp.apiurl + "checkAuth");
        Logs.e(MyApp.apiurl + "checkAuth");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<Long> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<Long>>() {
                            }.getType());
                    if (json.getData() == null)
                        return;
                    myApp.setServertime(json.getData());
                    getWelComeAd();
                    Logs.e(Utils.formatMyTime(json.getData()));
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


    private void getWelComeAd() {

        RequestParams params = new RequestParams(MyApp.apiurl + "getWelComeAd");
        params.addBodyParameter("mac", MyApp.mac);
        params.addBodyParameter("templateType", MyApp.templateType + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e(result);
                    TGson<List<WelcomeAd>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<WelcomeAd>>>() {
                            }.getType());
                    if (json.getData() == null || json.getData().isEmpty())
                        return;
                    long totalTime = 0;
                    for (WelcomeAd ad : json.getData()) {
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle data = new Bundle();
                        data.putSerializable("key", ad);
                        msg.setData(data);
                        boolean r = handler.sendMessageDelayed(msg, totalTime);
                        totalTime = totalTime + (ad.getInter() * 1000L);
                    }
                    new CountDownTimer(totalTime, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            welcome_time_tips.setText((millisUntilFinished / 1000) + "");
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();
                    handler.sendEmptyMessageDelayed(2, totalTime);

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

    private void playimg() {
        if (welcome_video.getVisibility() == View.VISIBLE) {
            welcome_video.setVisibility(View.GONE);
            if (welcome_video.isPlaying()) {
                welcome_video.stopPlayback();
            }
        }
        if (welcome_image.getVisibility() == View.GONE) {
            welcome_image.setVisibility(View.VISIBLE);
        }
        x.image().bind(welcome_image, currentad.getFilePath());
    }


    private void playmusic() {
        // TODO Auto-generated method stub

        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(WelcomeActivity.this,
                    Uri.parse(currentad.getBgFile()));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void playvideo() {
        if (welcome_image.getVisibility() == View.VISIBLE) {
            welcome_image.setVisibility(View.GONE);
        }
        if (welcome_video.getVisibility() == View.GONE) {
            welcome_video.setVisibility(View.VISIBLE);
        }
        welcome_video.setVideoURI(Uri.parse(currentad.getFilePath()));
    }

    @Override
    public int getContentId() {
        return R.layout.activity_welcome;
    }

    void toMain() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
        mp.start();

    }
}
