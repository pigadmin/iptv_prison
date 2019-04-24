package product.prison.view.ad;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.app.MyApp;
import product.prison.model.Details;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.utils.Utils;

public class AdActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private CountDownTimer countDownTimer = null;
    private MyApp myApp;
    private ImageView welcome_image;
    private VideoView welcome_video;
    private TextView welcome_tips, welcome_time_tips;
//    private Live live = null;
//    private Ad ad;
    private List<Details> details = new ArrayList<>();
    private Details currentad;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    currentad = (Details) msg.getData().getSerializable("key");
                    switch (currentad.getType()) {
                        case 1:
                            playimg();
//                            playmusic();
                            break;
                        case 2:
                            playvideo();
                            break;
                    }
                    break;
                case 1:
                    toEnd();
                    break;
            }
        }
    };

    private void playimg() {
        try {
            if (welcome_video.getVisibility() == View.VISIBLE) {
                welcome_video.setVisibility(View.GONE);
                if (welcome_video.isPlaying()) {
                    welcome_video.stopPlayback();
                }
            }
            if (welcome_image.getVisibility() == View.GONE) {
                welcome_image.setVisibility(View.VISIBLE);
            }
            String url = currentad.getFilePath();
            if (url.isEmpty() || !url.startsWith("h"))
                return;
            Logs.e(url);
            ImageUtils.displayFIT_XY(welcome_image, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void playmusic() {
        // TODO Auto-generated method stub

        try {
//            String url = currentad.getBgFile();
//            if (url.isEmpty() || !url.startsWith("h"))
//                return;
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            mediaPlayer.setDataSource(getApplicationContext(),
//                    Uri.parse(url));
//            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void playvideo() {
        try {
            if (welcome_image.getVisibility() == View.VISIBLE) {
                welcome_image.setVisibility(View.GONE);
            }
            if (welcome_video.getVisibility() == View.GONE) {
                welcome_video.setVisibility(View.VISIBLE);
            }
            String url = currentad.getFilePath();
            if (url.isEmpty() || !url.startsWith("h"))
                return;
            Logs.e(url);
            welcome_video.setVideoURI(Uri.parse(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        welcome_image = f(R.id.welcome_image);
        welcome_video = f(R.id.welcome_video);
        welcome_video.setOnPreparedListener(this);
        welcome_video.setOnErrorListener(this);
        Utils.fullvideo(welcome_video);
        welcome_tips = f(R.id.welcome_tips);
        welcome_time_tips = f(R.id.welcome_time_tips);
        setMediaListene();
    }

    private MediaPlayer mediaPlayer;

    private void setMediaListene() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);

    }

//    private int type = 0;
//    private VodData vodData;

    @Override
    public void loadData() {
        try {
//            type = getIntent().getIntExtra("type", 0);
//            if (type == 1) {
//                live = (Live) getIntent().getSerializableExtra("key");
//                ad = live.getLiveAds();
//                SocketIO.uploadLog("播放直播广告-" + ad.getName());
//            } else {
//                vodData = (VodData) getIntent().getSerializableExtra("key");
//                ad = vodData.getAd();
//                SocketIO.uploadLog("播放点播广告-" + ad.getName());
//            }

//            details = ad.getDetails();
            details= (List<Details> ) getIntent().getSerializableExtra("key");

            if (details.isEmpty()) {
                handler.sendEmptyMessageDelayed(1, 100);
            } else {
                long totalTime = 0;
                for (Details ad : details) {
                    Message msg = new Message();
                    msg.what = 0;
                    Bundle data = new Bundle();
                    data.putSerializable("key", ad);
                    msg.setData(data);
                    boolean r = handler.sendMessageDelayed(msg, totalTime);
                    totalTime = totalTime + (ad.getInter() * 1000L);
                    //                totalTime = totalTime + (5 * 1000L);
                }
                if (totalTime < 1) {
                    handler.sendEmptyMessageDelayed(1, 100);
                    return;
                }

                new CountDownTimer(totalTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        welcome_time_tips.setText("广告剩余  " + (millisUntilFinished / 1000) + "秒");
                    }

                    @Override
                    public void onFinish() {
//                        handler.sendEmptyMessageDelayed(1, 100);
                        finish();
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        handler.removeMessages(1);
        return super.onKeyDown(keyCode, event);
    }

    private void toEnd() {
        finish();
//        if (type == 1) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("key", (Serializable) live);
//            startActivity(new Intent(this, LiveActivity.class).putExtras(bundle));
//        } else {
//            Intent intent = new Intent(this, PlayerActivity.class);
//            intent.putExtra("position", getIntent().getIntExtra("position", 0));
//            intent.putExtra("key", vodData);
//            startActivity(intent);
//        }

    }

    @Override
    public int getContentId() {
        return R.layout.activity_ad;
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
