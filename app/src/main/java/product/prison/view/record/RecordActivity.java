package product.prison.view.record;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.RecordListAdapter;
import product.prison.app.MyApp;
import product.prison.model.TranscribeData;
import product.prison.utils.Logs;
import product.prison.utils.SpUtils;
import product.prison.utils.Utils;

public class RecordActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MyApp app;
    private int defaultchannel = 0;
    private int channel = 0;
    private TextView live_no_b;
    private VideoView live_player;
    private List<TranscribeData> livelist = new ArrayList<>();
    private AudioManager audioManager;
    private ListView live_list;
    private TextView live_count;
    private PopupWindow popupWindow = null;
    private View view;
    private String keych = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        if (livelist.isEmpty())
                            return;
//            if (live_player.isPlaying())
//                live_player.stopPlayback();
                        String name = livelist.get(channel).getName();
                        url = livelist.get(channel).getPath();
                        Logs.e(channel + ". " + name + " " + url);

                        live_no_b.setText((channel + 1) + "");
                        handler.removeMessages(1);
                        handler.sendEmptyMessageDelayed(1, 3 * 1000);

                        if (url.startsWith("h") || url.startsWith("r") || url.startsWith("u")) {
                            live_player.setVideoPath(url);
                        } else {
                            Toast.makeText(getApplicationContext(), name + " " + getString(R.string.urlerror), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        break;
                    case 1://关闭频道显示
                        live_no_b.setText("");
                        break;
                    case 2://关闭频道列表显示
                        if (popupWindow == null || !popupWindow.isShowing())
                            return;
                        popupWindow.dismiss();
                        break;
                    case 3:
                        try {
                            if (Long.parseLong(keych) > livelist.size()) {
                                keych = "";
                                Toast.makeText(getApplicationContext(), "没有此频道,节目编号1-" + livelist.size(), Toast.LENGTH_SHORT).show();
                                handler.removeMessages(1);
                                handler.sendEmptyMessageDelayed(1, 3 * 1000);
                                return;
                            }
                            channel = Integer.parseInt(keych) - 1;
                            keych = "";
                            handler.sendEmptyMessage(0);
                        } catch (Exception e) {
                            keych = "";
                            Toast.makeText(getApplicationContext(), "没有此频道,节目编号1-" + livelist.size(), Toast.LENGTH_SHORT).show();
                            handler.removeMessages(1);
                            handler.sendEmptyMessageDelayed(1, 3 * 1000);
                            e.printStackTrace();
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initView(Bundle savedInstanceState) {
        live_no_b = f(R.id.live_no_b);
        live_player = f(R.id.live_player);
        Utils.fullvideo(live_player);
        live_player.setOnPreparedListener(this);
        live_player.setOnCompletionListener(this);
        live_player.setOnErrorListener(this);
    }

    @Override
    public void loadData() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        channel = SpUtils.getInt(this, "record", defaultchannel);
        livelist = (List<TranscribeData>) getIntent().getExtras().get("key");

        handler.sendEmptyMessage(0);
    }


    private String url = "http://192.168.31.250:8105/wisdom_hotel/upload/1.ts";


    //显示节目列表
    private void show() {
        // TODO Auto-generated method stub
        try {
            if (popupWindow != null) {
//                Logs.e("@@@" + popupWindow.isShowing());
                if (popupWindow.isShowing())
                    return;
            }
//            System.out.println("@@@@@@2222");
            view = getLayoutInflater().inflate(R.layout.pop_live, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.dismiss();
                }
            });
            live_list = view.findViewById(R.id.live_list);
            live_count = view.findViewById(R.id.live_count);
            live_list.setAdapter(new RecordListAdapter(this, livelist));
            live_count.setText("频道总数(" + livelist.size() + ")");
            live_list.setSelectionFromTop(channel, 220);
            popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
//            System.out.println("@@@@@@3333");
            live_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> av, View v,
                                        int position, long id) {
                    channel = position;
                    handler.sendEmptyMessage(0);
                    handler.removeMessages(2);
                    handler.sendEmptyMessageDelayed(2, 5 * 1000);
                }
            });
            live_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    handler.removeMessages(2);
                    handler.sendEmptyMessageDelayed(2, 5 * 1000);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            handler.removeMessages(2);
            handler.sendEmptyMessageDelayed(2, 5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //按键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

//        Logs.e("@@@" + keyCode);

        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            keych += keyCode - 7;

            live_no_b.setText(keych);
            handler.removeMessages(3);
            handler.sendEmptyMessageDelayed(3, 1 * 1000);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_ENTER) {
            show();
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                downvol();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                upvol();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                upchanle();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                downchanle();
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                pause();
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    //onStop生命周期
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        SpUtils.putInt(this, "record", channel);
        super.onStop();
    }

    //上一个频道
    private void upchanle() {
        // TODO Auto-generated method stub
        try {
            ++channel;
            if (channel > livelist.size() - 1)
                channel = 0;
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    //下一个频道
    private void downchanle() {
        // TODO Auto-generated method stub
        try {
            --channel;
            if (channel < 0)
                channel = livelist.size() - 1;
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //增加音量
    private void upvol() {
        // TODO Auto-generated method stub
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
                        | AudioManager.FLAG_SHOW_UI);
    }

    //减少音量
    private void downvol() {
        // TODO Auto-generated method stub
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
                        | AudioManager.FLAG_SHOW_UI);
    }

    //暂停
    private void pause() {
        // TODO Auto-generated method stub
        try {
            if (live_player.isPlaying()) {
                live_player.pause();
            } else {
                live_player.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    //触摸监听
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            System.out.println("@@@@@@1111");
            show();
        }
        return super.onTouchEvent(event);
    }


    @Override
    public int getContentId() {
        return R.layout.activity_live;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        handler.sendEmptyMessageDelayed(0, 2 * 1000);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (live_player.isPlaying())
            live_player.stopPlayback();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (live_player.isPlaying())
            live_player.stopPlayback();
        mp.start();
        mp.setLooping(true);
    }
}
