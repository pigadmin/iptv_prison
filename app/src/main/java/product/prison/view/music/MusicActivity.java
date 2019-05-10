package product.prison.view.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.MusicListAdapter;
import product.prison.adapter.SongGridAdapter;
import product.prison.adapter.SongListAdapter;
import product.prison.app.MyApp;
import product.prison.broadcast.MyAction;
import product.prison.model.MusicData;
import product.prison.model.SongData;
import product.prison.model.SongalbumList;
import product.prison.model.SongsData;
import product.prison.model.TGson;
import product.prison.utils.Logs;
import product.prison.utils.SocketIO;
import product.prison.utils.Utils;

public class MusicActivity extends BaseActivity implements MusicListAdapter.OnItemClickListener, AdapterView.OnItemClickListener {

    private RecyclerView left_list;
    private LinearLayoutManager layoutmanager;
    private List<MusicData> list = new ArrayList<>();
    private List<SongalbumList> songalbumList = new ArrayList<>();
    private List<SongsData> grid = new ArrayList<>();
    private int type = 0;
    private int albumId = 0;
    private GridView right_grid;
    private ListView right_list;
    private MusicListAdapter adapter;
    private SongGridAdapter songGridAdapter;
    private SongListAdapter songListAdapter;
    private ImageButton music_left, music_play, music_right, music_voldown, music_volup;
    private SeekBar music_sebar;
    private TextView music_local, music_all, music_nowname, music_volnum;
    private AudioManager am;

    @Override
    public void initView(Bundle savedInstanceState) {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setMediaListene();

        left_list = f(R.id.left_list);


        right_grid = f(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
        right_list = f(R.id.right_list);
        right_list.setOnItemClickListener(this);


        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

        music_left = f(R.id.music_left);
        music_play = f(R.id.music_play);
        music_right = f(R.id.music_right);

        music_volup = f(R.id.music_volup);
        music_volup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });
        music_voldown = f(R.id.music_voldown);
        music_voldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        });


        music_sebar = f(R.id.music_sebar);

        music_local = f(R.id.music_local);
        music_all = f(R.id.music_all);
        music_nowname = f(R.id.music_nowname);


        music_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    index--;
                    Logs.e(index + "@@@@");
                    if (index < 0) {
                        index = 0;
                    }
                    playerSong();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
            }
        });
        music_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    ++index;
                    Logs.e(index + "@@@@");
                    if (index > grid.size() - 1) {
                        index = grid.size() - 1;
                    }
                    playerSong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 播放或者停止
        music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMusic();
            }
        });// 音乐播放位置
        music_sebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    staTimeStr = mediaPlayer.getCurrentPosition();
                    music_local.setText(getTimeStr(staTimeStr));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
            }
        });

