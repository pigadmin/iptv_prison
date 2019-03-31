package product.prison.view.ad;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.app.MyApp;
import product.prison.model.Command;
import product.prison.model.Play;
import product.prison.model.ScDetails;
import product.prison.model.Sources;
import product.prison.utils.ImageUtils;
import product.prison.utils.Utils;


public class NowinsActivity extends BaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    static Activity activity;
    Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    try {
                        switch (type) {
                            case 1:

                                break;
                            case 2:
                                if (msgvideo != null) {
                                    msgvideo.seekTo((int) (msgvideo
                                            .getCurrentPosition() + msgvideo
                                            .getDuration() * 0.05));
                                }

                                break;
                            case 3:
                                if (msgvideo != null)
                                    msgvideo.seekTo((int) (msgvideo.getCurrentPosition() + msgvideo
                                            .getDuration() * 0.05));
                                break;
                            case 4:
                                break;
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    break;
                case 2:

                    try {
                        switch (type) {
                            case 1:

                                break;
                            case 2:
                                if (msgvideo != null)
                                    msgvideo.seekTo((int) (msgvideo
                                            .getCurrentPosition() - msgvideo
                                            .getDuration() * 0.05));
                                break;
                            case 3:
                                if (msgvideo != null)
                                    msgvideo.seekTo((int) (msgvideo.getCurrentPosition() - msgvideo
                                            .getDuration() * 0.05));
                                break;
                            case 4:
                                break;
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    break;
                case 3:
                    loadData();
                    break;

                case 80:

                    handle.sendEmptyMessageDelayed(80, 5 * 1000);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Command command;


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(MyApp.PAUSE);
            filter.addAction(MyApp.STOP);
            filter.addAction(MyApp.FORWARD);
            filter.addAction(MyApp.REWIND);
            filter.addAction(MyApp.Cancle);
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            // TODO: handle exception
        }

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    Timer timer;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String com = intent.getAction();
            System.out.println(com);
            if (com.equals(MyApp.PAUSE)) {
                try {
                    if (msgvideo != null) {
                        if (msgvideo.isPlaying()) {
                            msgvideo.pause();
                        } else {
                            msgvideo.start();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else if (com.equals(MyApp.STOP)) {
                finish();
            } else if (com.equals(MyApp.FORWARD)) {

                try {
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            handle.sendEmptyMessage(1);
                        }
                    }, 0, 1 * 1000);

                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.toString());
                }

            } else if (com.equals(MyApp.REWIND)) {
                try {
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            handle.sendEmptyMessage(2);
                        }
                    }, 0, 1 * 1000);
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.toString());
                }
            } else if (com.equals(MyApp.Cancle)) {
                if (timer != null) {
                    timer.cancel();
                }

            }
        }
    };
    int type = 0;
    String resurl;
    Play play;


    private void sucai() {//素材
        try {
            resurl = play.getMaterial().getPath();
            String temp = play.getMaterial().getPath().substring(resurl.lastIndexOf(".")).toLowerCase();
            System.out.println(resurl + "*************" + temp);
            type = RsType.type.get(temp);
            AllGonn();
            switch (type) {
                case 1://ResImage
                    msgimg.setVisibility(View.VISIBLE);
                    playimg();
                    //                next();
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
                    System.out.println("====================");
                    msgweb.setVisibility(View.VISIBLE);
                    playweb();
                    break;
                case 5://ResOffice
                    cutimg = 0;
                    msgimg.setVisibility(View.VISIBLE);
                    imglist = play.getMaterial().getDetails();
                    handler.sendEmptyMessage(0);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ScDetails> imglist = new ArrayList<>();
    private int cutimg = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (imglist.isEmpty())
                        return;
                    if (cutimg >= imglist.size()) {
                        cutimg = 0;
                    }
                    String tmpurl = imglist.get(cutimg).getPath();
                    ImageUtils.display(msgimg, tmpurl);
                    cutimg++;
                    if (imglist.size() > 1) {
                        handler.sendEmptyMessageDelayed(0, 3 * 1000);
                    }
                    break;
            }
        }
    };

    private void OtherRes() {
        try {
            String temp = resurl.substring(resurl.lastIndexOf(".")).toLowerCase();
            System.out.println(resurl + "*************" + temp);
            type = RsType.type.get(temp);
            AllGonn();
            switch (type) {
                case 1://ResImage
                    msgimg.setVisibility(View.VISIBLE);
                    playimg();
                    //                next();
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
                    System.out.println("====================");
                    msgweb.setVisibility(View.VISIBLE);
                    playweb();
                    break;
                case 5://ResOffice
//                    msgimg.setVisibility(View.VISIBLE);
//                    playweb();
                    cutimg = 0;
                    msgimg.setVisibility(View.VISIBLE);
                    imglist = play.getMaterial().getDetails();
                    handler.sendEmptyMessage(0);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Sources sources;
    int cursource;

    private void Vod() {
        try {
            sources = play.getSource();
            msgname.setText(getString(R.string.insname) + sources.getName());
            resurl = sources.getDetails().get(0).getFilePath();
            String temp = resurl.substring(resurl.lastIndexOf(".")).toLowerCase();
            System.out.println(resurl + "*************" + temp);
            type = RsType.type.get(temp);

            AllGonn();
            switch (type) {
                case 1://ResImage
                    msgimg.setVisibility(View.VISIBLE);
                    playimg();
                    //                next();
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
                    cutimg = 0;
                    msgimg.setVisibility(View.VISIBLE);
                    imglist = play.getMaterial().getDetails();
                    handler.sendEmptyMessage(0);
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
    }

    private void next() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (cursource < mings.getSources().size() - 1) {
//                    cursource++;
//                } else {
//                    cursource = 0;
//                }
//                OtherRes();
//            }
//        }, 5 * 1000);
    }


    private void AllGonn() {
        msgimg.setVisibility(View.GONE);
        msgvideo.setVisibility(View.GONE);
        msgweb.setVisibility(View.GONE);

    }


    static VideoView msgvideo;
    ImageView msgimg;
    TextView msgname;
    WebView msgweb;


    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
//        if (!mings.getSources().isEmpty()) {
//            if (cursource < mings.getSources().size() - 1) {
//                cursource++;
//            } else {
//                cursource = 0;
//            }
//            OtherRes();
//        }

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
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public static void exit() {
        if (activity != null) {
            activity.finish();
        }

    }


    @Override
    public void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        activity = this;
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
    }

    @Override
    public void loadData() {
        command = (Command) getIntent().getExtras().get("key");
        System.out.println(command.getCommand());
        if (command != null) {
            play = command.getPlay();
//            play.getSname()
            type = play.getStype();
            switch (type) {
                case 1://素材
                    msgname.setText(getString(R.string.insname) + play.getSname());
                    resurl = play.getSurl();
                    sucai();
                    break;
                case 2://直播
                    AllGonn();
                    msgvideo.setVisibility(View.VISIBLE);
                    msgname.setText(getString(R.string.insname) + play.getSname());
                    resurl = play.getSurl();
                    msgvideo.setVideoPath(resurl);
                    System.out.println(resurl + "*************直播");
                    break;
                case 3://点播
                    Vod();
                    break;
                case 4://临时文件
                    msgname.setText(getString(R.string.insname) + play.getSname());
                    resurl = play.getSurl();
                    OtherRes();
                    break;

            }
        }

    }

    @Override
    public int getContentId() {
        return R.layout.activity_nowins;
    }
}
