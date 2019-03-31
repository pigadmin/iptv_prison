package product.prison.view.ad;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.app.MyApp;
import product.prison.model.Calendar;
import product.prison.model.MaterialType;
import product.prison.model.MaterialVO;
import product.prison.model.ProgramContentVO;
import product.prison.model.ProgramListVO;
import product.prison.model.ProgramVO;
import product.prison.utils.Utils;
import product.prison.utils.ViewUtil;
import product.prison.utils.ZipUtil;

public class WeekActivity extends BaseActivity {
    MyApp app;


    // 播放信息
//    private ProgramListVO prolistVO = new ProgramListVO();
    private ProgramVO programVO = new ProgramVO();

    private void craveView() {
        try {
            System.out.println("===craveView()===");
            if (prolistVO != null) {
                // 第一次执行
                System.out.println("第一次执行===" +
                        prolistVO.getPrograms().size());
                programVO = prolistVO.getPrograms().get(0);
                // 背景

                background(programVO);
//
                buildCycle();

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 每隔一个时间段布置一次节目单
     */
    int testi;
    private Timer programTimer;

    private void buildCycle() {
        // Log.i(TAG, "进入 buildCycle....................................");
        try {
            if (programTimer == null) {
                programTimer = new Timer();
            } else {
                programTimer.cancel();
                programTimer.purge();
                programTimer = new Timer();
            }

            List<ProgramVO> programs = prolistVO.getPrograms();
            long totalTime = 0;
            for (ProgramVO p : programs) {
                totalTime = totalTime + (p.getTime() * 1000L);
            }
            if (totalTime != 0) {
                programTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        palyProgram();
                    }
                }, 0, totalTime);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private static final String PROGRAM_DATA = "program";
    // 清屏，显示广告节目
    private static final int SHOW_AD_MSG = 2;
    private static final int UPDATE_PROGRAM_MSG = 3;

    private void palyProgram() {
        handler.removeMessages(SHOW_AD_MSG);

        List<ProgramVO> programs = prolistVO.getPrograms();

        System.out.println("共有节目：" + programs.size());

        long totalTime = 0;
        for (int i = 0; i < programs.size(); i++) {
            ProgramVO p = programs.get(i);
            // 把节目放进消息
            Message msg = new Message();
            msg.what = SHOW_AD_MSG;
            Bundle data = new Bundle();
            data.putSerializable(PROGRAM_DATA, p);
            msg.setData(data);
            boolean r = handler.sendMessageDelayed(msg, totalTime);
            System.out.println("..................................."
                    + totalTime + " 后执行" + r);
            // 下个节目运行开始时间等于前面节目加起来的时间
            totalTime = totalTime + (p.getTime() * 1000L);
            System.out.println("下个节目运行开始时间等于前面节目加起来的时间:::::::::::::::::"
                    + totalTime);

        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case SHOW_AD_MSG:
                        System.out
                                .println("*************************重新创建视图。。。。。。。。。。。。。。。");

                        loadData();

                        ProgramVO p = (ProgramVO) msg.getData().getSerializable(
                                PROGRAM_DATA);

                        background(p);
                        createScreen(p);
                        break;
                    case UPDATE_PROGRAM_MSG:
                        loadData();
                        break;
                    case 4:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case 5:
                        ActivityManager ac = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        ac.restartPackage(WeekActivity.this.getPackageName());
                        break;


                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    };
    private SurfaceView surfaceView;
    int index;

    private void createScreen(ProgramVO v) {

        /**** 执行块（开始：刷一次屏幕） ********************************************************************/

        System.out.println(v.getWidth() + "*" + v.getHeight());
        System.out.println("播放时间：" + v.getTime() + " 秒");

        // 本地素材

        for (ProgramContentVO vo : v.getContents()) {

            int ty = vo.getClassify();
            System.out.println("类型" + ty);

            // 构造图片屏幕
            if (ty == MaterialType.zero || ty == MaterialType.PIC
                    || ty == MaterialType.PPT || ty == MaterialType.WORD
                    || ty == MaterialType.EXCEL || ty == MaterialType.PDF) {

                try {
                    ImageView view = ViewUtil.createImageView(this, vo,
                            matrailMap);
                    frame.addView(view);
                } catch (OutOfMemoryError e) {
                    // TODO: handle exception
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            if (ty == MaterialType.TXT) {
                // TextView textView = ViewUtil.createTextView(context, vo,
                // matrailMap);
                WebView webView = ViewUtil.createWebView2(this, vo, matrailMap);
                frame.addView(webView);
            }
            // 构造flash屏幕
            if (ty == MaterialType.FLASH || ty == MaterialType.HTML) {
                try {
                    WebView webView = ViewUtil.createWebView(this, vo, matrailMap);
                    frame.addView(webView);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

            // 构造TV屏幕
            if (ty == MaterialType.VIDEO || ty == MaterialType.IntVIDEO) {

                try {
                    index = 0;
                    surfaceView = startVideo(this, vo, matrailMap);
                    frame.addView(surfaceView);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            // 音乐播放器
            if (ty == MaterialType.AUDIO) {
                String key = vo.getMaterials().get(0).getPath().split("/")[vo
                        .getMaterials().get(0).getPath().split("/").length - 1];
                try {
                    if (media == null) {
                        media = new MediaPlayer();
                    } else {
                        if (media.isPlaying()) {
                            media.stop();
                            media.release();
                        }
                    }
                    media.reset();
                    media.setDataSource(matrailMap.get(key));

                    media.prepare();
                    media.start();
                    media.setLooping(true);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
        /**** 执行块（结束） ********************************************************************/
    }

    private List<String> pathList;
    private List<Integer> livetype;
    private int vodtype;
    String path;

    public SurfaceView startVideo(Context context, final ProgramContentVO vo,
                                  HashMap<String, String> matrailMap) {
        // 启动视频
        List<MaterialVO> list = vo.getMaterials();

        pathList = new ArrayList<String>();
        livetype = new ArrayList<Integer>();

        if (vo.getClassify() == MaterialType.IntVIDEO
                || vo.getClassify() == MaterialType.VIDEO) {
            pathList.clear();
            for (int i = 0; i < vo.getMaterials().size(); i++) {
                path = vo.getMaterials().get(i).getPath();
                pathList.add(path);
                livetype.add(vo.getClassify());
            }
        }

        surfaceView = ViewUtil.createSurfaceView(context, vo, matrailMap);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub
            }

            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
//                System.out.println("surfaceCreated");
                vodtype = vo.getClassify();
                initMediaPlayer(pathList, surfaceView, vo.getClassify());
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                // TODO Auto-generated method stub

            }
        });
        return surfaceView;
    }

    MediaPlayer media;

    public void initMediaPlayer(final List<String> materialPath,
                                final SurfaceView surfaceView, final int classify) {
        try {

            media = new MediaPlayer();

            media.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mdplayer) {
                    // TODO Auto-generated method stub
                    mdplayer.start();
                }
            });
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    int tmp = ++index;
//                    System.out.println(tmp);
//                    System.out.println(pathList.size());
                    if (tmp >= pathList.size()) {
                        index = 0;
                    }
                    play();
                }
            });
            media.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    // if (HttpUtil.IsHaveInternet(getApplicationContext())) {
                    // media.start();
                    // }
                    return true;
                }
            });
            play();

        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    private void play() {
        // TODO Auto-generated method stub

        try {
            System.out.println("===================");

            media.stop();
            media.reset();
            media.setAudioStreamType(AudioManager.STREAM_MUSIC);

            String[] path = pathList.get(index).split("/");
            System.out.println(livetype.get(index));
            System.out.println(pathList.get(index));

            if (livetype.get(index) == MaterialType.VIDEO) {
                media.setDataSource(pathList.get(index));
                media.setDisplay(surfaceView.getHolder());
                media.prepareAsync();
            } else if (livetype.get(index) == MaterialType.IntVIDEO) {
                File file = new File(ZipUtil.res_file + File.separator
                        + path[path.length - 1]);
                if (file.exists()) {
                    media.setDataSource(ZipUtil.res_file + File.separator
                            + path[path.length - 1]);
                    media.setDisplay(surfaceView.getHolder());
                    media.prepareAsync();
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }

    }

    // 背景
    private void background(ProgramVO p) {
        try {

            int type = p.getScreenType();

            System.out.println("屏幕====" + type);

            if (type == 1) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (type == 2) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            // 背景
            MaterialVO bg = p.getBackground();

            if (bg != null) {

                if (bg.getPath() != null && !bg.getPath().equals("")) {
                    if (bg.getClassify() == MaterialType.PIC) {
                        System.out.println("background====" + bg.getPath());
                        String key = bg.getPath().split("/")[bg.getPath()
                                .split("/").length - 1];
                        System.out.println(key);
                        Drawable d = null;
                        try {
                            d = new BitmapDrawable(matrailMap.get(key));
                        } catch (OutOfMemoryError e) {
                            // TODO: handle exception
                        }
                        frame.setBackgroundDrawable(d);
                    }
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // 本地素材
    private HashMap<String, String> matrailMap = new HashMap<String, String>();
    private HashMap<String, String> matrailMap2 = new HashMap<String, String>();
    // 当前播放（信息）
    private ProgramListVO prolistVO = new ProgramListVO();

    // 当前文件夹名称
    private String fname = "";

    private void initData(String fname, String main) {
        try {
            this.fname = fname;
            matrailMap.clear();
            matrailMap.putAll(ZipUtil.materailList(fname));
            prolistVO = Utils.gson.fromJson(main, ProgramListVO.class);
            msgname.setText(getString(R.string.weekname) + prolistVO.getName());
            matrailMap2.putAll(matrailMap);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    FrameLayout frame;
    TextView msgname;


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    static Activity activity;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    public static void exit() {
        if (activity != null) {
            activity.finish();
        }
    }

    Calendar calendar;

    @Override
    protected void onStop() {
        app.setWeek(false);
        handler.removeMessages(SHOW_AD_MSG);
        handler.removeMessages(UPDATE_PROGRAM_MSG);
        if (programTimer != null) {
            programTimer.cancel();
            programTimer.purge();
        }
        super.onStop();
    }

    String head = new SimpleDateFormat("yyyy-MM-dd ").format(new Date(System
            .currentTimeMillis()));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void stop() {
        try {
            calendar = (Calendar) getIntent().getExtras().get("key");

            long end = (long) sdf.parse(head
                    + calendar.getTime_end()).getTime();

            long cur = System.currentTimeMillis();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, end - cur);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        app = (MyApp) getApplication();
        activity = this;
        frame = f(R.id.frame);
        msgname = f(R.id.msgname);

        stop();
    }

    @Override
    public void loadData() {
        String fname = ZipUtil.gainNewestFileName();
        if (fname != null) {
            System.out.println("初始化数据。。。。");
            // 该文件夹下的描述文件
            String main = ZipUtil.readMainFile(fname);
            System.out.println("@@@@@@@@" + main);
            // 播放数据
            initData(fname, main);
//            // 生成界面
            craveView();
        }
    }

    @Override
    public int getContentId() {
        return R.layout.activity_week;
    }
}