//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                music_volsebar.getProgress(), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyAction.PAUSEBGMUSIC);
        filter.addAction(MyAction.GOONBGMUSIC);
        registerReceiver(receiver, filter);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals(MyAction.PAUSEBGMUSIC)) {
                    Logs.e("暂停----");

                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }

                }
                if (intent.getAction().equals(MyAction.GOONBGMUSIC)) {
                    Logs.e("继续播放-----");
                    if (!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void getSongType() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getSongType");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("getSongType " + result);
                    TGson<List<MusicData>> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<List<MusicData>>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    list = json.getData();
                    if (list == null || list.isEmpty())
                        return;
                    adapter = new MusicListAdapter(getApplicationContext(), list);
                    left_list.setAdapter(adapter);
                    adapter.setOnItemClickListener(MusicActivity.this);

                    updateSongGrid(0);
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

    private void updateSongGrid(int position) {
        try {
            type = list.get(position).getId();
            Logs.e("分类id" + type);
            albumId = 0;
            songalbumList = list.get(position).getSongalbumList();
            songGridAdapter = new SongGridAdapter(getApplicationContext(), songalbumList);
            right_grid.setAdapter(songGridAdapter);
            getsong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getsong() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getsong");
        params.addBodyParameter("mac", MyApp.mac);
        if (type != 0) {
            params.addBodyParameter("type", type + "");
        }
        if (albumId != 0) {
            params.addBodyParameter("albumId", albumId + "");
        }
        params.addBodyParameter("pageNo", "1");
        params.addBodyParameter("pageSize", "99999");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("getsong " + result);
                    TGson<SongData> json = Utils.gson.fromJson(result,
                            new TypeToken<TGson<SongData>>() {
                            }.getType());
                    if (!json.getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), json.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    grid = json.getData().getData();
                    Logs.e("歌曲长度" + grid.size());
                    songListAdapter = new SongListAdapter(getApplicationContext(), grid);
                    right_list.setAdapter(songListAdapter);

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

    @Override
    public void loadData() {
        getSongType();
    }


    @Override
    public int getContentId() {
        return R.layout.activity_music;
    }

    @Override
    public void onItemClick(View view, int position) {//左侧
        updateSongGrid(position);
        adapter.update(position);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (parent == right_grid) {//专辑
                albumId = songalbumList.get(position).getId();
                Logs.e("专辑" + songalbumList.get(position).getName() + "id=" + albumId);
                getsong();
            } else if (parent == right_list) {//播放
                Logs.e("播放位置" + position);
                index = position;
                playerSong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer mediaPlayer;
    private int index = -1;

    private void setMediaListene() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                music_play.setBackgroundResource(R.drawable.music_pause);
                endTimeStr = mp.getDuration();
                handler.sendEmptyMessage(0);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (index < grid.size() - 1) {
                    ++index;
                } else {
                    index = 0;
                }
                playerSong();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }

    // 播放哪一首歌
    private void playerSong() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        try {
            String name = grid.get(index).getName();
            SocketIO.uploadLog("播放歌曲-" + name);
            music_nowname.setText("正在播放：" + name);
            String url = grid.get(index).getSongFile();
            Logs.e(index + "正在播放：" + name + url);
            if (!url.startsWith("h"))
                return;
            mediaPlayer.setDataSource(getApplicationContext(),
                    Uri.parse(url));
            mediaPlayer.prepareAsync();
            songListAdapter.update(index);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 暂停播放//继续播放
    private void pauseMusic() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            music_play.setBackgroundResource(R.drawable.music_play);
        } else {
            mediaPlayer.start();
            music_play.setBackgroundResource(R.drawable.music_pause);
        }
    }

    // 获取歌曲时间
    private String getTimeStr(int time) {
        return new SimpleDateFormat("mm:ss").format(new Date(time));
    }

    private int staTimeStr = 0;
    private int endTimeStr = 0;

    // 设置音乐播放时间
    private void setPlayTime() {
        music_sebar.setMax(endTimeStr);
        music_sebar.setProgress(staTimeStr);
    }


    // 停止播放
    private void stopMusic() {
        music_local.setText("00:00");
        music_all.setText("00:00");
        staTimeStr = 0;
        endTimeStr = 0;
        music_sebar.setProgress(0);
        mediaPlayer.stop();
        music_play.setBackgroundResource(R.drawable.music_pause);
    }


    // 继续播放
    private void restMusic() {

//        myhandler.sendEmptyMessage(handles.SEEKBARCHANG);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        music_play.setBackgroundResource(R.drawable.music_play);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        music_all.setText(getTimeStr(endTimeStr));
                        staTimeStr = mediaPlayer.getCurrentPosition();
                        setPlayTime();
                        handler.sendEmptyMessage(1);

                        break;
                    case 1:
                        if (mediaPlayer.isPlaying()) {
                            staTimeStr = mediaPlayer.getCurrentPosition();
                            music_local.setText(getTimeStr(staTimeStr));
                            music_sebar.setProgress(staTimeStr);
                        }
                        handler.sendEmptyMessageDelayed(1, 1000);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    @Override
    protected void onPause() {
        super.onPause();
//        Logs.e("onPause");
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Logs.e("onRestart");
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        stopMusic();
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
