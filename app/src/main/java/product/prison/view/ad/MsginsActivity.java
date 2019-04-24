package product.prison.view.ad;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.app.MyApp;
import product.prison.model.Mings;
import product.prison.model.MsgSgLives;
import product.prison.model.Sources;
import product.prison.utils.ImageUtils;
import product.prison.utils.SocketIO;
import product.prison.utils.Utils;

public class MsginsActivity extends BaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    MyApp app;
    Mings mings;
    MsgSgLives msgSgLives;
    Sources sources;


    int cursource = 0;


    String resurl;
    int type;

    private void OtherRes() {
        try {
            sources = mings.getSources().get(cursource);
            msgname.setText(getString(R.string.msginsname) + sources.getName());
            resurl = sources.getDetails().get(0).getFilePath();
            String temp = resurl.substring(resurl.lastIndexOf(".")).toLowerCase();
            System.out.println(resurl + "*************" + temp);
            type = RsType.type.get(temp);

            AllGonn();
            switch (type) {
                case 1://ResImage
                    msgimg.setVisibility(View.VISIBLE);
                    playimg();
                    break;
                case 2://ResAudio
                    msgvideo.setVisibility(View.VISIBLE);
                    playvideo();
                    break;
                case 3://ResVideo
                    msgvideo.setVisibility(View.VISIBLE);
                    playvideo();
                    break;
                case 4://ResTxt
                    msgweb.setVisibility(View.VISIBLE);
                    playweb();
                    break;
                case 5://ResOffice
                    msgimg.setVisibility(View.VISIBLE);
                    playweb();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playweb() {
        msgweb.loadUrl(resurl);
    }


    private void playvideo() {
        msgvideo.setVideoPath(resurl);
    }


    private void playimg() {
        ImageUtils.display(msgimg, resurl);
        next();
    }

    private void next() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cursource < mings.getSources().size() - 1) {
                    cursource++;
                } else {
                    cursource = 0;
                }
                OtherRes();
            }
        }, 5 * 1000);
    }


    private void AllGonn() {
        msgimg.setVisibility(View.GONE);
        msgvideo.setVisibility(View.GONE);
        msgweb.setVisibility(View.GONE);

    }


    VideoView msgvideo;
    ImageView msgimg;
    TextView msgname;
    WebView msgweb;


    private void stop() {
        long end = app.getMings().getEndTime();
        long cur = System.currentTimeMillis();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, end - cur);

    }

    @Override
    protected void onStop() {
        app.setMing(false);
        SocketIO.uploadLog("结束计划播放");
        super.onStop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        if (!mings.getSources().isEmpty()) {
            if (cursource < mings.getSources().size() - 1) {
                cursource++;
            } else {
                cursource = 0;
            }
            OtherRes();
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        mp.start();
        mp.setLooping(true);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
        mings = app.getMings();

        msgvideo = f(R.id.msgvideo);
        Utils.fullvideo(msgvideo);
        msgname = f(R.id.msgname);
        MediaController controller = new MediaController(this);
        msgvideo.setMediaController(controller);
        msgimg = f(R.id.msgimg);
        msgweb = f(R.id.msgweb);
        WebSettings websettings = msgweb.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
        msgweb.setBackgroundColor(Color.TRANSPARENT);
        msgweb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        msgweb.getSettings().setDefaultTextEncodingName("GBK");


        msgvideo.setOnPreparedListener(this);
        msgvideo.setOnCompletionListener(this);
        msgvideo.setOnErrorListener(this);


        stop();
    }

    @Override
    public void loadData() {
        try {
            String path = "";
            if (!mings.getMsgSgLives().isEmpty()) {
                AllGonn();
                msgvideo.setVisibility(View.VISIBLE);
                msgSgLives = mings.getMsgSgLives().get(0);
                msgname.setText(getString(R.string.msginsname) + msgSgLives.getLivesingle().getName());
                msgvideo.setVideoPath(msgSgLives.getLivesingle().getAddress());
                System.out.println(msgSgLives.getLivesingle().getAddress() + "*************直播");
            } else if (!mings.getSources().isEmpty()) {
                OtherRes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_msgins;
    }
}
