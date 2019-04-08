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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import product.prison.app.MyApp;
import product.prison.model.Backs;
import product.prison.model.Nt;
import product.prison.utils.ImageUtils;
import product.prison.utils.Logs;
import product.prison.view.ad.RsType;
import product.prison.view.msg.NoticeActivity;

public abstract class BaseActivity extends Activity implements IBaseView {

    public static Activity activity;

    public <T, View> T f(int id) {
        return (T) super.findViewById(id);
    }

    private MyApp app;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (dialog == null)
                        return;
                    if (dialog.isShowing())
                        dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        initView(savedInstanceState);
        loadData();
        activity = this;
        app = (MyApp) getApplication();

        setBg();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApp.NT);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(MyApp.NT)) {
                    showAlterDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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
            Logs.e("bg" + url);
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

    private AlertDialog dialog = null;
    private int closetime = 10;

    private void showAlterDialog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    handler.removeMessages(0);
                }
            }
            final Nt nt = app.getNt();
            final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
            alterDiaglog.setIcon(R.mipmap.ic_launcher);//图标
            alterDiaglog.setTitle("通知");//文字
            if (nt.getContent().startsWith("h")) {
                alterDiaglog.setView(view);
                String temp = nt.getContent().substring(nt.getContent().lastIndexOf("."));
                switch (RsType.type.get(temp.toLowerCase())) {
                    case 1:
                        ImageView welcome_image = view.findViewById(R.id.welcome_image);
                        welcome_image.setVisibility(View.VISIBLE);
                        ImageUtils.display(welcome_image, nt.getContent());
                        handler.sendEmptyMessageDelayed(0, closetime * 1000);
                        break;
                    case 2:
                    case 3:

                        VideoView welcome_video = view.findViewById(R.id.welcome_video);
                        welcome_video.setVisibility(View.VISIBLE);
                        welcome_video.setVideoPath(nt.getContent());
                        welcome_video.start();
                        welcome_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                return true;
                            }
                        });
                        welcome_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            } else {
                alterDiaglog.setMessage(nt.getContent());
                handler.sendEmptyMessageDelayed(0, closetime * 1000);
            }

            alterDiaglog.setPositiveButton("查看", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(BaseActivity.this, NoticeActivity.class));

                }
            });
            alterDiaglog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //显示
            dialog = alterDiaglog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
