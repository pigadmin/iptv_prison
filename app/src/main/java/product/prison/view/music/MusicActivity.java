package product.prison.view.music;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import product.prison.BaseActivity;
import product.prison.R;
import product.prison.adapter.MusicListAdapter;
import product.prison.adapter.SongGridAdapter;
import product.prison.adapter.SongListAdapter;
import product.prison.app.MyApp;
import product.prison.model.MusicData;
import product.prison.model.SongData;
import product.prison.model.SongalbumList;
import product.prison.model.SongsData;
import product.prison.model.TGson;
import product.prison.utils.Logs;
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

    @Override
    public void initView(Bundle savedInstanceState) {
        setMediaListene();


        left_list = f(R.id.left_list);


        right_grid = f(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
        right_list = f(R.id.right_list);
        right_list.setOnItemClickListener(this);


        layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        left_list.setLayoutManager(layoutmanager);

    }

    private void getSongType() {
        RequestParams params = new RequestParams(MyApp.apiurl + "getSongType");
        params.addBodyParameter("mac", MyApp.mac);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    Logs.e("getSongType "+result);
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
                    Logs.e("getsong "+result);
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
            String url = grid.get(index).getSongFile();
            Logs.e(url);
            if (!url.startsWith("h"))
                return;
            mediaPlayer.setDataSource(getApplicationContext(),
                    Uri.parse(url));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 停止播放
    private void stopMusic() {
        mediaPlayer.stop();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        stopMusic();
        super.onPause();
    }

    // 暂停播放//继续播放
    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }

    }

    @Override
    protected void onDestroy() {
        stopMusic();
        super.onDestroy();
    }
}
